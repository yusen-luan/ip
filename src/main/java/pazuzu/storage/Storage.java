package pazuzu.storage;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import pazuzu.task.Deadline;
import pazuzu.task.Event;
import pazuzu.task.Task;
import pazuzu.task.TaskList;

/**
 * Handles the loading and saving of tasks to the storage file.
 */
public class Storage {
    private static final String FILE_PATH = "./data/pazuzu.txt";
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");
    
    /**
     * Saves the current task list to the storage file.
     * Creates the data directory if it doesn't exist.
     * 
     * @param taskList the TaskList containing all tasks to save
     */
    public void saveTasks(TaskList taskList) {
        try {
            // Create data directory if it doesn't exist
            File dataDir = new File("./data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            
            // Create or overwrite the file
            File file = new File(FILE_PATH);
            FileWriter writer = new FileWriter(file);
            
            // Write each task using getTask() method
            for (int i = 0; i < taskList.getSize(); i++) {
                writer.write((i + 1) + ". " + taskList.getTask(i).getTask() + "\n");
            }
            
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving tasks to file: " + e.getMessage());
        }
    }
    
    /**
     * Loads tasks from the storage file and returns a populated TaskList.
     * If the file doesn't exist, returns an empty TaskList.
     * 
     * @return TaskList containing all loaded tasks
     */
    public TaskList loadTasks() {
        TaskList taskList = new TaskList();
        
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                return taskList; // Return empty list if file doesn't exist
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
                    taskList.addTask(task);
                }
            }
            
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading tasks from file: " + e.getMessage());
        }
        
        return taskList;
    }
    
    /**
     * Parses a task string and creates the appropriate Task, Deadline, or Event object.
     * 
     * @param taskData the task string in format "[T][X] name" or "[D][ ] name (by: deadline)"
     * @return the parsed Task object, or null if parsing fails
     */
    private Task parseTaskFromString(String taskData) {
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
                String deadlineStr = content.substring(byIndex + 6, content.length() - 1);
                try {
                    // Parse the formatted date back to LocalDateTime
                    LocalDateTime deadline = LocalDateTime.parse(deadlineStr, OUTPUT_FORMATTER);
                    task = new Deadline(taskName, deadline);
                } catch (Exception e) {
                    // If parsing fails, skip this task
                    return null;
                }
            }
        } else if (taskType == 'E') {
            // Event task: "[E][X] task name (from: start to: end)"
            int fromIndex = content.lastIndexOf(" (from: ");
            int toIndex = content.lastIndexOf(" to: ");
            if (fromIndex != -1 && toIndex != -1 && content.endsWith(")")) {
                String taskName = content.substring(0, fromIndex);
                String startDateStr = content.substring(fromIndex + 8, toIndex);
                String endDateStr = content.substring(toIndex + 5, content.length() - 1);
                try {
                    // Parse the formatted dates back to LocalDateTime
                    LocalDateTime startDate = LocalDateTime.parse(startDateStr, OUTPUT_FORMATTER);
                    LocalDateTime endDate = LocalDateTime.parse(endDateStr, OUTPUT_FORMATTER);
                    task = new Event(taskName, startDate, endDate);
                } catch (Exception e) {
                    // If parsing fails, skip this task
                    return null;
                }
            }
        }
        
        // Set completion status
        if (task != null && isDone) {
            task.markDone();
        }
        
        return task;
    }
}
