package edu.brown.cs.student.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;

import com.google.common.collect.ImmutableMap;

import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {

  // use port 4567 by default when running server
  private static final int DEFAULT_PORT = 4567;

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    // set up parsing of command line flags
    OptionParser parser = new OptionParser();

    // "./run --gui" will start a web server
    parser.accepts("gui");

    // use "--port <n>" to specify what port on which the server runs
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);

    OptionSet options = parser.parse(args);
    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }

    try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
      String input;
      while ((input = br.readLine()) != null) {
        try {
          ArrayList<Star> stars = new ArrayList<Star>();
          input = input.trim();
          String[] arguments = input.split(" ");
          if (arguments[0].equals("add")) {
            MathBot x = new MathBot();
            System.out.println(x.add(Double.parseDouble(arguments[1]),
                    Double.parseDouble(arguments[2])));
          } else if (arguments[0].equals("subtract")) {
            MathBot x = new MathBot();
            System.out.println(x.subtract(Double.parseDouble(arguments[1]),
                    Double.parseDouble(arguments[2])));
          } else if (arguments[0].equals("stars")) {
            stars = this.stars(arguments[1]);
          } else if (arguments[0].equals("naive_neighbors")) {
            Double[] coordinates = new Double[3];
            if (arguments.length == 5) {
              coordinates[0] = Double.parseDouble(arguments[2]);
              coordinates[1] = Double.parseDouble(arguments[3]);
              coordinates[2] = Double.parseDouble(arguments[4]);
            } else if (arguments.length == 3) {
              Star inputtedStar = null;
              for (int i = 0; i < stars.size(); i++) {
                if (stars.get(i).getProperName().equals(arguments[2])) {
                  inputtedStar = stars.get(i);
                }
              }
              coordinates[0] = inputtedStar.getX();
              coordinates[1] = inputtedStar.getY();
              coordinates[2] = inputtedStar.getZ();
            }
            Comparator<Star> starDistanceComparator = new Comparator<Star>() {
              /**
               * Custom Comparator for the Priority Queue
               * @param star1 first star object
               * @param star2 second star object you are comparing to first star
               * @return an int whose value will determine the order of the Priority Queue
               */
              public int compare(Star star1, Star star2) {
                return Double.compare(star1.calcDistance(coordinates[0],
                        coordinates[1], coordinates[2]),
                        star2.calcDistance(coordinates[0], coordinates[1], coordinates[2]));
              }
            };
            PriorityQueue<Star> nearestNeighbors = new PriorityQueue<>(starDistanceComparator);
            for (int i = 0; i < stars.size(); i++) {
              nearestNeighbors.add(stars.get(i));
            }
            int i = 0;
            while (i < Double.parseDouble(arguments[1])) {
              String starName = nearestNeighbors.poll().getProperName();
              System.out.println(starName);
            }

          }
        } catch (Exception e) {
          // e.printStackTrace();
          System.out.println("ERROR: We couldn't process your input");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("ERROR: Invalid input for REPL");
    }

  }

  /**
   * processes the data in the given CSV file and stores it
   * @param fileName
   * @return an ArrayList of star objects
   */
  private ArrayList<Star> stars(String fileName) {
    CSVstars manyStars = new CSVstars();
    return manyStars.fillStars(fileName);
  }



  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration(Configuration.VERSION_2_3_0);

    // this is the directory where FreeMarker templates are placed
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port) {
    // set port to run the server on
    Spark.port(port);

    // specify location of static resources (HTML, CSS, JS, images, etc.)
    Spark.externalStaticFileLocation("src/main/resources/static");

    // when there's a server error, use ExceptionPrinter to display error on GUI
    Spark.exception(Exception.class, new ExceptionPrinter());

    // initialize FreeMarker template engine (converts .ftl templates to HTML)
    FreeMarkerEngine freeMarker = createEngine();

    // setup Spark Routes
    Spark.get("/", new MainHandler(), freeMarker);
  }

  /**
   * Display an error page when an exception occurs in the server.
   */
  private static class ExceptionPrinter implements ExceptionHandler<Exception> {
    @Override
    public void handle(Exception e, Request req, Response res) {
      // status 500 generally means there was an internal server error
      res.status(500);

      // write stack trace to GUI
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

  /**
   * A handler to serve the site's main page.
   *
   * @return ModelAndView to render.
   * (main.ftl).
   */
  private static class MainHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      // this is a map of variables that are used in the FreeMarker template
      Map<String, Object> variables = ImmutableMap.of("title",
          "Go go GUI");

      return new ModelAndView(variables, "main.ftl");
    }
  }
}
