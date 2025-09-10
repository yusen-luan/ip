package pazuzu.task;
import java.util.ArrayList;
import java.time.LocalDateTime;

import pazuzu.exception.PazuzuExceptions;

/**
 * Manages a list of tasks and provides operations to manipulate them.
 */
public class TaskList {
    private ArrayList<Task> tasks;
    
    /**
     * Creates a new empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }
    
    /**
     * Creates a TaskList with the given list of tasks.
     * 
     * @param tasks the list of tasks to initialize with
     */
    public TaskList(ArrayList<Task> tasks) {
        assert tasks != null : "Task list cannot be null";
        this.tasks = tasks;
    }
    
    /**
     * Adds a task to the list.
     * 
     * @param task the Task object to be added to the list
     */
    public void addTask(Task task) {
        assert task != null : "Cannot add null task to list";
        tasks.add(task);
    }
    
    /**
     * Removes a task from the list at the given 1-indexed position.
     * 
     * @param taskNumber the 1-indexed position of the task to delete
     * @return the deleted Task object
     * @throws IndexOutOfBoundsException if the task number is invalid
     */
    public Task deleteTask(int taskNumber) throws IndexOutOfBoundsException {
        validateTaskNumber(taskNumber);
        return tasks.remove(taskNumber - 1);
    }
    
    /**
     * Marks a task as done.
     * 
     * @param taskNumber the 1-indexed position of the task to mark
     * @return the marked Task object
     * @throws IndexOutOfBoundsException if the task number is invalid
     * @throws PazuzuExceptions.MarkingException if the task is already done
     */
    public Task markTask(int taskNumber) throws IndexOutOfBoundsException, PazuzuExceptions.MarkingException {
        validateTaskNumber(taskNumber);
        Task task = tasks.get(taskNumber - 1);
        if (task.checkIsDone()) {
            throw new PazuzuExceptions.MarkingException("Task already done");
        }
        task.markDone();
        return task;
    }
    
    /**
     * Marks a task as not done.
     * 
     * @param taskNumber the 1-indexed position of the task to unmark
     * @return the unmarked Task object
     * @throws IndexOutOfBoundsException if the task number is invalid
     * @throws PazuzuExceptions.MarkingException if the task is already not done
     */
    public Task unmarkTask(int taskNumber) throws IndexOutOfBoundsException, PazuzuExceptions.MarkingException {
        validateTaskNumber(taskNumber);
        Task task = tasks.get(taskNumber - 1);
        if (!task.checkIsDone()) {
            throw new PazuzuExceptions.MarkingException("Task already not done");
        }
        task.markNotDone();
        return task;
    }
    
    /**
     * Gets a task by its 1-indexed position.
     * 
     * @param taskNumber the 1-indexed position of the task
     * @return the Task object
     * @throws IndexOutOfBoundsException if the task number is invalid
     */
    public Task getTaskByNumber(int taskNumber) throws IndexOutOfBoundsException {
        validateTaskNumber(taskNumber);
        return tasks.get(taskNumber - 1);
    }
    
    /**
     * Gets a task by its 0-indexed position (for internal use like Storage).
     * 
     * @param index the 0-indexed position of the task
     * @return the Task object
     */
    public Task getTask(int index) {
        assert index >= 0 && index < tasks.size() : "Index must be valid for direct ArrayList access";
        return tasks.get(index);
    }
    
    /**
     * Gets the number of tasks in the list.
     * 
     * @return the number of tasks
     */
    public int getSize() {
        return tasks.size();
    }
    
    /**
     * Checks if the task list is empty.
     * 
     * @return true if the list is empty, false otherwise
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }
    
    /**
     * Gets all tasks in the list.
     * 
     * @return the ArrayList containing all tasks
     */
    public ArrayList<Task> getAllTasks() {
        return tasks;
    }
    
    /**
     * Finds all tasks whose names contain the specified keyword (case-insensitive).
     * 
     * @param keyword the keyword to search for
     * @return a new TaskList containing all matching tasks
     */
    public TaskList findTasksContaining(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        
        for (Task task : tasks) {
            if (task.getName().toLowerCase().contains(lowerKeyword)) {
                matchingTasks.add(task);
            }
        }
        
        return new TaskList(matchingTasks);
    }
    
