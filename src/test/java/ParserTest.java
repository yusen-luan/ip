import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import pazuzu.parser.Parser;
import pazuzu.exception.PazuzuExceptions;
import pazuzu.task.Task;
import pazuzu.task.Deadline;
import pazuzu.task.Event;

/**
 * Test class for Parser functionality including date parsing, task command parsing, and edge cases.
 */
public class ParserTest {
    private Parser parser;

    @BeforeEach
    public void setUp() {
        parser = new Parser();
    }

    // Tests for parseDateTime method
    @Test
    public void testParseDateTime_ValidDateFormats() throws PazuzuExceptions.BadTaskException {
        // Test yyyy-MM-dd format
        LocalDateTime result1 = parser.parseDateTime("2023-12-25");
        assertEquals(LocalDateTime.of(2023, 12, 25, 0, 0), result1);

        // Test dd/MM/yyyy format
        LocalDateTime result2 = parser.parseDateTime("25/12/2023");
        assertEquals(LocalDateTime.of(2023, 12, 25, 0, 0), result2);

        // Test yyyy/MM/dd format
        LocalDateTime result3 = parser.parseDateTime("2023/12/25");
        assertEquals(LocalDateTime.of(2023, 12, 25, 0, 0), result3);
    }

    @Test
    public void testParseDateTime_WithTimeComponent() throws PazuzuExceptions.BadTaskException {
        // Test with 4-digit time
        LocalDateTime result1 = parser.parseDateTime("2023-12-25 1437");
        assertEquals(LocalDateTime.of(2023, 12, 25, 14, 37), result1);

        // Test with 3-digit time
        LocalDateTime result2 = parser.parseDateTime("2023-12-25 900");
        assertEquals(LocalDateTime.of(2023, 12, 25, 9, 0), result2);

        // Test edge times
        LocalDateTime result3 = parser.parseDateTime("2023-12-25 0000");
        assertEquals(LocalDateTime.of(2023, 12, 25, 0, 0), result3);

        LocalDateTime result4 = parser.parseDateTime("2023-12-25 2359");
        assertEquals(LocalDateTime.of(2023, 12, 25, 23, 59), result4);
    }

    @Test
    public void testParseDateTime_InvalidFormats() {
        // Test null input
        assertThrows(PazuzuExceptions.BadTaskException.class, () -> {
            parser.parseDateTime(null);
        });

        // Test empty string
        assertThrows(PazuzuExceptions.BadTaskException.class, () -> {
            parser.parseDateTime("");
        });

        // Test invalid date format
        assertThrows(PazuzuExceptions.BadTaskException.class, () -> {
            parser.parseDateTime("invalid-date");
        });

        // Test invalid time format
        assertThrows(PazuzuExceptions.BadTaskException.class, () -> {
            parser.parseDateTime("2023-12-25 25:70");
        });

        // Test invalid time (out of range)
        assertThrows(PazuzuExceptions.BadTaskException.class, () -> {
            parser.parseDateTime("2023-12-25 2570");
        });
    }

    // Tests for parseTaskCommand method
    @Test
    public void testParseTaskCommand_TodoTask() throws PazuzuExceptions.BadTaskException {
        Task task = parser.parseTaskCommand("todo buy groceries");
        assertTrue(task instanceof Task);
        assertEquals("buy groceries", task.getName());
        assertFalse(task.checkIsDone());
    }

    @Test
    public void testParseTaskCommand_DeadlineTask() throws PazuzuExceptions.BadTaskException {
        Task task = parser.parseTaskCommand("deadline submit assignment | 2023-12-25 1400");
        assertTrue(task instanceof Deadline);
        assertEquals("submit assignment", task.getName());
        assertFalse(task.checkIsDone());
    }

    @Test
    public void testParseTaskCommand_EventTask() throws PazuzuExceptions.BadTaskException {
        Task task = parser.parseTaskCommand("event team meeting | 2023-12-25 1400 | 2023-12-25 1600");
        assertTrue(task instanceof Event);
        assertEquals("team meeting", task.getName());
        assertFalse(task.checkIsDone());
    }

    @Test
    public void testParseTaskCommand_InvalidTodoFormat() {
        // Test todo without space
        assertThrows(PazuzuExceptions.BadTaskException.class, () -> {
            parser.parseTaskCommand("todo");
        });

        // Test todo with empty name
        assertThrows(PazuzuExceptions.BadTaskException.class, () -> {
            parser.parseTaskCommand("todo ");
        });

        // Test todo with only spaces
        assertThrows(PazuzuExceptions.BadTaskException.class, () -> {
            parser.parseTaskCommand("todo    ");
        });
    }

