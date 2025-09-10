package pazuzu.util;

import java.time.format.DateTimeFormatter;

/**
 * Utility class for common date and time formatting operations.
 */
public class DateTimeUtil {
    /**
     * Standard output formatter for displaying dates and times.
     * Format: MMM dd yyyy HH:mm (e.g., "Dec 02 2019 18:00")
     */
    public static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");
}
