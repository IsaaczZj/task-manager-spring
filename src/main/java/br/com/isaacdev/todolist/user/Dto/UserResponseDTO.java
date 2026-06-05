package br.com.isaacdev.todolist.user.Dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String username,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String name

) {
}
