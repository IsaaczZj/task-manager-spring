package br.com.isaacdev.todolist.user.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDTO(
        @NotBlank(message = "Username é obrigatório")
        String username,

        @NotBlank(message = "Nome é obrigatório") String name,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "A senha precisa ter no mínimo 6 caracteres")
        String password
) {
}
