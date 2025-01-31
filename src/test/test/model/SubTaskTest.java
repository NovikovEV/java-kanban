package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubTaskTest {
    private SubTask subTask;

    @BeforeEach
    void init() {
        subTask = new SubTask(
                1,
                1,
                "Пропылесосить комнаты",
                "тщательно",
                TaskStatus.NEW
        );
    }

    @Test
    void testToString() {
        String expected = "SubTask{id=1, epicId=1, name=Пропылесосить комнаты, status=NEW}";
        String actually = subTask.toString();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testSetEpicId() {
        int expected = 2;
        subTask.setEpicId(2);
        int actually = subTask.getEpicId();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testGetEpicId() {
        int expected = 1;
        int actually = subTask.getEpicId();
        Assertions.assertEquals(expected, actually);
    }

    @Test
    void testSerializeToCsv() {
        String expected = "1,SUBTASK,Пропылесосить комнаты,NEW,тщательно,1\n";
        String actually = subTask.serializeToCsv();
        Assertions.assertEquals(expected, actually);
    }
}