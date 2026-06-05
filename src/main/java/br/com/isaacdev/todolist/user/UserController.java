package br.com.isaacdev.todolist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.isaacdev.todolist.user.Dto.CreateUserDTO;
import br.com.isaacdev.todolist.user.Dto.UserResponseDTO;
import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("")
    public ResponseEntity<Object> create(
            @Valid @RequestBody CreateUserDTO body
    ) {
        UserModel user = this.userRepository.findByUsername(body.username());
        if (user != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of("message", "Usuario já existe")
            );
        }

        UserModel userModel = new UserModel();
        userModel.setUsername(body.username());
        userModel.setName(body.name());
        var passwordHash = BCrypt.withDefaults().hashToString(
                12,
                body.password().toCharArray()
        );
        userModel.setPassword(passwordHash);
        var userCreated = this.userRepository.save(userModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    @GetMapping("")
    public ResponseEntity<Object> list() {
        var users = this.userRepository.findAll().stream().map(user -> new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUsername()
        )).toList();
        return ResponseEntity.ok(users);
    }
}
