package br.com.isaacdev.todolist.task;

import br.com.isaacdev.todolist.task.dto.CreateTaskDTO;
import br.com.isaacdev.todolist.task.dto.UpdateTaskDTO;
import br.com.isaacdev.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("")
    public ResponseEntity<Object> create(
        @Valid @RequestBody CreateTaskDTO body,
        HttpServletRequest request
    ) {
        if (this.taskRepository.existsByTitle(body.title())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("message", "Essa tarefa já existe")
            );
        }

        if (body.endAt().isBefore(body.startAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of(
                    "message",
                    "A data de termino deve ser depois da data de inicio"
                )
            );
        }

        TaskModel taskModel = new TaskModel();
        taskModel.setDescription(body.description());
        taskModel.setTitle(body.title());
        taskModel.setStartAt(body.startAt());
        taskModel.setEndAt(body.endAt());
        taskModel.setPriority(body.priority());
        var userId = request.getAttribute("userId");
        taskModel.setUserId((UUID) userId);

        var taskCreated = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskCreated);
    }

    @GetMapping("")
    public ResponseEntity<Object> list(HttpServletRequest request) {
        var userId = request.getAttribute("userId");

        var tasks = this.taskRepository.findByUserId((UUID) userId);
        return ResponseEntity.ok(tasks);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(
        HttpServletRequest request,
        @Valid @RequestBody UpdateTaskDTO body,
        @PathVariable UUID id
    ) {
        var userId = (UUID) request.getAttribute("userId");

        var task = this.taskRepository.findById(id);

        if (task.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("message", "Tarefa não encontrada")
            );
        }
        var taskToUpdated = task.get();

        if (!taskToUpdated.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                Map.of("message", "Voce não tem permissão")
            );
        }
        Utils.copyNonNullProperties(body, taskToUpdated);
        var taskUpdated = this.taskRepository.save(taskToUpdated);
        return ResponseEntity.ok(taskUpdated);
    }
}
