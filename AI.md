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

## Level-2

### Added List Management Functionality

Extended Pazuzu with task/item storage and retrieval capabilities:

- **Added storage array**: Created a fixed-size String array (100 items) to store user inputs
- **Implemented addToList() method**: Adds user input messages to the internal array with bounds checking
- **Implemented printList() method**: Displays all stored items in numbered format (1. Item, 2. Item, etc.)
- **Added "List" command**: When user types "List", the program calls printList() instead of adding to list
- **Enhanced feedback**: Changed echo message to "added: [input]" to clearly indicate when items are stored
- **Added item counter**: Tracks the number of items stored in the array

The program now functions as a simple task list manager where users can add items and view all stored items on demand.

## Level-3

### Task Management with Completion Status

Transformed the simple list manager into a comprehensive task management system:

#### Task Class Implementation
- **Created Task.java**: New public class with proper object-oriented design
- **Task fields**: `boolean isDone` (defaults to false) and `String name` (set by constructor)
- **Task methods**: `markDone()`, `markNotDone()`, `checkIsDone()`, and `getName()`
- **Complete documentation**: Added comprehensive Javadoc for all methods and class

#### Enhanced Pazuzu Functionality
- **Upgraded storage**: Changed from `String[]` to `Task[]` array for proper task objects
- **Modified addToList()**: Now creates Task objects instead of storing raw strings
- **Enhanced printList()**: Displays completion status with `[ ]` for incomplete and `[X]` for completed tasks
- **Task completion commands**: Added `mark n` and `unmark n` commands for task status management
- **Robust validation**: Range checking for task numbers with "No such task" error handling
- **User feedback**: Confirmation messages showing updated task status after mark/unmark operations
- **Error handling**: Handles invalid numbers and out-of-range task indices gracefully

#### New Command Structure
- `mark [number]`: Marks specified task as completed
- `unmark [number]`: Marks specified task as incomplete  
- `List`: Shows all tasks with visual completion indicators
- `bye`: Exits the program
- Any other input: Adds a new task to the list

The program now provides a complete task management experience with visual status tracking and interactive task completion management.