    /**
     * Edits a task based on the provided parameters.
     * 
     * @param taskNumber the 1-indexed position of the task to edit
     * @param newName the new name (use "_" to keep current name)
     * @param newDate1 the new first date (deadline for Deadline, start date for Event, "_" for Todo)
     * @param newDate2 the new second date (end date for Event, "_" for others)
     * @return the edited Task object
     * @throws IndexOutOfBoundsException if the task number is invalid
     * @throws PazuzuExceptions.BadTaskException if the edit parameters are invalid for the task type
     */
    public Task editTask(int taskNumber, String newName, String newDate1, String newDate2) 
            throws IndexOutOfBoundsException, PazuzuExceptions.BadTaskException {
        validateTaskNumber(taskNumber);
        
        Task task = tasks.get(taskNumber - 1);
        
        // Edit name if provided
        if (!newName.equals("_")) {
            task.setName(newName);
        }
        
        // Edit task-specific fields based on task type
        if (task instanceof Deadline) {
            if (!newDate1.equals("_")) {
                // For deadline, newDate1 is the deadline, newDate2 should be "_"
                if (!newDate2.equals("_")) {
                    throw new PazuzuExceptions.BadTaskException("Deadline tasks only have one date field");
                }
                // Parse the deadline date
                // Note: We'll need to handle date parsing here or pass parsed dates from the caller
                // For now, we'll assume the caller handles date parsing
            }
        } else if (task instanceof Event) {
            if (!newDate1.equals("_")) {
                // Parse start date
                // Note: We'll need to handle date parsing here or pass parsed dates from the caller
            }
            if (!newDate2.equals("_")) {
                // Parse end date
                // Note: We'll need to handle date parsing here or pass parsed dates from the caller
            }
        } else {
            // Task (Todo) - should not have any date fields
            if (!newDate1.equals("_") || !newDate2.equals("_")) {
                throw new PazuzuExceptions.BadTaskException("Todo tasks do not have date fields");
            }
        }
        
        return task;
    }
    
    /**
     * Edits a task with parsed date objects.
     * 
     * @param taskNumber the 1-indexed position of the task to edit
     * @param newName the new name (use "_" to keep current name)
     * @param newDate1 the new first date (deadline for Deadline, start date for Event, null for Todo)
     * @param newDate2 the new second date (end date for Event, null for others)
     * @return the edited Task object
     * @throws IndexOutOfBoundsException if the task number is invalid
     * @throws PazuzuExceptions.BadTaskException if the edit parameters are invalid for the task type
     */
    public Task editTask(int taskNumber, String newName, LocalDateTime newDate1, LocalDateTime newDate2) 
            throws IndexOutOfBoundsException, PazuzuExceptions.BadTaskException {
        validateTaskNumber(taskNumber);
        
        Task task = tasks.get(taskNumber - 1);
        
        // Edit name if provided
        if (!newName.equals("_")) {
            task.setName(newName);
        }
        
        // Edit task-specific fields based on task type
        if (task instanceof Deadline) {
            Deadline deadlineTask = (Deadline) task;
            if (newDate1 != null) {
                // For deadline, newDate1 is the deadline, newDate2 should be null
                if (newDate2 != null) {
                    throw new PazuzuExceptions.BadTaskException("Deadline tasks only have one date field");
                }
                deadlineTask.setDeadline(newDate1);
            }
        } else if (task instanceof Event) {
            Event eventTask = (Event) task;
            if (newDate1 != null) {
                eventTask.setStartDate(newDate1);
            }
            if (newDate2 != null) {
                eventTask.setEndDate(newDate2);
            }
        } else {
            // Task (Todo) - should not have any date fields
            if (newDate1 != null || newDate2 != null) {
                throw new PazuzuExceptions.BadTaskException("Todo tasks do not have date fields");
            }
        }
        
        return task;
    }
    
    /**
     * Validates that a task number is within valid range (1-indexed).
     * 
     * @param taskNumber the task number to validate
     * @throws IndexOutOfBoundsException if the task number is invalid
     */
    private void validateTaskNumber(int taskNumber) throws IndexOutOfBoundsException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new IndexOutOfBoundsException("Task number out of range");
        }
    }
}
