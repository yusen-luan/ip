import java.util.Scanner;

public class Pazuzu {
    private static Task[] items = new Task[100];
    private static int itemCount = 0;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Hello! I'm Pazuzu!\nWhat can I do for you?\n");
        
        while (true) {
            String input = scanner.nextLine();
            
            try {
                if (input.equals("bye")) {
                    System.out.println("Bye.");
                    break;
                } else if (input.equals("List")) {
                    printList();
                } else if (input.startsWith("mark ")) {
                    handleMarkCommand(input);
                } else if (input.startsWith("unmark ")) {
                    handleUnmarkCommand(input);
                } else if (input.startsWith("todo") || input.startsWith("deadline ") || input.startsWith("event ")) {
                    handleTaskInput(input);
                } else {
                    throw new PazuzuExceptions.UndefinedCmdException("Undefined command");
                }
            } catch (PazuzuExceptions.UndefinedCmdException e) {
                System.out.println("HUHH???!!!");
            } catch (PazuzuExceptions.BadTaskException e) {
                System.out.println("Stupid Task!");
            } catch (PazuzuExceptions.MarkingException e) {
                System.out.println("Can't even keep track of your own task");
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
     * If the task is already done, throws MarkingException.
     * 
     * @param input the full input string starting with "mark "
     * @throws PazuzuExceptions.MarkingException when trying to mark an already done task
     */
    private static void handleMarkCommand(String input) throws PazuzuExceptions.MarkingException {
        try {
            int taskNumber = Integer.parseInt(input.substring(5).trim());
            if (taskNumber >= 1 && taskNumber <= itemCount) {
                if (items[taskNumber - 1].checkIsDone()) {
                    throw new PazuzuExceptions.MarkingException("Task already done");
                }
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
     * If the task is already not done, throws MarkingException.
     * 
     * @param input the full input string starting with "unmark "
     * @throws PazuzuExceptions.MarkingException when trying to unmark an already undone task
     */
    private static void handleUnmarkCommand(String input) throws PazuzuExceptions.MarkingException {
        try {
            int taskNumber = Integer.parseInt(input.substring(7).trim());
            if (taskNumber >= 1 && taskNumber <= itemCount) {
                if (!items[taskNumber - 1].checkIsDone()) {
                    throw new PazuzuExceptions.MarkingException("Task already not done");
                }
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
     * @throws PazuzuExceptions.BadTaskException when task format is invalid
     */
    private static void handleTaskInput(String input) throws PazuzuExceptions.BadTaskException {
        if (input.startsWith("todo")) {
            if (input.length() <= 4 || !input.substring(4, 5).equals(" ")) {
                throw new PazuzuExceptions.BadTaskException("Invalid todo format");
            }
            String taskName = input.substring(5).trim();
            if (taskName.isEmpty()) {
                throw new PazuzuExceptions.BadTaskException("Empty task name");
            }
            addToList(new Task(taskName));
            System.out.println("Got it. I've added this task:");
            System.out.print("  ");
            items[itemCount - 1].printTask();
            System.out.println("Now you have " + itemCount + " tasks in the list.");
        } else if (input.startsWith("deadline")) {
            if (input.length() <= 8 || !input.substring(8, 9).equals(" ")) {
                throw new PazuzuExceptions.BadTaskException("Invalid deadline format");
            }
            String remaining = input.substring(9).trim();
            int slashIndex = remaining.lastIndexOf('/');
            if (slashIndex == -1 || slashIndex >= remaining.length() - 1) {
                throw new PazuzuExceptions.BadTaskException("Invalid deadline format");
            }
            String taskName = remaining.substring(0, slashIndex).trim();
            String deadline = remaining.substring(slashIndex + 1).trim();
            if (taskName.isEmpty() || deadline.isEmpty()) {
                throw new PazuzuExceptions.BadTaskException("Empty task name or deadline");
            }
            addToList(new Deadline(taskName, deadline));
            System.out.println("Got it. I've added this task:");
            System.out.print("  ");
            items[itemCount - 1].printTask();
            System.out.println("Now you have " + itemCount + " tasks in the list.");
        } else if (input.startsWith("event")) {
            if (input.length() <= 5 || !input.substring(5, 6).equals(" ")) {
                throw new PazuzuExceptions.BadTaskException("Invalid event format");
            }
            String remaining = input.substring(6).trim();
            String[] parts = remaining.split("/");
            if (parts.length != 3) {
                throw new PazuzuExceptions.BadTaskException("Invalid event format");
            }
            String taskName = parts[0].trim();
            String startDate = parts[1].trim();
            String endDate = parts[2].trim();
            if (taskName.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                throw new PazuzuExceptions.BadTaskException("Empty task name, start date, or end date");
            }
            addToList(new Event(taskName, startDate, endDate));
            System.out.println("Got it. I've added this task:");
            System.out.print("  ");
            items[itemCount - 1].printTask();
            System.out.println("Now you have " + itemCount + " tasks in the list.");
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
