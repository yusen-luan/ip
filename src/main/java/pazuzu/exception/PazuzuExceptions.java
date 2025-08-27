package pazuzu.exception;
/**
 * Contains custom exception classes for Pazuzu task management system.
 * Provides specific error handling for different types of user input errors.
 */
public class PazuzuExceptions {
    
    /**
     * Exception thrown when user input doesn't match any recognized command format.
     * Indicates that the command is not understood by the system.
     */
    public static class UndefinedCmdException extends Exception {
        public UndefinedCmdException(String message) {
            super(message);
        }
    }
    
    /**
     * Exception thrown when a task command is recognized but formatted incorrectly.
     * Examples include missing slashes, empty task names, or incomplete task information.
     */
    public static class BadTaskException extends Exception {
        public BadTaskException(String message) {
            super(message);
        }
    }
    
    /**
     * Exception thrown when attempting to mark an already completed task as done,
     * or when attempting to unmark a task that is already not done.
     */
    public static class MarkingException extends Exception {
        public MarkingException(String message) {
            super(message);
        }
    }
}
