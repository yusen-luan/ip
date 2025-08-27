package pazuzu.task;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event task with start and end dates.
 * Extends the base Task class to include event timing information.
 */
public class Event extends Task {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");
    
    /**
     * Creates a new event task with the given name, start date, and end date.
     * The task is initially not done.
     * 
     * @param name the name of the event task
     * @param startDate the start date of the event as a LocalDateTime
     * @param endDate the end date of the event as a LocalDateTime
     */
    public Event(String name, LocalDateTime startDate, LocalDateTime endDate) {
        super(name);
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    /**
     * Gets the start date of this event.
     * 
     * @return the start date as a LocalDateTime
     */
    public LocalDateTime getStartDate() {
        return this.startDate;
    }
    
    /**
     * Gets the end date of this event.
     * 
     * @return the end date as a LocalDateTime
     */
    public LocalDateTime getEndDate() {
        return this.endDate;
    }
    
    /**
     * Prints the event task in the format [E][X] name (from: startDate to: endDate) 
     * where X appears only if the task is done.
     */
    @Override
    public void printTask() {
        System.out.println(getTask());
    }
    
    /**
     * Returns the event task formatted as a string in the format [E][X] name (from: startDate to: endDate) 
     * where X appears only if the task is done.
     * 
     * @return the formatted event task string
     */
    @Override
    public String getTask() {
        String status = this.checkIsDone() ? "[X]" : "[ ]";
        String formattedStartDate = this.startDate.format(OUTPUT_FORMATTER);
        String formattedEndDate = this.endDate.format(OUTPUT_FORMATTER);
        return "[E]" + status + " " + this.getName() + " (from: " + formattedStartDate + " to: " + formattedEndDate + ")";
    }
}
