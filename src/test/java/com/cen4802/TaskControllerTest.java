package com.cen4802;

import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class TaskControllerTest {

    @Test
    @SuppressWarnings("unchecked")
    void showTasksDisplaysTheThreeStartingTasks() {
        // Creates a controller with its default tasks.
        TaskController controller = new TaskController();
        ExtendedModelMap model = new ExtendedModelMap();

        String viewName = controller.showTasks(model);
        List<Task> tasks = (List<Task>) model.getAttribute("tasks");

        assertEquals("index", viewName);
        assertEquals(3, model.getAttribute("taskCount"));
        assertIterableEquals(
                List.of("Finish Java assignment", "Study Git and GitHub", "Submit project"),
                tasks.stream().map(Task::getDescription).toList()
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    void addTaskAddsATrimmedDescription() {
        TaskController controller = new TaskController();

        // Adds a task with extra spaces to test input cleanup.
        String viewName = controller.addTask("  Write unit tests  ");

        ExtendedModelMap model = new ExtendedModelMap();
        controller.showTasks(model);
        List<Task> tasks = (List<Task>) model.getAttribute("tasks");

        assertEquals("redirect:/", viewName);
        assertIterableEquals(
                List.of(
                        "Finish Java assignment",
                        "Study Git and GitHub",
                        "Submit project",
                        "Write unit tests"
                ),
                tasks.stream().map(Task::getDescription).toList()
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    void addTaskDoesNotAddBlankDescriptions() {
        TaskController controller = new TaskController();

        // Attempts to add invalid task descriptions.
        controller.addTask("");
        controller.addTask("   ");

        ExtendedModelMap model = new ExtendedModelMap();
        controller.showTasks(model);
        List<Task> tasks = (List<Task>) model.getAttribute("tasks");

        assertIterableEquals(
                List.of("Finish Java assignment", "Study Git and GitHub", "Submit project"),
                tasks.stream().map(Task::getDescription).toList()
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    void completeTaskRemovesTheTaskAtTheRequestedIndex() {
        TaskController controller = new TaskController();

        // Completes the second task in the starting list.
        String viewName = controller.completeTask(1);

        ExtendedModelMap model = new ExtendedModelMap();
        controller.showTasks(model);
        List<Task> tasks = (List<Task>) model.getAttribute("tasks");

        assertEquals("redirect:/", viewName);
        assertIterableEquals(
                List.of("Finish Java assignment", "Submit project"),
                tasks.stream().map(Task::getDescription).toList()
        );
    }

    @Test
    void clearTasksRemovesAllTasks() {
        // Creates a controller containing the three starting tasks.
        TaskController controller = new TaskController();

        // Clears the list and captures the returned page instruction.
        String viewName = controller.clearTasks();

        // Gets the task list and count that would be displayed.
        ExtendedModelMap model = new ExtendedModelMap();
        controller.showTasks(model);

        // Checks that the method redirects home and leaves no tasks.
        assertEquals("redirect:/", viewName);
        assertEquals(0, model.getAttribute("taskCount"));
        assertEquals(List.of(), model.getAttribute("tasks"));
    }

    @Test
    void clearTasksWorksWhenAlreadyEmpty() {
        // Clears the starting tasks to prepare an empty list.
        TaskController controller = new TaskController();
        controller.clearTasks();

        // Attempts to clear the already empty list.
        String viewName = controller.clearTasks();

        ExtendedModelMap model = new ExtendedModelMap();
        controller.showTasks(model);

        // Checks that clearing an empty list still works correctly.
        assertEquals("redirect:/", viewName);
        assertEquals(0, model.getAttribute("taskCount"));
        assertEquals(List.of(), model.getAttribute("tasks"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void addTaskWorksAfterClearingAllTasks() {
        // Removes the starting tasks before adding a new one.
        TaskController controller = new TaskController();
        controller.clearTasks();

        // Adds a task to confirm that the cleared list remains usable.
        controller.addTask("Record midterm demonstration");

        ExtendedModelMap model = new ExtendedModelMap();
        controller.showTasks(model);
        List<Task> tasks = (List<Task>) model.getAttribute("tasks");

        // Checks that the list contains only the newly added task.
        assertEquals(1, model.getAttribute("taskCount"));
        assertIterableEquals(
                List.of("Record midterm demonstration"),
                tasks.stream().map(Task::getDescription).toList()
        );
    }
}
