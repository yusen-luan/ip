package pazuzu;

import pazuzu.exception.PazuzuExceptions;
import pazuzu.parser.Parser;
import pazuzu.storage.Storage;
import pazuzu.task.Task;
import pazuzu.task.TaskList;
/**
 * Main controller class that coordinates between different components 
 * to provide a task management system.
 */
public class Pazuzu {
    private TaskList tasks;
    private Storage storage;
    private Parser parser;
    
    /**
     * Initializes the Pazuzu application for GUI integration.
     */
    public Pazuzu() {
        storage = new Storage();
        parser = new Parser();
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
            if (input.equals("bye")) {
                return "Bye.";
            } else if (input.equals("List")) {
                return handleListCommand();
            } else if (input.startsWith("mark ")) {
                return handleMarkCommand(input);
            } else if (input.startsWith("unmark ")) {
                return handleUnmarkCommand(input);
            } else if (input.startsWith("delete ")) {
                return handleDeleteCommand(input);
            } else if (input.startsWith("find ")) {
                return handleFindCommand(input);
            } else if (input.startsWith("todo") || input.startsWith("deadline") || input.startsWith("event")) {
                return handleTaskCommand(input);
            } else {
                throw new PazuzuExceptions.UndefinedCmdException("Undefined command");
            }
        } catch (PazuzuExceptions.UndefinedCmdException e) {
            return "HUHH???!!!";
        } catch (PazuzuExceptions.BadTaskException e) {
            return "Stupid Task!";
        } catch (PazuzuExceptions.MarkingException e) {
            return "Can't even keep track of your own task";
        } catch (IndexOutOfBoundsException e) {
            return "No such task";
        } catch (NumberFormatException e) {
            return "No such task";
        }
    }
    
    // Handler methods for GUI integration
    
    /**
     * Handles the list command and returns formatted task list.
     */
    private String handleListCommand() {
        if (tasks.isEmpty()) {
            return "No tasks in your list.";
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
        int taskNumber = parser.parseTaskNumber(input, 5);
        Task markedTask = tasks.markTask(taskNumber);
        storage.saveTasks(tasks);
        return "done:\n  " + markedTask.getTask();
    }
    
    /**
     * Handles the unmark command and returns confirmation message.
     */
    private String handleUnmarkCommand(String input) throws PazuzuExceptions.MarkingException, NumberFormatException, IndexOutOfBoundsException {
        int taskNumber = parser.parseTaskNumber(input, 7);
        Task unmarkedTask = tasks.unmarkTask(taskNumber);
        storage.saveTasks(tasks);
        return "not done:\n  " + unmarkedTask.getTask();
    }
    
    /**
     * Handles task creation commands and returns confirmation message.
     */
    private String handleTaskCommand(String input) throws PazuzuExceptions.BadTaskException {
        Task newTask = parser.parseTaskCommand(input);
        tasks.addTask(newTask);
        storage.saveTasks(tasks);
        return "Got it. I've added this task:\n  " + newTask.getTask() + "\nNow you have " + tasks.getSize() + " tasks in the list.";
    }
    
    /**
     * Handles the delete command and returns confirmation message.
     */
    private String handleDeleteCommand(String input) throws NumberFormatException, IndexOutOfBoundsException {
        int taskNumber = parser.parseTaskNumber(input, 7);
        Task deletedTask = tasks.deleteTask(taskNumber);
        storage.saveTasks(tasks);
        String result = "Deleted task " + deletedTask.getTask();
        if (!deletedTask.checkIsDone()) {
            result += "\nGuess ur not locked-in enough for this";
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
            return "where got " + keyword + "bro?";
        } else {
            StringBuilder result = new StringBuilder("Found:\n");
            for (int i = 0; i < matchingTasks.getSize(); i++) {
                result.append((i + 1)).append(". ").append(matchingTasks.getTask(i).getTask()).append("\n");
            }
            return result.toString().trim();
        }
    }
}
