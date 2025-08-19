import java.util.Scanner;

public class Pazuzu {
    private static Task[] items = new Task[100];
    private static int itemCount = 0;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Hello! I'm Pazuzu!\nWhat can I do for you?\n");
        
        while (true) {
            String input = scanner.nextLine();
            
            if (input.equals("bye")) {
                System.out.println("Bye.");
                break;
            } else if (input.equals("List")) {
                printList();
            } else if (input.startsWith("mark ")) {
                handleMarkCommand(input);
            } else if (input.startsWith("unmark ")) {
                handleUnmarkCommand(input);
            } else {
                addToList(input);
                System.out.println("added: " + input);
            }
            
            System.out.println("===========");
        }
        
        scanner.close();
    }
    
    /**
     * Adds a new task to the list if there is space available.
     * Creates a new Task object with the given name and stores it in the internal array.
     * The item count is incremented. If the array is full (100 items), the task will not be added.
     * 
     * @param taskName the name of the task to be added to the list
     */
    private static void addToList(String taskName) {
        if (itemCount < 100) {
            items[itemCount] = new Task(taskName);
            itemCount++;
        }
    }
    
    /**
     * Prints all tasks currently stored in the list.
     * Each task is displayed with a numbered format starting from 1, 
     * with completion status shown as [ ] for not done or [X] for done.
     * If the list is empty, nothing will be printed.
     */
    private static void printList() {
        for (int i = 0; i < itemCount; i++) {
            String status = items[i].checkIsDone() ? "[X]" : "[ ]";
            System.out.println((i + 1) + ". " + status + " " + items[i].getName());
        }
    }
    
    /**
     * Handles the mark command to mark a task as done.
     * Parses the task number from the input and marks the corresponding task as done.
     * If the task number is out of range, prints "No such task".
     * 
     * @param input the full input string starting with "mark "
     */
    private static void handleMarkCommand(String input) {
        try {
            int taskNumber = Integer.parseInt(input.substring(5).trim());
            if (taskNumber >= 1 && taskNumber <= itemCount) {
                items[taskNumber - 1].markDone();
                System.out.println("Nice! I've marked this task as done:");
                String status = items[taskNumber - 1].checkIsDone() ? "[X]" : "[ ]";
                System.out.println("  " + status + " " + items[taskNumber - 1].getName());
            } else {
                System.out.println("No such task");
            }
        } catch (NumberFormatException e) {
            System.out.println("No such task");
        }
    }
    
    /**
     * Handles the unmark command to mark a task as not done.
     * Parses the task number from the input and marks the corresponding task as not done.
     * If the task number is out of range, prints "No such task".
     * 
     * @param input the full input string starting with "unmark "
     */
    private static void handleUnmarkCommand(String input) {
        try {
            int taskNumber = Integer.parseInt(input.substring(7).trim());
            if (taskNumber >= 1 && taskNumber <= itemCount) {
                items[taskNumber - 1].markNotDone();
                System.out.println("OK, I've marked this task as not done yet:");
                String status = items[taskNumber - 1].checkIsDone() ? "[X]" : "[ ]";
                System.out.println("  " + status + " " + items[taskNumber - 1].getName());
            } else {
                System.out.println("No such task");
            }
        } catch (NumberFormatException e) {
            System.out.println("No such task");
        }
    }
}
