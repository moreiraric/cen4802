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
}
