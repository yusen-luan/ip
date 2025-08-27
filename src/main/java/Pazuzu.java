/**
 * Main controller class that coordinates between different components 
 * to provide a task management system.
 */
public class Pazuzu {
    private TaskList tasks;
    private Ui ui;
    private Storage storage;
    private Parser parser;
    
    /**
     * Initializes the Pazuzu application with all necessary components.
     */
    public Pazuzu() {
        ui = new Ui();
        storage = new Storage();
        parser = new Parser();
        tasks = storage.loadTasks();
    }
    
    /**
     * Runs the main application loop.
     */
    public void run() {
        ui.showWelcome();
        
        while (true) {
            String input = ui.readCommand();
            
            try {
                if (input.equals("bye")) {
                    ui.showGoodbye();
                    break;
                } else if (input.equals("List")) {
                    handleListCommand();
                } else if (input.startsWith("mark ")) {
                    handleMarkCommand(input);
                } else if (input.startsWith("unmark ")) {
                    handleUnmarkCommand(input);
                } else if (input.startsWith("delete ")) {
                    handleDeleteCommand(input);
                } else if (input.startsWith("todo") || input.startsWith("deadline") || input.startsWith("event")) {
                    handleTaskCommand(input);
                } else {
                    throw new PazuzuExceptions.UndefinedCmdException("Undefined command");
                }
            } catch (PazuzuExceptions.UndefinedCmdException e) {
                ui.showUndefinedCommandError();
            } catch (PazuzuExceptions.BadTaskException e) {
                ui.showBadTaskError();
            } catch (PazuzuExceptions.MarkingException e) {
                ui.showMarkingError();
            } catch (IndexOutOfBoundsException e) {
                ui.showInvalidTaskNumberError();
            } catch (NumberFormatException e) {
                ui.showInvalidTaskNumberError();
            }
            
            ui.showLine();
        }
        
        ui.close();
    }
    
    public static void main(String[] args) {
        new Pazuzu().run();
    }
    
    /**
     * Handles the list command to display all tasks.
     */
    private void handleListCommand() {
        ui.showTaskList(tasks);
    }
    
    /**
     * Handles the mark command to mark a task as done.
     * 
     * @param input the full input string starting with "mark "
     */
    private void handleMarkCommand(String input) throws PazuzuExceptions.MarkingException, NumberFormatException, IndexOutOfBoundsException {
        int taskNumber = parser.parseTaskNumber(input, 5);
        Task markedTask = tasks.markTask(taskNumber);
        ui.showTaskMarked(markedTask);
        storage.saveTasks(tasks);
    }
    
    /**
     * Handles the unmark command to mark a task as not done.
     * 
     * @param input the full input string starting with "unmark "
     */
    private void handleUnmarkCommand(String input) throws PazuzuExceptions.MarkingException, NumberFormatException, IndexOutOfBoundsException {
        int taskNumber = parser.parseTaskNumber(input, 7);
        Task unmarkedTask = tasks.unmarkTask(taskNumber);
        ui.showTaskUnmarked(unmarkedTask);
        storage.saveTasks(tasks);
    }
    
    /**
     * Handles task creation commands.
     * 
     * @param input the full input string from the user
     */
    private void handleTaskCommand(String input) throws PazuzuExceptions.BadTaskException {
        Task newTask = parser.parseTaskCommand(input);
        tasks.addTask(newTask);
        ui.showTaskAdded(newTask, tasks.getSize());
        storage.saveTasks(tasks);
    }
    
    /**
     * Handles the delete command to remove a task from the list.
     * 
     * @param input the full input string starting with "delete "
     */
    private void handleDeleteCommand(String input) throws NumberFormatException, IndexOutOfBoundsException {
        int taskNumber = parser.parseTaskNumber(input, 7);
        Task deletedTask = tasks.deleteTask(taskNumber);
        ui.showTaskDeleted(deletedTask);
        storage.saveTasks(tasks);
    }
}
