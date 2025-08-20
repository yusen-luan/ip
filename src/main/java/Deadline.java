/**
 * Represents a deadline task with a completion deadline.
 * Extends the base Task class to include deadline information.
 */
public class Deadline extends Task {
    private String deadline;
    
    /**
     * Creates a new deadline task with the given name and deadline.
     * The task is initially not done.
     * 
     * @param name the name of the deadline task
     * @param deadline the deadline for completing this task
     */
    public Deadline(String name, String deadline) {
        super(name);
        this.deadline = deadline;
    }
    
    /**
     * Gets the deadline of this task.
     * 
     * @return the deadline string
     */
    public String getDeadline() {
        return this.deadline;
    }
    
    /**
     * Prints the deadline task in the format [D][X] name (by: deadline) 
     * where X appears only if the task is done.
     */
    @Override
    public void printTask() {
        String status = this.checkIsDone() ? "[X]" : "[ ]";
        System.out.println("[D]" + status + " " + this.getName() + " (by: " + this.deadline + ")");
    }
}
