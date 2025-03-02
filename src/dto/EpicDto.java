package dto;

public record EpicDto(
        String taskName,
        String description
) {
    @Override
    public String taskName() {
        return taskName;
    }

    @Override
    public String description() {
        return description;
    }
}
