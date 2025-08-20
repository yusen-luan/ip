/**
 * Represents an event task with start and end dates.
 * Extends the base Task class to include event timing information.
 */
public class Event extends Task {
    private String startDate;
    private String endDate;
    
    /**
     * Creates a new event task with the given name, start date, and end date.
     * The task is initially not done.
     * 
     * @param name the name of the event task
     * @param startDate the start date of the event
     * @param endDate the end date of the event
     */
    public Event(String name, String startDate, String endDate) {
        super(name);
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    /**
     * Gets the start date of this event.
     * 
     * @return the start date string
     */
    public String getStartDate() {
        return this.startDate;
    }
    
    /**
     * Gets the end date of this event.
     * 
     * @return the end date string
     */
    public String getEndDate() {
        return this.endDate;
    }
    
    /**
     * Prints the event task in the format [E][X] name (from: startDate to: endDate) 
     * where X appears only if the task is done.
     */
    @Override
    public void printTask() {
        String status = this.checkIsDone() ? "[X]" : "[ ]";
        System.out.println("[E]" + status + " " + this.getName() + " (from: " + this.startDate + " to: " + this.endDate + ")");
    }
}
