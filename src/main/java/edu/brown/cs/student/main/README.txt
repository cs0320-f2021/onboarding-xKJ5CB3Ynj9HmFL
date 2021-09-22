Project Name: Onboarding

Design Choices:
The Main class contains a REPL and calls MathBot CSVstars classes. The MathBot class instantiates objects
that an add and subtract two given numbers. The CSVstars class contains a method that
when called, reads the given CSV and populates a data structure with Star objects.

Errors/Bugs:
I have not yet run any unit or system tests, however I did jot down some edge cases
that I would test had I had more time:

- Calling naive_neighbors before loading a file
- Asking for more neighbors than csv has stars
- K is exceeded due to a tie
- Calling a file that does not exist
