import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Pazuzu {
    private static ArrayList<Task> items = new ArrayList<>();
    
    public static void main(String[] args) {
        initTasks();
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Pazuzu, what u want?\n");
        
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
                } else if (input.startsWith("delete ")) {
                    handleDeleteCommand(input);
                } else if (input.startsWith("todo") || input.startsWith("deadline") || input.startsWith("event")) {
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
        for (int i = 0; i < items.size(); i++) {
            System.out.print((i + 1) + ". ");
            items.get(i).printTask();
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
            if (taskNumber >= 1 && taskNumber <= items.size()) {
                if (items.get(taskNumber - 1).checkIsDone()) {
                    throw new PazuzuExceptions.MarkingException("Task already done");
                }
                items.get(taskNumber - 1).markDone();
                System.out.println("done:");
                System.out.print("  ");
                items.get(taskNumber - 1).printTask();
                saveTaskUpdates();
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
            if (taskNumber >= 1 && taskNumber <= items.size()) {
                if (!items.get(taskNumber - 1).checkIsDone()) {
                    throw new PazuzuExceptions.MarkingException("Task already not done");
                }
                items.get(taskNumber - 1).markNotDone();
                System.out.println("not done:");
                System.out.print("  ");
                items.get(taskNumber - 1).printTask();
                saveTaskUpdates();
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
            items.get(items.size() - 1).printTask();
            System.out.println("Now you have " + items.size() + " tasks in the list.");
            saveTaskUpdates();
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
            items.get(items.size() - 1).printTask();
            System.out.println("Now you have " + items.size() + " tasks in the list.");
            saveTaskUpdates();
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
            items.get(items.size() - 1).printTask();
            System.out.println("Now you have " + items.size() + " tasks in the list.");
            saveTaskUpdates();
        }
    }
    
    /**
     * Adds a task to the list.
     * The task is stored in the internal ArrayList.
     * 
     * @param task the Task object to be added to the list
     */
    private static void addToList(Task task) {
        items.add(task);
    }
    
    /**
     * Handles the delete command to remove a task from the list.
     * Parses the task number from the input and removes the corresponding task.
     * If the task number is out of range, prints "No such task".
     * 
     * @param input the full input string starting with "delete "
     */
    private static void handleDeleteCommand(String input) {
        try {
            int taskNumber = Integer.parseInt(input.substring(7).trim());
            if (taskNumber >= 1 && taskNumber <= items.size()) {
                deleteTask(taskNumber);
            } else {
                System.out.println("No such task");
            }
        } catch (NumberFormatException e) {
            System.out.println("No such task");
        }
    }
    
    /**
     * Deletes a task from the list at the given 1-indexed position.
     * Prints the deleted task information and a message.
     * 
     * @param taskNumber the 1-indexed position of the task to delete
     */
    private static void deleteTask(int taskNumber) {
        Task deletedTask = items.get(taskNumber - 1);
        items.remove(taskNumber - 1);
        System.out.print("Deleted task ");
        deletedTask.printTask();
        if (!deletedTask.checkIsDone()) {
            System.out.println("Guess ur not locked-in enough for this");
        }
        saveTaskUpdates();
    }
    
    /**
     * Saves the current task list to ./data/pazuzu.txt file.
     * Creates the data directory if it doesn't exist.
     * Each task is saved in the format returned by getTask() method.
     */
    private static void saveTaskUpdates() {
        try {
            // Create data directory if it doesn't exist
            File dataDir = new File("./data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            
            // Create or overwrite the file
            File file = new File("./data/pazuzu.txt");
            FileWriter writer = new FileWriter(file);
            
            // Write each task using getTask() method
            for (int i = 0; i < items.size(); i++) {
                writer.write((i + 1) + ". " + items.get(i).getTask() + "\n");
            }
            
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving tasks to file: " + e.getMessage());
        }
    }
    
    /**
     * Initializes the task list by loading tasks from ./data/pazuzu.txt file.
     * Parses each line to create appropriate Task, Deadline, or Event objects.
     * If the file doesn't exist, starts with an empty task list.
     */
    private static void initTasks() {
        try {
            File file = new File("./data/pazuzu.txt");
            if (!file.exists()) {
                return; // Start with empty list if file doesn't exist
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                
                // Parse line format: "1. [T][X] task name" or "1. [D][ ] task name (by: deadline)"
                int dotIndex = line.indexOf(". ");
                if (dotIndex == -1) {
                    continue; // Skip malformed lines
                }
                
                String taskData = line.substring(dotIndex + 2); // Skip "1. " part
                Task task = parseTaskFromString(taskData);
                if (task != null) {
                    items.add(task);
                }
            }
            
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading tasks from file: " + e.getMessage());
        }
    }
    
    /**
     * Parses a task string and creates the appropriate Task, Deadline, or Event object.
     * 
     * @param taskData the task string in format "[T][X] name" or "[D][ ] name (by: deadline)"
     * @return the parsed Task object, or null if parsing fails
     */
    private static Task parseTaskFromString(String taskData) {
        if (taskData.length() < 6) {
            return null; // Too short to be valid
        }
        
        // Extract task type and completion status
        char taskType = taskData.charAt(1); // T, D, or E
        boolean isDone = taskData.charAt(4) == 'X';
        String content = taskData.substring(6); // Skip "[T][X] " part
        
        Task task = null;
        
        if (taskType == 'T') {
            // Todo task: "[T][X] task name"
            task = new Task(content);
        } else if (taskType == 'D') {
            // Deadline task: "[D][X] task name (by: deadline)"
            int byIndex = content.lastIndexOf(" (by: ");
            if (byIndex != -1 && content.endsWith(")")) {
                String taskName = content.substring(0, byIndex);
                String deadline = content.substring(byIndex + 6, content.length() - 1);
                task = new Deadline(taskName, deadline);
            }
        } else if (taskType == 'E') {
            // Event task: "[E][X] task name (from: start to: end)"
            int fromIndex = content.lastIndexOf(" (from: ");
            int toIndex = content.lastIndexOf(" to: ");
            if (fromIndex != -1 && toIndex != -1 && content.endsWith(")")) {
                String taskName = content.substring(0, fromIndex);
                String startDate = content.substring(fromIndex + 8, toIndex);
                String endDate = content.substring(toIndex + 5, content.length() - 1);
                task = new Event(taskName, startDate, endDate);
            }
        }
        
        // Set completion status
        if (task != null && isDone) {
            task.markDone();
        }
        
        return task;
    }
}
