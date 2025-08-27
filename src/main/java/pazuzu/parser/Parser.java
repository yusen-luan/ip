package pazuzu.parser;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import pazuzu.exception.PazuzuExceptions;
import pazuzu.task.Deadline;
import pazuzu.task.Event;
import pazuzu.task.Task;

/**
 * Handles parsing of user commands and date/time strings.
 */
public class Parser {
    
    /**
     * Parses a date string into a LocalDateTime object.
     * Supports various input formats including yyyy-mm-dd with optional time in 24hr format.
     * If no time is provided, defaults to 00:00.
     * 
     * @param dateTimeString the date string to parse (with optional time)
     * @return the parsed LocalDateTime
     * @throws PazuzuExceptions.BadTaskException if the date format is invalid
     */
    public LocalDateTime parseDateTime(String dateTimeString) throws PazuzuExceptions.BadTaskException {
        if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
            throw new PazuzuExceptions.BadTaskException("Empty date string");
        }
        
        dateTimeString = dateTimeString.trim();
        
        // Check if there's a time component (look for space followed by 3-4 digits)
        String datepart = dateTimeString;
        LocalTime timepart = LocalTime.of(0, 0); // Default to 00:00
        
        // Split on space to separate date and time
        String[] parts = dateTimeString.split("\\s+");
        if (parts.length == 2) {
            datepart = parts[0];
            String timePart = parts[1];
            
            // Parse time in formats like "1437" (14:37) or "0900" (09:00)
            if (timePart.matches("\\d{3,4}")) {
                try {
                    if (timePart.length() == 3) {
                        // Format like "900" -> 09:00
                        int hour = Integer.parseInt(timePart.substring(0, 1));
                        int minute = Integer.parseInt(timePart.substring(1, 3));
                        timepart = LocalTime.of(hour, minute);
                    } else if (timePart.length() == 4) {
                        // Format like "1437" -> 14:37
                        int hour = Integer.parseInt(timePart.substring(0, 2));
                        int minute = Integer.parseInt(timePart.substring(2, 4));
                        timepart = LocalTime.of(hour, minute);
                    }
                } catch (Exception e) {
                    throw new PazuzuExceptions.BadTaskException("Invalid time format: " + timePart + ". Use 24hr format like 1437 for 14:37");
                }
            } else {
                throw new PazuzuExceptions.BadTaskException("Invalid time format: " + timePart + ". Use 24hr format like 1437 for 14:37");
            }
        }
        
        // Define possible input date formats
        DateTimeFormatter[] dateFormatters = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("MM-dd-yyyy"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy") // This should handle 12/12/2012
        };
        
        for (DateTimeFormatter formatter : dateFormatters) {
            try {
                LocalDate date = LocalDate.parse(datepart, formatter);
                return LocalDateTime.of(date, timepart);
            } catch (DateTimeParseException e) {
                // Try next format
            }
        }
        
        throw new PazuzuExceptions.BadTaskException("Invalid date format: " + datepart + ". Please use formats like yyyy-mm-dd or dd/mm/yyyy");
    }
    
    /**
     * Parses a task creation command and returns the appropriate Task object.
     * 
     * @param input the full input string from the user
     * @return the created Task object
     * @throws PazuzuExceptions.BadTaskException when task format is invalid
     */
    public Task parseTaskCommand(String input) throws PazuzuExceptions.BadTaskException {
        if (input.startsWith("todo")) {
            return parseTodoCommand(input);
        } else if (input.startsWith("deadline")) {
            return parseDeadlineCommand(input);
        } else if (input.startsWith("event")) {
            return parseEventCommand(input);
        } else {
            throw new PazuzuExceptions.BadTaskException("Unknown task type");
        }
    }
    
    /**
     * Parses a todo command and creates a Task object.
     * 
     * @param input the todo command string
     * @return the created Task object
     * @throws PazuzuExceptions.BadTaskException when format is invalid
     */
    private Task parseTodoCommand(String input) throws PazuzuExceptions.BadTaskException {
        if (input.length() <= 4 || !input.substring(4, 5).equals(" ")) {
            throw new PazuzuExceptions.BadTaskException("Invalid todo format");
        }
        String taskName = input.substring(5).trim();
        if (taskName.isEmpty()) {
            throw new PazuzuExceptions.BadTaskException("Empty task name");
        }
        return new Task(taskName);
    }
    
    /**
     * Parses a deadline command and creates a Deadline object.
     * 
     * @param input the deadline command string
     * @return the created Deadline object
     * @throws PazuzuExceptions.BadTaskException when format is invalid
     */
    private Deadline parseDeadlineCommand(String input) throws PazuzuExceptions.BadTaskException {
        if (input.length() <= 8 || !input.substring(8, 9).equals(" ")) {
            throw new PazuzuExceptions.BadTaskException("Invalid deadline format");
        }
        String remaining = input.substring(9).trim();
        int pipeIndex = remaining.lastIndexOf('|');
        if (pipeIndex == -1 || pipeIndex >= remaining.length() - 1) {
            throw new PazuzuExceptions.BadTaskException("Invalid deadline format");
        }
        String taskName = remaining.substring(0, pipeIndex).trim();
        String deadline = remaining.substring(pipeIndex + 1).trim();
        if (taskName.isEmpty() || deadline.isEmpty()) {
            throw new PazuzuExceptions.BadTaskException("Empty task name or deadline");
        }
        LocalDateTime parsedDeadline = parseDateTime(deadline);
        return new Deadline(taskName, parsedDeadline);
    }
    
    /**
     * Parses an event command and creates an Event object.
     * 
     * @param input the event command string
     * @return the created Event object
     * @throws PazuzuExceptions.BadTaskException when format is invalid
     */
    private Event parseEventCommand(String input) throws PazuzuExceptions.BadTaskException {
        if (input.length() <= 5 || !input.substring(5, 6).equals(" ")) {
            throw new PazuzuExceptions.BadTaskException("Invalid event format");
        }
        String remaining = input.substring(6).trim();
        String[] parts = remaining.split("\\|");
        if (parts.length != 3) {
            throw new PazuzuExceptions.BadTaskException("Invalid event format");
        }
        String taskName = parts[0].trim();
        String startDate = parts[1].trim();
        String endDate = parts[2].trim();
        if (taskName.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            throw new PazuzuExceptions.BadTaskException("Empty task name, start date, or end date");
        }
        LocalDateTime parsedStartDate = parseDateTime(startDate);
        LocalDateTime parsedEndDate = parseDateTime(endDate);
        return new Event(taskName, parsedStartDate, parsedEndDate);
    }
    
    /**
     * Parses a command that contains a task number (mark, unmark, delete).
     * 
     * @param input the command string
     * @param commandLength the length of the command prefix (e.g., "mark " = 5)
     * @return the task number (1-indexed)
     * @throws NumberFormatException if the task number is not a valid integer
     */
    public int parseTaskNumber(String input, int commandLength) throws NumberFormatException {
        return Integer.parseInt(input.substring(commandLength).trim());
    }
    
    /**
     * Parses a find command and extracts the search keyword.
     * 
     * @param input the find command string
     * @return the search keyword
     * @throws PazuzuExceptions.BadTaskException when format is invalid
     */
    public String parseFindCommand(String input) throws PazuzuExceptions.BadTaskException {
        if (input.length() <= 5 || !input.substring(4, 5).equals(" ")) {
            throw new PazuzuExceptions.BadTaskException("Invalid find format");
        }
        String keyword = input.substring(5).trim();
        if (keyword.isEmpty()) {
            throw new PazuzuExceptions.BadTaskException("Empty search keyword");
        }
        return keyword;
    }
}
