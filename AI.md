# AI.md

## Level-1

### Changes Made to Pazuzu.java

Modified the Pazuzu class to implement interactive user input functionality:

- **Added user input handling**: Integrated `Scanner` to read user input from the console
- **Implemented echo functionality**: Program now prints back exactly what the user types
- **Added exit condition**: When user inputs "bye", the program prints "Bye." and terminates
- **Added interaction separator**: Prints "===========" between each user interaction to clearly separate them
- **Improved program flow**: Wrapped functionality in a continuous loop until exit condition is met
- **Added proper resource management**: Scanner is properly closed when program exits

The program now provides an interactive experience where users can type messages, see them echoed back, and cleanly exit by typing "bye".
