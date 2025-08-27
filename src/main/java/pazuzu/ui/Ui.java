package pazuzu.ui;
import java.util.Scanner;

import pazuzu.task.Task;
import pazuzu.task.TaskList;

/**
 * Handles all user interface interactions and display formatting.
 */
public class Ui {
    private Scanner scanner;
    
    /**
     * Creates a new Ui instance with a Scanner for user input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Displays the welcome message when the program starts.
     */
    public void showWelcome() {
        System.out.println("Pazuzu, what u want?\n");
    }
    
    /**
     * Displays the goodbye message when the program exits.
     */
    public void showGoodbye() {
        System.out.println("Bye.");
    }
    
    /**
     * Reads a command from the user input.
     * 
     * @return the user input string
     */
    public String readCommand() {
        return scanner.nextLine();
    }
    
    /**
     * Displays all tasks in the task list.
     * 
     * @param taskList the TaskList containing all tasks to display
     */
    public void showTaskList(TaskList taskList) {
        for (int i = 0; i < taskList.getSize(); i++) {
            System.out.print((i + 1) + ". ");
            taskList.getTask(i).printTask();
        }
    }
    
    /**
     * Displays a confirmation message when a task is added.
     * 
     * @param task the task that was added
     * @param totalTasks the total number of tasks after adding
     */
    public void showTaskAdded(Task task, int totalTasks) {
        System.out.println("Got it. I've added this task:");
        System.out.print("  ");
        task.printTask();
        System.out.println("Now you have " + totalTasks + " tasks in the list.");
    }
    
    /**
     * Displays a confirmation message when a task is marked as done.
     * 
     * @param task the task that was marked
     */
    public void showTaskMarked(Task task) {
        System.out.println("done:");
        System.out.print("  ");
        task.printTask();
    }
    
    /**
     * Displays a confirmation message when a task is marked as not done.
     * 
     * @param task the task that was unmarked
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("not done:");
        System.out.print("  ");
        task.printTask();
    }
    
    /**
     * Displays a confirmation message when a task is deleted.
     * 
     * @param task the task that was deleted
     */
    public void showTaskDeleted(Task task) {
        System.out.print("Deleted task ");
        task.printTask();
        if (!task.checkIsDone()) {
            System.out.println("Guess ur not locked-in enough for this");
        }
    }
    
    /**
     * Displays an error message for undefined commands.
     */
    public void showUndefinedCommandError() {
        System.out.println("HUHH???!!!");
    }
    
    /**
     * Displays an error message for bad task format.
     */
    public void showBadTaskError() {
        System.out.println("Stupid Task!");
    }
    
    /**
     * Displays an error message for marking operations.
     */
    public void showMarkingError() {
        System.out.println("Can't even keep track of your own task");
    }
    
    /**
     * Displays an error message for invalid task numbers.
     */
    public void showInvalidTaskNumberError() {
        System.out.println("No such task");
    }
    
    /**
     * Displays a generic error message.
     * 
     * @param message the error message to display
     */
    public void showError(String message) {
        System.out.println(message);
    }
    
    /**
     * Displays the separator line between commands.
     */
    public void showLine() {
        System.out.println("===========");
    }
    
    /**
     * Closes the Scanner when the program exits.
     */
    public void close() {
        scanner.close();
    }
}
