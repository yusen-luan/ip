package pazuzu.storage;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import pazuzu.task.Deadline;
import pazuzu.task.Event;
import pazuzu.task.Task;
import pazuzu.task.TaskList;
import pazuzu.util.DateTimeUtil;

/**
 * Handles the loading and saving of tasks to the storage file.
 */
public class Storage {
    private static final String FILE_PATH = "./data/pazuzu.txt";
    
    /**
     * Saves the current task list to the storage file.
     * Creates the data directory if it doesn't exist.
     * 
     * @param taskList the TaskList containing all tasks to save
     */
    public void saveTasks(TaskList taskList) {
        // Create data directory if it doesn't exist
        File dataDir = new File("./data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        
        try (FileWriter writer = new FileWriter(new File(FILE_PATH))) {
            // Write each task using getTask() method
            for (int i = 0; i < taskList.getSize(); i++) {
                writer.write((i + 1) + ". " + taskList.getTask(i).getTask() + "\n");
            }
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
        
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return taskList; // Return empty list if file doesn't exist
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
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
        if (!isValidTaskData(taskData)) {
            return null;
        }
        
        TaskMetadata metadata = extractTaskMetadata(taskData);
        String content = extractTaskContent(taskData);
        
        Task task = createTaskByType(metadata.taskType, content);
        if (task != null && metadata.isDone) {
            task.markDone();
        }
        
        return task;
    }
    
    /**
     * Validates that the task data string has the correct format and minimum length.
     * 
     * @param taskData the task string to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidTaskData(String taskData) {
        return taskData.length() >= 6 &&
               taskData.charAt(0) == '[' &&
               taskData.charAt(2) == ']' &&
               taskData.charAt(3) == '[' &&
               taskData.charAt(5) == ']';
    }
    
    /**
     * Extracts task metadata (type and completion status) from the task data string.
     * 
     * @param taskData the task string to parse
     * @return TaskMetadata containing task type and completion status
     */
    private TaskMetadata extractTaskMetadata(String taskData) {
        assert taskData.charAt(0) == '[' : "Task data must start with '['";
        char taskType = taskData.charAt(1); // T, D, or E
        assert taskData.charAt(2) == ']' : "Task type must be enclosed in brackets";
        assert taskData.charAt(3) == '[' : "Status indicator must start with '['";
        boolean isDone = taskData.charAt(4) == 'X';
        assert taskData.charAt(5) == ']' : "Status indicator must end with ']'";
        
        return new TaskMetadata(taskType, isDone);
    }
    
    /**
     * Extracts the task content from the task data string.
     * 
     * @param taskData the task string to parse
     * @return the task content with leading spaces trimmed
     */
    private String extractTaskContent(String taskData) {
        return taskData.substring(6).trim(); // Skip "[T][X] " part and trim leading spaces
    }
    
    /**
     * Creates a task object based on the task type and content.
     * 
     * @param taskType the type of task ('T', 'D', or 'E')
     * @param content the task content string
     * @return the created Task object, or null if parsing fails
     */
    private Task createTaskByType(char taskType, String content) {
        switch (taskType) {
            case 'T':
                return createTodoTask(content);
            case 'D':
                return createDeadlineTask(content);
            case 'E':
                return createEventTask(content);
            default:
                return null;
        }
    }
    
    /**
     * Creates a Todo task from the given content.
     * 
     * @param content the task content
     * @return the created Task object
     */
    private Task createTodoTask(String content) {
        return new Task(content);
    }
    
    /**
     * Creates a Deadline task from the given content.
     * 
     * @param content the task content in format "task name (by: deadline)"
     * @return the created Deadline object, or null if parsing fails
     */
    private Task createDeadlineTask(String content) {
        int byIndex = content.lastIndexOf(" (by: ");
        if (byIndex == -1 || !content.endsWith(")")) {
            return null;
        }
        
        assert byIndex > 0 : "Task name must exist before deadline";
        assert content.length() > byIndex + 7 : "Deadline string must have content";
        
        String taskName = content.substring(0, byIndex);
        String deadlineStr = content.substring(byIndex + 6, content.length() - 1);
        
        try {
            LocalDateTime deadline = LocalDateTime.parse(deadlineStr, DateTimeUtil.OUTPUT_FORMATTER);
            return new Deadline(taskName, deadline);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    /**
     * Creates an Event task from the given content.
     * 
     * @param content the task content in format "task name (from: start to: end)"
     * @return the created Event object, or null if parsing fails
     */
    private Task createEventTask(String content) {
        int fromIndex = content.lastIndexOf(" (from: ");
        int toIndex = content.lastIndexOf(" to: ");
        
        if (fromIndex == -1 || toIndex == -1 || !content.endsWith(")")) {
            return null;
        }
        
        String taskName = content.substring(0, fromIndex);
        String startDateStr = content.substring(fromIndex + 8, toIndex);
        String endDateStr = content.substring(toIndex + 5, content.length() - 1);
        
        try {
            LocalDateTime startDate = LocalDateTime.parse(startDateStr, DateTimeUtil.OUTPUT_FORMATTER);
            LocalDateTime endDate = LocalDateTime.parse(endDateStr, DateTimeUtil.OUTPUT_FORMATTER);
            return new Event(taskName, startDate, endDate);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    /**
     * Helper class to hold task metadata extracted from the task data string.
     */
    private static class TaskMetadata {
        final char taskType;
        final boolean isDone;
        
        TaskMetadata(char taskType, boolean isDone) {
            this.taskType = taskType;
            this.isDone = isDone;
        }
    }
}
