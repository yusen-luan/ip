import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.time.LocalDateTime;

import pazuzu.task.TaskList;
import pazuzu.task.Task;
import pazuzu.task.Deadline;
import pazuzu.task.Event;
import pazuzu.exception.PazuzuExceptions;

/**
 * Test class for TaskList functionality including task management operations, 
 * exception handling, and boundary conditions.
 */
public class TaskListTest {
    private TaskList taskList;
    private Task todoTask;
    private Deadline deadlineTask;
    private Event eventTask;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
        todoTask = new Task("buy groceries");
        deadlineTask = new Deadline("submit assignment", LocalDateTime.of(2023, 12, 25, 14, 0));
        eventTask = new Event("team meeting", 
                             LocalDateTime.of(2023, 12, 25, 10, 0),
                             LocalDateTime.of(2023, 12, 25, 12, 0));
    }

    // Tests for constructor and initialization
    @Test
    public void testEmptyConstructor() {
        TaskList emptyList = new TaskList();
        assertTrue(emptyList.isEmpty());
        assertEquals(0, emptyList.getSize());
    }

    @Test
    public void testConstructorWithArrayList() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(todoTask);
        tasks.add(deadlineTask);
        
        TaskList prefilledList = new TaskList(tasks);
        assertEquals(2, prefilledList.getSize());
        assertFalse(prefilledList.isEmpty());
        assertEquals(todoTask, prefilledList.getTask(0));
        assertEquals(deadlineTask, prefilledList.getTask(1));
    }

    // Tests for addTask method
    @Test
    public void testAddTask() {
        assertTrue(taskList.isEmpty());
        
        taskList.addTask(todoTask);
        assertEquals(1, taskList.getSize());
        assertFalse(taskList.isEmpty());
        assertEquals(todoTask, taskList.getTask(0));
        
        taskList.addTask(deadlineTask);
        assertEquals(2, taskList.getSize());
        assertEquals(deadlineTask, taskList.getTask(1));
    }

    @Test
    public void testAddMultipleTasks() {
        taskList.addTask(todoTask);
        taskList.addTask(deadlineTask);
        taskList.addTask(eventTask);
        
        assertEquals(3, taskList.getSize());
        assertEquals(todoTask, taskList.getTask(0));
        assertEquals(deadlineTask, taskList.getTask(1));
        assertEquals(eventTask, taskList.getTask(2));
    }

    // Tests for deleteTask method
    @Test
    public void testDeleteTask_ValidIndex() {
        taskList.addTask(todoTask);
        taskList.addTask(deadlineTask);
        taskList.addTask(eventTask);
        
        Task deletedTask = taskList.deleteTask(2); // Delete middle task (1-indexed)
        assertEquals(deadlineTask, deletedTask);
        assertEquals(2, taskList.getSize());
        assertEquals(todoTask, taskList.getTask(0));
        assertEquals(eventTask, taskList.getTask(1));
    }

    @Test
    public void testDeleteTask_LastTask() {
        taskList.addTask(todoTask);
        taskList.addTask(deadlineTask);
        
        Task deletedTask = taskList.deleteTask(2); // Delete last task
        assertEquals(deadlineTask, deletedTask);
        assertEquals(1, taskList.getSize());
        assertEquals(todoTask, taskList.getTask(0));
    }

    @Test
    public void testDeleteTask_OnlyTask() {
        taskList.addTask(todoTask);
        
        Task deletedTask = taskList.deleteTask(1);
        assertEquals(todoTask, deletedTask);
        assertTrue(taskList.isEmpty());
        assertEquals(0, taskList.getSize());
    }

    @Test
    public void testDeleteTask_InvalidIndex() {
        taskList.addTask(todoTask);
        
        // Test index too high
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.deleteTask(2);
        });
        
        // Test index too low
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.deleteTask(0);
        });
        
        // Test negative index
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.deleteTask(-1);
        });
    }

    @Test
    public void testDeleteTask_EmptyList() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.deleteTask(1);
        });
    }

    // Tests for markTask method
    @Test
    public void testMarkTask_ValidTask() throws PazuzuExceptions.MarkingException {
        taskList.addTask(todoTask);
        assertFalse(todoTask.checkIsDone());
        
        Task markedTask = taskList.markTask(1);
        assertEquals(todoTask, markedTask);
        assertTrue(todoTask.checkIsDone());
    }

    @Test
    public void testMarkTask_AlreadyDone() {
        taskList.addTask(todoTask);
        todoTask.markDone(); // Mark it done first
        
        assertThrows(PazuzuExceptions.MarkingException.class, () -> {
            taskList.markTask(1);
        });
    }

    @Test
    public void testMarkTask_InvalidIndex() {
        taskList.addTask(todoTask);
        
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.markTask(2);
        });
        
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.markTask(0);
        });
    }

    // Tests for unmarkTask method
    @Test
    public void testUnmarkTask_ValidTask() throws PazuzuExceptions.MarkingException {
        taskList.addTask(todoTask);
        todoTask.markDone(); // Mark it done first
        assertTrue(todoTask.checkIsDone());
        
        Task unmarkedTask = taskList.unmarkTask(1);
        assertEquals(todoTask, unmarkedTask);
        assertFalse(todoTask.checkIsDone());
    }

    @Test
    public void testUnmarkTask_AlreadyNotDone() {
        taskList.addTask(todoTask);
        assertFalse(todoTask.checkIsDone()); // Already not done
        
        assertThrows(PazuzuExceptions.MarkingException.class, () -> {
            taskList.unmarkTask(1);
        });
    }

    @Test
    public void testUnmarkTask_InvalidIndex() {
        taskList.addTask(todoTask);
        todoTask.markDone();
        
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.unmarkTask(2);
        });
        
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.unmarkTask(0);
        });
    }

    // Tests for getTaskByNumber method
    @Test
    public void testGetTaskByNumber_ValidIndex() {
        taskList.addTask(todoTask);
        taskList.addTask(deadlineTask);
        
        assertEquals(todoTask, taskList.getTaskByNumber(1));
        assertEquals(deadlineTask, taskList.getTaskByNumber(2));
    }

    @Test
    public void testGetTaskByNumber_InvalidIndex() {
        taskList.addTask(todoTask);
        
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.getTaskByNumber(2);
        });
        
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.getTaskByNumber(0);
        });
        
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.getTaskByNumber(-1);
        });
    }

    // Tests for getTask method (0-indexed)
    @Test
    public void testGetTask_ValidIndex() {
        taskList.addTask(todoTask);
        taskList.addTask(deadlineTask);
        
        assertEquals(todoTask, taskList.getTask(0));
        assertEquals(deadlineTask, taskList.getTask(1));
    }

    // Tests for size and empty methods
    @Test
    public void testGetSize() {
        assertEquals(0, taskList.getSize());
        
        taskList.addTask(todoTask);
        assertEquals(1, taskList.getSize());
        
        taskList.addTask(deadlineTask);
        assertEquals(2, taskList.getSize());
        
        taskList.deleteTask(1);
        assertEquals(1, taskList.getSize());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(taskList.isEmpty());
        
        taskList.addTask(todoTask);
        assertFalse(taskList.isEmpty());
        
        taskList.deleteTask(1);
        assertTrue(taskList.isEmpty());
    }

    // Tests for getAllTasks method
    @Test
    public void testGetAllTasks() {
        taskList.addTask(todoTask);
        taskList.addTask(deadlineTask);
        
        ArrayList<Task> allTasks = taskList.getAllTasks();
        assertEquals(2, allTasks.size());
        assertEquals(todoTask, allTasks.get(0));
        assertEquals(deadlineTask, allTasks.get(1));
    }

    // Integration tests
    @Test
    public void testMarkUnmarkSequence() throws PazuzuExceptions.MarkingException {
        taskList.addTask(todoTask);
        taskList.addTask(deadlineTask);
        
        // Mark first task
        taskList.markTask(1);
        assertTrue(taskList.getTask(0).checkIsDone());
        assertFalse(taskList.getTask(1).checkIsDone());
        
        // Mark second task
        taskList.markTask(2);
        assertTrue(taskList.getTask(0).checkIsDone());
        assertTrue(taskList.getTask(1).checkIsDone());
        
        // Unmark first task
        taskList.unmarkTask(1);
        assertFalse(taskList.getTask(0).checkIsDone());
        assertTrue(taskList.getTask(1).checkIsDone());
    }

    @Test
    public void testComplexTaskOperations() throws PazuzuExceptions.MarkingException {
        // Add multiple tasks
        taskList.addTask(todoTask);
        taskList.addTask(deadlineTask);
        taskList.addTask(eventTask);
        assertEquals(3, taskList.getSize());
        
        // Mark middle task
        taskList.markTask(2);
        assertTrue(taskList.getTaskByNumber(2).checkIsDone());
        
        // Delete first task
        Task deleted = taskList.deleteTask(1);
        assertEquals(todoTask, deleted);
        assertEquals(2, taskList.getSize());
        
        // Verify order is maintained
        assertEquals(deadlineTask, taskList.getTask(0));
        assertEquals(eventTask, taskList.getTask(1));
        
        // Original task 2 (deadlineTask) is now at position 1
        assertTrue(taskList.getTaskByNumber(1).checkIsDone());
    }

    // Edge case tests
    @Test
    public void testLargeTaskList() {
        // Add many tasks to test performance and correctness
        for (int i = 0; i < 100; i++) {
            taskList.addTask(new Task("Task " + i));
        }
        
        assertEquals(100, taskList.getSize());
        assertEquals("Task 0", taskList.getTask(0).getName());
        assertEquals("Task 99", taskList.getTask(99).getName());
        
        // Delete from middle
        taskList.deleteTask(50); // This should be "Task 49" (1-indexed)
        assertEquals(99, taskList.getSize());
        assertEquals("Task 48", taskList.getTaskByNumber(49).getName());
        assertEquals("Task 50", taskList.getTaskByNumber(50).getName());
    }
}
