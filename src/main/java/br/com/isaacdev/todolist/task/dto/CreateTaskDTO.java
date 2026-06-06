package br.com.isaacdev.todolist.task.dto;

import br.com.isaacdev.todolist.task.TaskPriority;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record CreateTaskDTO(
    @NotBlank(message = "Descricao e obrigatoria")
    String description,

    @NotBlank(message = "Titulo e obrigatorio")
    @Size(max = 50, message = "O titulo deve ter no máximo 50 caracteres")
    String title,

    @NotNull(message = "Data de inicio e obrigatoria")
    @FutureOrPresent(message = "Data de inicio nao pode estar no passado")
    LocalDateTime startAt,

    @NotNull(message = "Data de termino e obrigatoria")
    @FutureOrPresent(message = "Data de termino nao pode estar no passado")
    LocalDateTime endAt,

    TaskPriority priority
) {
    public CreateTaskDTO {
        if (priority == null) {
            priority = TaskPriority.MEDIA;
        }
    }
}
