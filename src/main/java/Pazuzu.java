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
                handleTaskInput(input);
            }
            
            System.out.println("===========");
        }
        
        scanner.close();
    }
    
    /**
     * Prints all tasks currently stored in the list.
     * Each task is displayed with a numbered format starting from 1, 
     * using the task's printTask() method to show appropriate format based on task type.
     * If the list is empty, nothing will be printed.
     */
    private static void printList() {
        for (int i = 0; i < itemCount; i++) {
            System.out.print((i + 1) + ". ");
            items[i].printTask();
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
                System.out.print("  ");
                items[taskNumber - 1].printTask();
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
                System.out.print("  ");
                items[taskNumber - 1].printTask();
            } else {
                System.out.println("No such task");
            }
        } catch (NumberFormatException e) {
            System.out.println("No such task");
        }
    }
    
    /**
     * Handles task input parsing and creation.
     * Parses different task types based on input format:
     * - "todo <name>" creates a basic Task
     * - "deadline <name> /<deadline>" creates a Deadline task
     * - "event <name> /<startDate> /<endDate>" creates an Event task
     * 
     * @param input the full input string from the user
     */
    private static void handleTaskInput(String input) {
        if (input.startsWith("todo ")) {
            String taskName = input.substring(5).trim();
            addToList(new Task(taskName));
            System.out.println("Got it. I've added this task:");
            System.out.print("  ");
            items[itemCount - 1].printTask();
            System.out.println("Now you have " + itemCount + " tasks in the list.");
        } else if (input.startsWith("deadline ")) {
            String remaining = input.substring(9).trim();
            int slashIndex = remaining.lastIndexOf('/');
            if (slashIndex != -1 && slashIndex < remaining.length() - 1) {
                String taskName = remaining.substring(0, slashIndex).trim();
                String deadline = remaining.substring(slashIndex + 1).trim();
                addToList(new Deadline(taskName, deadline));
                System.out.println("Got it. I've added this task:");
                System.out.print("  ");
                items[itemCount - 1].printTask();
                System.out.println("Now you have " + itemCount + " tasks in the list.");
            } else {
                System.out.println("Please provide deadline in format: deadline <name> /<deadline>");
            }
        } else if (input.startsWith("event ")) {
            String remaining = input.substring(6).trim();
            String[] parts = remaining.split("/");
            if (parts.length == 3) {
                String taskName = parts[0].trim();
                String startDate = parts[1].trim();
                String endDate = parts[2].trim();
                addToList(new Event(taskName, startDate, endDate));
                System.out.println("Got it. I've added this task:");
                System.out.print("  ");
                items[itemCount - 1].printTask();
                System.out.println("Now you have " + itemCount + " tasks in the list.");
            } else {
                System.out.println("Please provide event in format: event <name> /<startDate> /<endDate>");
            }
        } else {
            // Default behavior for backwards compatibility
            addToList(new Task(input));
            System.out.println("added: " + input);
        }
    }
    
    /**
     * Adds a task to the list if there is space available.
     * The task is stored in the internal array and the item count is incremented.
     * If the array is full (100 items), the task will not be added.
     * 
     * @param task the Task object to be added to the list
     */
    private static void addToList(Task task) {
        if (itemCount < 100) {
            items[itemCount] = task;
            itemCount++;
        }
    }
}
