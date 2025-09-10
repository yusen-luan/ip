package pazuzu.task;
import java.time.LocalDateTime;

import pazuzu.util.DateTimeUtil;

/**
 * Represents a deadline task with a completion deadline.
 * Extends the base Task class to include deadline information.
 */
public class Deadline extends Task {
    private static final String TASK_TYPE_DEADLINE = "[D]";
    private LocalDateTime deadline;
    
    /**
     * Creates a new deadline task with the given name and deadline.
     * The task is initially not done.
     * 
     * @param name the name of the deadline task
     * @param deadline the deadline for completing this task as a LocalDateTime
     */
    public Deadline(String name, LocalDateTime deadline) {
        super(name);
        this.deadline = deadline;
    }
    
    /**
     * Gets the deadline of this task.
     * 
     * @return the deadline as a LocalDateTime
     */
    public LocalDateTime getDeadline() {
        return this.deadline;
    }
    
    /**
     * Prints the deadline task in the format [D][X] name (by: deadline) 
     * where X appears only if the task is done.
     */
    @Override
    public void printTask() {
        System.out.println(getTask());
    }
    
    /**
     * Returns the deadline task formatted as a string in the format [D][X] name (by: deadline) 
     * where X appears only if the task is done.
     * 
     * @return the formatted deadline task string
     */
    @Override
    public String getTask() {
        String status = this.checkIsDone() ? TASK_STATUS_DONE : TASK_STATUS_NOT_DONE;
        String formattedDate = this.deadline.format(DateTimeUtil.OUTPUT_FORMATTER);
        return TASK_TYPE_DEADLINE + status + " " + this.getName() + " (by: " + formattedDate + ")";
    }
}
