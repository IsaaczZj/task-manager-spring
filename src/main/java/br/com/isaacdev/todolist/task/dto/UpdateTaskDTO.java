package br.com.isaacdev.todolist.task.dto;

import br.com.isaacdev.todolist.task.TaskPriority;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record UpdateTaskDTO(

        String description,


        @Size(max = 50, message = "O titulo deve ter no maximo 50 caracteres")
        String title,
        @FutureOrPresent(message = "Data de inicio nao pode estar no passado")
        LocalDateTime startAt,


        @FutureOrPresent(message = "Data de termino nao pode estar no passado")
        LocalDateTime endAt,

        TaskPriority priority

) {
}