    @Test
    public void testParseTaskCommand_InvalidDeadlineFormat() {
        // Test deadline without pipe
        assertThrows(PazuzuExceptions.BadTaskException.class, () -> {
            parser.parseTaskCommand("deadline submit assignment");
        });

        // Test deadline with empty task name
        assertThrows(PazuzuExceptions.BadTaskException.class, () -> {
            parser.parseTaskCommand("deadline | 2023-12-25");
        });

        // Test deadline with empty date
        assertThrows(PazuzuExceptions.BadTaskException.class, () -> {
            parser.parseTaskCommand("deadline submit assignment |");
        });

        // Test deadline with invalid date
        assertThrows(PazuzuExceptions.BadTaskException.class, () -> {
            parser.parseTaskCommand("deadline submit assignment | invalid-date");
        });
    }

    @Test
    public void testParseTaskCommand_InvalidEventFormat() {
        // Test event with wrong number of pipes
        assertThrows(PazuzuExceptions.BadTaskException.class, () -> {
            parser.parseTaskCommand("event team meeting | 2023-12-25");
        });

        // Test event with empty task name
        assertThrows(PazuzuExceptions.BadTaskException.class, () -> {
            parser.parseTaskCommand("event | 2023-12-25 1400 | 2023-12-25 1600");
        });

        // Test event with empty start date
        assertThrows(PazuzuExceptions.BadTaskException.class, () -> {
            parser.parseTaskCommand("event team meeting | | 2023-12-25 1600");
        });

        // Test event with empty end date
        assertThrows(PazuzuExceptions.BadTaskException.class, () -> {
            parser.parseTaskCommand("event team meeting | 2023-12-25 1400 |");
        });
    }

    @Test
    public void testParseTaskCommand_UnknownTaskType() {
        assertThrows(PazuzuExceptions.BadTaskException.class, () -> {
            parser.parseTaskCommand("unknown task type");
        });
    }

    // Tests for parseTaskNumber method
    @Test
    public void testParseTaskNumber_ValidNumbers() {
        assertEquals(1, parser.parseTaskNumber("mark 1", 5));
        assertEquals(42, parser.parseTaskNumber("unmark 42", 7));
        assertEquals(999, parser.parseTaskNumber("delete 999", 7));
    }

    @Test
    public void testParseTaskNumber_WithExtraSpaces() {
        assertEquals(5, parser.parseTaskNumber("mark   5   ", 5));
        assertEquals(10, parser.parseTaskNumber("delete    10", 7));
    }

    @Test
    public void testParseTaskNumber_InvalidFormat() {
        // Test non-numeric input
        assertThrows(NumberFormatException.class, () -> {
            parser.parseTaskNumber("mark abc", 5);
        });

        // Test empty number
        assertThrows(NumberFormatException.class, () -> {
            parser.parseTaskNumber("mark ", 5);
        });

        // Test negative number (technically valid integer but logically invalid)
        assertEquals(-1, parser.parseTaskNumber("mark -1", 5));
    }

    // Edge case tests
    @Test
    public void testParseDateTime_LeapYear() throws PazuzuExceptions.BadTaskException {
        LocalDateTime result = parser.parseDateTime("2024-02-29");
        assertEquals(LocalDateTime.of(2024, 2, 29, 0, 0), result);
    }

    @Test
    public void testParseDateTime_WhitespaceHandling() throws PazuzuExceptions.BadTaskException {
        LocalDateTime result1 = parser.parseDateTime("   2023-12-25   ");
        assertEquals(LocalDateTime.of(2023, 12, 25, 0, 0), result1);

        LocalDateTime result2 = parser.parseDateTime("2023-12-25   1400");
        assertEquals(LocalDateTime.of(2023, 12, 25, 14, 0), result2);
    }

    @Test
    public void testParseTaskCommand_ComplexTaskNames() throws PazuzuExceptions.BadTaskException {
        // Test task name with special characters
        Task task1 = parser.parseTaskCommand("todo buy milk & bread (urgent!)");
        assertEquals("buy milk & bread (urgent!)", task1.getName());

        // Test task name with numbers
        Task task2 = parser.parseTaskCommand("todo complete CS2103T project v1.2");
        assertEquals("complete CS2103T project v1.2", task2.getName());
    }
}
