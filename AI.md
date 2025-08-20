# AI.md

# Model used: claude-4-sonnet

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

## Level-4

### Created 3 types of tasks
### Prompt: Create 2 .java files each represent a class that extends Task. Before we add them, add this new public method printTask() in Task that prints the Task as: [T][X<include x only if done>] <name of task>

Deadline: has a String deadline, is printed in printTask as: [D][X] <name> (by: <deadline>)

Event: has a String startDate and String endDate, is printed in printTask as: [E][X] <name> (from: <startDate> to: <endDate>)

When listing out the tasks in Pazuzu, use the printTask function instead. 
When scanner input start with "todo", the task is just Task, else if it starts with "deadline", create a Deadline and initialise the deadline field as whatever comes after "/" (and whatever between deadline and / will be the name), same logic goes for Event where you lookout for "Event" and two "/"

### Polymorphic Task System Implementation

Implemented a comprehensive polymorphic task management system with specialized task types:

#### Enhanced Base Task Class
- **Added printTask() method**: Base implementation prints `[T][X] name` format with completion status
- **Polymorphic design**: Method designed to be overridden by subclasses for specialized formatting
- **Consistent interface**: All task types now use uniform printing mechanism

#### Deadline Task Class (Deadline.java)
- **Extends Task**: Inherits all base functionality while adding deadline-specific features
- **Additional field**: `String deadline` for storing deadline information
- **Constructor**: `Deadline(String name, String deadline)` for initialization
- **Overridden printTask()**: Displays format `[D][X] name (by: deadline)`
- **Getter method**: `getDeadline()` for accessing deadline information
- **Complete documentation**: Comprehensive Javadoc for all methods

#### Event Task Class (Event.java)
- **Extends Task**: Inherits all base functionality while adding event-specific features
- **Additional fields**: `String startDate` and `String endDate` for event timing
- **Constructor**: `Event(String name, String startDate, String endDate)` for initialization
- **Overridden printTask()**: Displays format `[E][X] name (from: startDate to: endDate)`
- **Getter methods**: `getStartDate()` and `getEndDate()` for accessing timing information
- **Complete documentation**: Comprehensive Javadoc for all methods

#### Advanced Command Parsing System
- **Smart input parsing**: Recognizes different task type prefixes (`todo`, `deadline`, `event`)
- **Flexible format handling**: 
  - `todo <name>` → Creates basic Task
  - `deadline <name> /<deadline>` → Creates Deadline with parsed deadline
  - `event <name> /<startDate> /<endDate>` → Creates Event with parsed dates
- **Error handling**: Validates input format and provides helpful error messages
- **Backwards compatibility**: Unrecognized input creates basic Task objects

#### Enhanced User Experience
- **Improved feedback**: "Got it. I've added this task:" with task details and count
- **Consistent formatting**: All task operations use polymorphic `printTask()` method
- **Visual task types**: Clear differentiation between task types with `[T]`, `[D]`, `[E]` prefixes
- **Robust validation**: Comprehensive error checking for malformed input

#### Updated System Integration
- **Modified printList()**: Now uses polymorphic `printTask()` for proper type-specific formatting
- **Enhanced mark/unmark**: Feedback uses `printTask()` to show updated task with correct formatting
- **Centralized task creation**: New `handleTaskInput()` method manages all task parsing and creation
- **Type-agnostic operations**: Mark/unmark operations work seamlessly across all task types

The system now provides a sophisticated task management experience with multiple task types, each with specialized formatting and information storage, while maintaining a clean and intuitive user interface.

## Level-5
### Added exception handling
### Prompt: Ensure that Pazuzu only accept inputs that starts with the 3 type of tasks or the mark/unmask/list/bye etc commands defined. Any other input will throw an exception (undefinedCmdException, to be defined, when caught print the message "HUHH???!!!"). If we received one of the 3 tasks but formatted wrongly, eg no "/", or empty name, throw another exception (badTaskException, caught message: "Stupid Task!"). If we tried marking a done task or unmark an undone task, that is markingException, message: "Can't even keep track of your own task". Define all these exceptions in a new class PazuzuExceptions

### Comprehensive Error Handling Implementation

#### Custom Exception Classes (PazuzuExceptions.java)
- **UndefinedCmdException**: For unrecognized commands → "HUHH???!!!"
- **BadTaskException**: For malformed task input → "Stupid Task!"
- **MarkingException**: For redundant mark/unmark operations → "Can't even keep track of your own task"

#### Strict Input Validation
- **Command recognition**: Only accepts `todo`, `deadline`, `event`, `mark`, `unmark`, `List`, `bye`
- **Format validation**: Validates task structure (slashes, empty names, proper spacing)
- **Status validation**: Prevents marking done tasks or unmarking undone tasks

#### Enhanced Error Messages
- All exceptions caught with try-catch blocks in main loop
- Custom humorous error messages for different error types
- Program continues execution after displaying error messages

## Level-6
### Added Deletion
### Prompt: Replace Task[] items with an ArrayList<Task>, refactor the code in Pazuzu to work with the ArrayList instead of Array. 

In Pazuzu, create a new void deleteTask(int) method that removes the task in the arraylist at the given index (1-indexed not 0). The scanner input to detect is: delete <number>
Upon deletion the message is: Deleted task <content from printTask> \n Guess ur not locked-in enough for this

### ArrayList Refactoring and Delete Functionality

#### Data Structure Upgrade
- **Replaced Task[] array**: Switched to `ArrayList<Task>` for dynamic sizing
- **Removed manual counting**: No longer need `itemCount` variable
- **Updated all operations**: Refactored all array access to use ArrayList methods

#### Delete Command Implementation
- **Added delete command**: `delete <number>` removes tasks from list
- **Created deleteTask() method**: Handles 1-indexed task removal
- **Custom delete message**: Shows deleted task with "Guess ur not locked-in enough for this" message
- **Range validation**: Proper error handling for invalid task numbers



