package br.com.esdras.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.esdras.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    // serve para administrar a instancia do repositori
    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/")
    // O RequesteBody serve para obter as informações de onde estão vindo os dados
    // HttpServletRequest serve como mencao da pasta FilterTask
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        var usuario = request.getAttribute("idUser");
        taskModel.setIdUser((UUID) usuario);

        // variavel para pegar a data atual
        var currentDate = LocalDateTime.now();
        // se a data de incio ou a data final for menor que a data atual, acontecera um
        // erro
        if (currentDate.isAfter(taskModel.getStartAT()) || currentDate.isAfter(taskModel.getEndAT())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data está errada");
        }
        // Se a data de inicio for maior do que a da data atual ira dar erro
        if (taskModel.getStartAT().isAfter(taskModel.getEndAT())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de inicio deve ser menor do que a data de término");
        }

        var tarefa = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(tarefa);
    }

    @GetMapping("/")
    // lista de tarefas baseado nas credenciais dele
    public List<TaskModel> list(HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        // esse findByUser vai ser diretamente usado para especificar o que vai ser
        // encontrado, e o que vai ser encontrado está na linha 28
        var tasks = this.taskRepository.findByIdUser((UUID) idUser);
        return tasks;
    }

    // funcionalidade do update da tarefa onde ele quiser
    // http://localhost:8080/tasks/(variavel do path)->89556656-cascsafas-89464654
    // variaveldo path: faz parte da rota, Ele especifica os diretórios a serem
    // pesquisados para encontrar um comando.
    // ("/{id}")-> spring boot irá pesquisar o id do usuario no @Path Variable
    @PutMapping("/{id}")
    //update tera validacao para o usuario
    public ResponseEntity update(@RequestBody TaskModel taskModel,  @PathVariable UUID id, HttpServletRequest request) {

        var task = this.taskRepository.findById(id).orElse(null);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                          .body("Tarefa não encontrada");
        }

        var idUser = request.getAttribute("idUser");
        if(!task.getIdUser().equals(idUser)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                          .body("Usuário não tem permissão para alterar essa tarefa");

        }
        Utils.copyNonNullProperties(taskModel, task);

        var taskup = this.taskRepository.save(task); 

        return ResponseEntity.ok().body( this.taskRepository.save(taskup));

    }

}
