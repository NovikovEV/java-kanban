package dto;

import model.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

public record TaskDto(
        String taskName,
        String description,
        TaskStatus taskStatus,
        Duration duration,
        LocalDateTime startTime
) {
}
