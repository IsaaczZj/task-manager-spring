package br.com.isaacdev.todolist.task;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskModel, UUID> {
    boolean existsByTitle(String title);

    List<TaskModel> findByUserId(UUID userId);


}
