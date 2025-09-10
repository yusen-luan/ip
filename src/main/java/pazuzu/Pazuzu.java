package pazuzu;

import pazuzu.exception.PazuzuExceptions;
import pazuzu.parser.CommandParser;
import pazuzu.storage.Storage;
import pazuzu.task.Task;
import pazuzu.task.TaskList;
/**
 * Main controller class that coordinates between different components 
 * to provide a task management system.
 */
public class Pazuzu {
    // Command strings
    private static final String BYE_COMMAND = "bye";
    private static final String LIST_COMMAND = "List";
    private static final String MARK_COMMAND = "mark ";
    private static final String UNMARK_COMMAND = "unmark ";
    private static final String DELETE_COMMAND = "delete ";
    private static final String FIND_COMMAND = "find ";
    private static final String TODO_COMMAND = "todo";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";
    
    // Response messages
    private static final String BYE_RESPONSE = "Bye.";
    private static final String NO_TASKS_RESPONSE = "No tasks in your list.";
    private static final String TASK_DONE_PREFIX = "done:\n  ";
    private static final String TASK_NOT_DONE_PREFIX = "not done:\n  ";
    private static final String TASK_ADDED_PREFIX = "Got it. I've added this task:\n  ";
    private static final String TASK_ADDED_SUFFIX = "\nNow you have ";
    private static final String TASK_ADDED_SUFFIX2 = " tasks in the list.";
    private static final String TASK_DELETED_PREFIX = "Deleted task ";
    private static final String TASK_NOT_LOCKED_IN = "\nGuess ur not locked-in enough for this";
    private static final String FOUND_TASKS_PREFIX = "Found:\n";
    
    // Error messages
    private static final String UNDEFINED_COMMAND_ERROR = "I don't understand that command. Please try again.";
    private static final String BAD_TASK_ERROR = "Invalid task format. Please check your input.";
    private static final String MARKING_ERROR = "Unable to change task status. Please check the task number.";
    private static final String NO_SUCH_TASK_ERROR = "No such task";
    private TaskList tasks;
    private Storage storage;
    private CommandParser parser;
    
    /**
     * Initializes the Pazuzu application for GUI integration.
     */
    public Pazuzu() {
        storage = new Storage();
        parser = new CommandParser();
        tasks = storage.loadTasks();
    }
    
    /**
     * Processes a command and returns the response message.
     * This method is used for GUI integration.
     * 
     * @param input the command input from user
     * @return the response message to display
     */
    public String processCommand(String input) {
        try {
            if (input.equals(BYE_COMMAND)) {
                return BYE_RESPONSE;
            } else if (input.equals(LIST_COMMAND)) {
                return handleListCommand();
            } else if (input.startsWith(MARK_COMMAND)) {
                return handleMarkCommand(input);
            } else if (input.startsWith(UNMARK_COMMAND)) {
                return handleUnmarkCommand(input);
            } else if (input.startsWith(DELETE_COMMAND)) {
                return handleDeleteCommand(input);
            } else if (input.startsWith(FIND_COMMAND)) {
                return handleFindCommand(input);
            } else if (input.startsWith(TODO_COMMAND) || input.startsWith(DEADLINE_COMMAND) || input.startsWith(EVENT_COMMAND)) {
                return handleTaskCommand(input);
            } else {
                throw new PazuzuExceptions.UndefinedCmdException("Undefined command");
            }
        } catch (PazuzuExceptions.UndefinedCmdException e) {
            return UNDEFINED_COMMAND_ERROR;
        } catch (PazuzuExceptions.BadTaskException e) {
            return BAD_TASK_ERROR;
        } catch (PazuzuExceptions.MarkingException e) {
            return MARKING_ERROR;
        } catch (IndexOutOfBoundsException e) {
            return NO_SUCH_TASK_ERROR;
        } catch (NumberFormatException e) {
            return NO_SUCH_TASK_ERROR;
        }
    }
    
    // Handler methods for GUI integration
    
    /**
     * Handles the list command and returns formatted task list.
     */
    private String handleListCommand() {
        if (tasks.isEmpty()) {
            return NO_TASKS_RESPONSE;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < tasks.getSize(); i++) {
            result.append((i + 1)).append(". ").append(tasks.getTask(i).getTask()).append("\n");
        }
        return result.toString().trim();
    }
    
    /**
     * Handles the mark command and returns confirmation message.
     */
    private String handleMarkCommand(String input) throws PazuzuExceptions.MarkingException, NumberFormatException, IndexOutOfBoundsException {
        int taskNumber = parser.parseTaskNumber(input, CommandParser.MARK_COMMAND_LENGTH);
        Task markedTask = tasks.markTask(taskNumber);
        storage.saveTasks(tasks);
        return TASK_DONE_PREFIX + markedTask.getTask();
    }
    
    /**
     * Handles the unmark command and returns confirmation message.
     */
    private String handleUnmarkCommand(String input) throws PazuzuExceptions.MarkingException, NumberFormatException, IndexOutOfBoundsException {
        int taskNumber = parser.parseTaskNumber(input, CommandParser.UNMARK_COMMAND_LENGTH);
        Task unmarkedTask = tasks.unmarkTask(taskNumber);
        storage.saveTasks(tasks);
        return TASK_NOT_DONE_PREFIX + unmarkedTask.getTask();
    }
    
    /**
     * Handles task creation commands and returns confirmation message.
     */
    private String handleTaskCommand(String input) throws PazuzuExceptions.BadTaskException {
        Task newTask = parser.parseTaskCommand(input);
        tasks.addTask(newTask);
        storage.saveTasks(tasks);
        return TASK_ADDED_PREFIX + newTask.getTask() + TASK_ADDED_SUFFIX + tasks.getSize() + TASK_ADDED_SUFFIX2;
    }
    
    /**
     * Handles the delete command and returns confirmation message.
     */
    private String handleDeleteCommand(String input) throws NumberFormatException, IndexOutOfBoundsException {
        int taskNumber = parser.parseTaskNumber(input, CommandParser.DELETE_COMMAND_LENGTH);
        Task deletedTask = tasks.deleteTask(taskNumber);
        storage.saveTasks(tasks);
        String result = TASK_DELETED_PREFIX + deletedTask.getTask();
        if (!deletedTask.checkIsDone()) {
            result += TASK_NOT_LOCKED_IN;
        }
        return result;
    }
    
    /**
     * Handles the find command and returns search results.
     */
    private String handleFindCommand(String input) throws PazuzuExceptions.BadTaskException {
        String keyword = parser.parseFindCommand(input);
        TaskList matchingTasks = tasks.findTasksContaining(keyword);
        if (matchingTasks.isEmpty()) {
            return "No tasks found containing: " + keyword;
        } else {
            StringBuilder result = new StringBuilder(FOUND_TASKS_PREFIX);
            for (int i = 0; i < matchingTasks.getSize(); i++) {
                result.append((i + 1)).append(". ").append(matchingTasks.getTask(i).getTask()).append("\n");
            }
            return result.toString().trim();
        }
    }
}
