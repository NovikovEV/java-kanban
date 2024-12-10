package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskTest {
    private static Task task1;
    private static Task task2;

    @BeforeEach
    void init() {
        task1 = new Task(
                1,
                "Приготовить кофе",
                "добавить сливки",
                TaskStatus.NEW
        );

        task2 = new Task(
                1,
                "Купить хлеб",
                "половину буханки",
                TaskStatus.DONE
        );
    }


    @Test
    void testToString() {
        String expected = "Task{id=1, taskName='Приготовить кофе', description='добавить сливки', taskStatus=NEW}";
        String actually = task1.toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testEquals() {
        boolean actually = task1.equals(task2);
        Assertions.assertTrue(actually);
    }

    @Test
    void testHashCode() {
        int expected = -1349013337;
        int actually = task1.hashCode();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testGetId() {
        int expected = 1;
        int actually = task1.getId();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testSetId() {
        int expected = 2;
        task1.setId(2);
        int actually = task1.getId();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testGetTaskName() {
        String expected = "Приготовить кофе";
        String actually = task1.getTaskName();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testSetTaskName() {
        String expected = "Новое имя";
        task1.setTaskName(expected);
        String actually = task1.getTaskName();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testGetDescription() {
        String expected = "добавить сливки";
        String actually = task1.getDescription();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testSetDescription() {
        String expected = "Новое примечание";
        task1.setDescription(expected);
        String actually = task1.getDescription();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testGetTaskStatus() {
        TaskStatus expected = TaskStatus.NEW;
        TaskStatus actually = task1.getTaskStatus();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testSetTaskStatus() {
        TaskStatus expected = TaskStatus.IN_PROGRESS;
        task1.setTaskStatus(TaskStatus.IN_PROGRESS);
        TaskStatus actually = task1.getTaskStatus();
        Assertions.assertEquals(expected, actually);
    }
}