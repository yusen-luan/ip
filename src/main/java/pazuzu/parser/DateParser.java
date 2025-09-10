package pazuzu.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import pazuzu.exception.PazuzuExceptions;

/**
 * Handles parsing of date and time strings into LocalDateTime objects.
 * Supports various input formats including yyyy-mm-dd with optional time in 24hr format.
 */
public class DateParser {
    // Time parsing constants
    private static final int THREE_DIGIT_TIME_LENGTH = 3;
    private static final int FOUR_DIGIT_TIME_LENGTH = 4;
    private static final int HOUR_INDEX_THREE_DIGIT = 1;
    private static final int MINUTE_INDEX_THREE_DIGIT = 1;
    private static final int HOUR_INDEX_FOUR_DIGIT = 2;
    private static final String TIME_PATTERN = "\\d{3,4}";
    
    // Supported date formats
    private static final DateTimeFormatter[] DATE_FORMATTERS = {
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ofPattern("yyyy/MM/dd"),
        DateTimeFormatter.ofPattern("dd-MM-yyyy"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy"),
        DateTimeFormatter.ofPattern("MM-dd-yyyy"),
        DateTimeFormatter.ofPattern("MM/dd/yyyy")
    };
    
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
        validateInput(dateTimeString);
        
        String trimmedInput = dateTimeString.trim();
        String[] parts = splitDateAndTime(trimmedInput);
        
        String datePart = parts[0];
        LocalTime timePart = parseTimePart(parts);
        
        LocalDate date = parseDatePart(datePart);
        return LocalDateTime.of(date, timePart);
    }
    
    /**
     * Validates that the input string is not null or empty.
     */
    private void validateInput(String dateTimeString) throws PazuzuExceptions.BadTaskException {
        if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
            throw new PazuzuExceptions.BadTaskException("Empty date string");
        }
    }
    
    /**
     * Splits the input string into date and time components.
     * Returns array where [0] is date part and [1] is time part (if present).
     */
    private String[] splitDateAndTime(String dateTimeString) {
        String[] parts = dateTimeString.split("\\s+");
        if (parts.length > 2) {
            // If more than 2 parts, join everything except the last part as date
            String[] result = new String[2];
            result[0] = String.join(" ", java.util.Arrays.copyOf(parts, parts.length - 1));
            result[1] = parts[parts.length - 1];
            return result;
        }
        return parts;
    }
    
    /**
     * Parses the time component from the split parts.
     * Returns LocalTime.of(0, 0) if no time part is present.
     */
    private LocalTime parseTimePart(String[] parts) throws PazuzuExceptions.BadTaskException {
        if (parts.length == 1) {
            return LocalTime.of(0, 0); // Default to 00:00 if no time provided
        }
        
        String timeString = parts[1];
        return parseTimeString(timeString);
    }
    
    /**
     * Parses a time string in formats like "1437" (14:37) or "0900" (09:00).
     */
    private LocalTime parseTimeString(String timeString) throws PazuzuExceptions.BadTaskException {
        if (!timeString.matches(TIME_PATTERN)) {
            throw new PazuzuExceptions.BadTaskException(
                "Invalid time format: " + timeString + ". Use 24hr format like 1437 for 14:37");
        }
        
        try {
            if (timeString.length() == THREE_DIGIT_TIME_LENGTH) {
                return parseThreeDigitTime(timeString);
            } else if (timeString.length() == FOUR_DIGIT_TIME_LENGTH) {
                return parseFourDigitTime(timeString);
            } else {
                throw new PazuzuExceptions.BadTaskException(
                    "Invalid time format: " + timeString + ". Use 24hr format like 1437 for 14:37");
            }
        } catch (DateTimeException | NumberFormatException e) {
            throw new PazuzuExceptions.BadTaskException(
                "Invalid time format: " + timeString + ". Use 24hr format like 1437 for 14:37");
        }
    }
    
    /**
     * Parses three-digit time format like "900" -> 09:00.
     */
    private LocalTime parseThreeDigitTime(String timeString) {
        int hour = Integer.parseInt(timeString.substring(0, HOUR_INDEX_THREE_DIGIT));
        int minute = Integer.parseInt(timeString.substring(MINUTE_INDEX_THREE_DIGIT, THREE_DIGIT_TIME_LENGTH));
        return LocalTime.of(hour, minute);
    }
    
    /**
     * Parses four-digit time format like "1437" -> 14:37.
     */
    private LocalTime parseFourDigitTime(String timeString) {
        int hour = Integer.parseInt(timeString.substring(0, HOUR_INDEX_FOUR_DIGIT));
        int minute = Integer.parseInt(timeString.substring(HOUR_INDEX_FOUR_DIGIT, FOUR_DIGIT_TIME_LENGTH));
        return LocalTime.of(hour, minute);
    }
    
    /**
     * Parses the date component using various supported formats.
     */
    private LocalDate parseDatePart(String datePart) throws PazuzuExceptions.BadTaskException {
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                return LocalDate.parse(datePart, formatter);
            } catch (DateTimeParseException e) {
                // Try next formatter if current one fails
            }
        }
        
        throw new PazuzuExceptions.BadTaskException(
            "Invalid date format: " + datePart + ". Please use formats like yyyy-mm-dd or dd/mm/yyyy");
    }
}
