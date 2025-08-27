/**
 * Represents a task with a name and completion status.
 * Tasks are not done by default when created.
 */
public class Task {
    private boolean isDone;
    private String name;
    
    /**
     * Creates a new task with the given name.
     * The task is initially not done.
     * 
     * @param name the name of the task
     */
    public Task(String name) {
        this.name = name;
        this.isDone = false;
    }
    
    /**
     * Marks this task as done.
     */
    public void markDone() {
        this.isDone = true;
    }
    
    /**
     * Marks this task as not done.
     */
    public void markNotDone() {
        this.isDone = false;
    }
    
    /**
     * Checks if this task is done.
     * 
     * @return true if the task is done, false otherwise
     */
    public boolean checkIsDone() {
        return this.isDone;
    }
    
    /**
     * Gets the name of this task.
     * 
     * @return the name of the task
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Prints the task in the format [T][X] name where X appears only if the task is done.
     */
    public void printTask() {
        String status = this.isDone ? "[X]" : "[ ]";
        System.out.println("[T]" + status + " " + this.name);
    }
    
    /**
     * Returns the task formatted as a string in the format [T][X] name where X appears only if the task is done.
     * 
     * @return the formatted task string
     */
    public String getTask() {
        String status = this.isDone ? "[X]" : "[ ]";
        return "[T]" + status + " " + this.name;
    }
}
