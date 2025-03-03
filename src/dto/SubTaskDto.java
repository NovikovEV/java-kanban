package dto;

import model.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

public record SubTaskDto(
        int epicId,
        String taskName,
        String description,
        TaskStatus taskStatus,
        Duration duration,
        LocalDateTime startTime
) {
}
