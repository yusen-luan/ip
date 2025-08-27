package pazuzu.task;
import java.util.ArrayList;

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
        this.tasks = tasks;
    }
    
    /**
     * Adds a task to the list.
     * 
     * @param task the Task object to be added to the list
     */
    public void addTask(Task task) {
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
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new IndexOutOfBoundsException("Task number out of range");
        }
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
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new IndexOutOfBoundsException("Task number out of range");
        }
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
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new IndexOutOfBoundsException("Task number out of range");
        }
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
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new IndexOutOfBoundsException("Task number out of range");
        }
        return tasks.get(taskNumber - 1);
    }
    
    /**
     * Gets a task by its 0-indexed position (for internal use like Storage).
     * 
     * @param index the 0-indexed position of the task
     * @return the Task object
     */
    public Task getTask(int index) {
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
}
