package br.com.esdras.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;


 /* 
    *
    *ID
    *Usuario(ID_Usuario)
    *Descricao
    *titulo
    *data de incio
    *data de termino
    *prioridade
    *
    */


@Data
// o nome da entidade sera tb_task
@Entity(name="tb_task")
public class TaskModel {

    //chave primaria
    @Id
    //gerar o Id automaticamente
    @GeneratedValue(generator = "UUID")

    
    //identificacap
    private UUID id;
    //identificacao
    private String description;
    @Column(length = 50)
    //titulo da tarefa
    private String title;
    //data de incio
    private LocalDateTime startAT;
    //data de fim
    private LocalDateTime endAT;
    //prioridade
    private String priority;

    //asociacao do usuario com a tarefa
    private UUID idUser;

    //
    @CreationTimestamp
    //informar quando a tarefa for criada
    private LocalDateTime createdAT;

    //throws exception é passsado do if para a camada mais a cima
    public void setTitle(String title) throws Exception{
        if(title.length()>50) {
            //O exception e a excessao mais generica do java, em que ha dois tipos de exececoes
            //Trataveis: erro de dados de validacao
            //Nao tratveis: sistema fora do ar
            //Essa exception e um excessao tratavel, dentro do set title
            throw new Exception("O campo title deve conter no máximo 50 caracteres");
        }
        this.title = title;
    }
    
}
