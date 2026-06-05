package br.com.isaacdev.todolist.task;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TaskPriority {
    BAIXA,
    MEDIA,
    ALTA;

    @JsonCreator
    public static TaskPriority fromValue(String value) {
        if (value == null || value.isBlank()) {
            return MEDIA;
        }

        return TaskPriority.valueOf(value.trim().toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return name().toLowerCase();
    }
}
