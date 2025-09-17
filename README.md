# Pazuzu Task Manager

**Pazuzu** is a modern, intuitive task management application built with Java and JavaFX. It features a sleek messaging-style interface that makes managing your tasks feel as natural as sending a text message.

![Java](https://img.shields.io/badge/Java-17-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-17-blue)
![Gradle](https://img.shields.io/badge/Gradle-8.5-green)

## Features

- **Modern UI**: Clean, messaging-app inspired interface with circular profile pictures and message bubbles
- **Three Task Types**: Todo, Deadline, and Event tasks with rich formatting
- **Smart Search**: Find tasks quickly with keyword search
- **Task Editing**: Modify existing tasks without recreating them
- **Persistent Storage**: Your tasks are automatically saved and restored
- **Intuitive Commands**: Simple, natural language commands

## Quick Start

### Prerequisites
- **Java 17** or higher
- **JavaFX 17** (included in dependencies)

### Running Pazuzu

#### Option 1: Using Gradle (Recommended)
```bash
./gradlew run
```

#### Option 2: Using JAR file
```bash
java -jar build/libs/pazuzu.jar
```

#### Option 3: Using Launcher
```bash
java -cp build/libs/pazuzu.jar pazuzu.Launcher
```

## Command Guide

### Viewing Tasks

#### List All Tasks
```
List
```
Shows all your tasks with their completion status and details.

**Example output:**
```
1. [T][X] Buy groceries
2. [D][ ] Submit assignment (by: Oct 15 2024 23:59)
3. [E][X] Team meeting (from: Oct 10 2024 14:00 to: Oct 10 2024 15:30)
```

### Adding Tasks

#### Todo Tasks
```
todo <task description>
```

**Examples:**
```
todo Buy milk
todo Call dentist for appointment
todo Review project proposal
```

#### Deadline Tasks
```
deadline <task description> | <date> [time]
```

**Examples:**
```
deadline Submit CS2103T assignment | 2024-10-15 2359
deadline Pay electricity bill | 2024-12-01
deadline Complete project report | 15/10/2024 1800
```

#### Event Tasks
```
event <task description> | <start date> [time] | <end date> [time]
```

**Examples:**
```
event Team meeting | 2024-10-10 1400 | 2024-10-10 1530
event Conference | 2024-11-15 | 2024-11-17
event Birthday party | 15/12/2024 1900 | 15/12/2024 2300
```

### Managing Task Status

#### Mark Task as Done
```
mark <task number>
```

**Examples:**
```
mark 1    # Marks the first task as completed
mark 3    # Marks the third task as completed
```

#### Mark Task as Not Done
```
unmark <task number>
```

**Examples:**
```
unmark 2    # Marks the second task as incomplete
unmark 5    # Marks the fifth task as incomplete
```

### Deleting Tasks

#### Delete a Task
```
delete <task number>
```

**Examples:**
```
delete 1    # Deletes the first task
delete 4    # Deletes the fourth task
```

> **Note:** If you delete an incomplete task, Pazuzu will remind you that you're "not locked-in enough"

### Finding Tasks

#### Search by Keyword
```
find <keyword>
```

**Examples:**
```
find assignment      # Finds all tasks containing "assignment"
find meeting         # Finds all tasks containing "meeting"
find groceries       # Finds all tasks containing "groceries"
```

### Editing Tasks

#### Edit Task Details
```
edit <task number> | <new name> | <new date1> | <new date2>
```

Use `_` for fields you don't want to change.

**Examples:**
```
edit 1 | Buy organic milk | _ | _                    # Change only the name
edit 2 | _ | 2024-10-20 | _                          # Change only the deadline
edit 3 | Team standup | 2024-10-11 1000 | 2024-10-11 1100    # Change name and times
edit 4 | _ | _ | _                                    # Invalid - must change at least one field
```

**Field meanings by task type:**
- **Todo**: Only name can be edited (date fields ignored)
- **Deadline**: name = task name, date1 = deadline, date2 = ignored
- **Event**: name = task name, date1 = start time, date2 = end time

### Exiting

#### Exit the Application
```
bye
```

## Date and Time Formats

Pazuzu supports flexible date and time input formats:

### Supported Date Formats
- `yyyy-MM-dd` (e.g., `2024-10-15`)
- `yyyy/MM/dd` (e.g., `2024/10/15`)
- `dd-MM-yyyy` (e.g., `15-10-2024`)
- `dd/MM/yyyy` (e.g., `15/10/2024`)
- `MM-dd-yyyy` (e.g., `10-15-2024`)
- `MM/dd/yyyy` (e.g., `10/15/2024`)

### Time Format
- 24-hour format: `HHMM` (e.g., `1430` for 2:30 PM, `0900` for 9:00 AM)
- 3-digit format: `HMM` (e.g., `900` for 9:00 AM)
- If no time is specified, defaults to `00:00` (midnight)

### Complete Examples
```
2024-10-15 1430        # Oct 15, 2024 at 2:30 PM
15/10/2024 900         # Oct 15, 2024 at 9:00 AM
2024-12-25             # Dec 25, 2024 at midnight
```

## User Interface

Pazuzu features a modern, messaging-app inspired interface:

- **Message Bubbles**: Your commands appear as blue bubbles (right-aligned), Pazuzu's responses as gray bubbles (left-aligned)
- **Profile Pictures**: Circular profile pictures for both you and Pazuzu
- **Modern Input**: Rounded input field with smart send button that activates only when you type something
- **Clean Design**: iOS-inspired color scheme with proper spacing and typography

## Building from Source

### Prerequisites
- Java 17 SDK
- Gradle 8.5+

### Build Commands

#### Compile and Build
```bash
./gradlew build
```

#### Run Tests
```bash
./gradlew test
```

#### Generate JAR
```bash
./gradlew shadowJar
```

#### Clean Build
```bash
./gradlew clean build
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── pazuzu/
│   │       ├── Pazuzu.java              # Main controller
│   │       ├── Launcher.java            # Application launcher
│   │       ├── exception/               # Custom exceptions
│   │       ├── parser/                  # Command and date parsing
│   │       ├── storage/                 # File I/O operations
│   │       ├── task/                    # Task classes (Task, Deadline, Event)
│   │       ├── ui/                      # JavaFX UI components
│   │       └── util/                    # Utility classes
│   └── resources/
│       ├── images/                      # Profile pictures
│       └── view/                        # FXML layout files
└── test/
    └── java/                            # Unit tests
```

## Technical Details

- **Language**: Java 17
- **UI Framework**: JavaFX 17
- **Build System**: Gradle 8.5
- **Architecture**: MVC pattern with clear separation of concerns
- **Storage**: Plain text file format with automatic parsing
- **Testing**: JUnit 5 with comprehensive test coverage

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is part of the CS2103T Software Engineering course and is intended for educational purposes.

## Tips for Effective Use

1. **Use descriptive task names** - Make it easy to understand what needs to be done
2. **Set realistic deadlines** - Pazuzu will help you track them, but you need to set achievable goals
3. **Use the search feature** - Quickly find tasks with keywords when your list grows
4. **Edit instead of delete+recreate** - Save time by editing existing tasks
5. **Check your list regularly** - Use `List` command to stay on top of your tasks

---

**Happy task managing with Pazuzu!**