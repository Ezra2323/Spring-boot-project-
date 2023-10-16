package br.com.esdras.todolist.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;


@RestController
@RequestMapping("/users")

public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel){

        var user = this.userRepository.findByUsername(userModel.getUsername());

       //Usado para indicar quando o mesmo username foi digitado mais de uma vez
        if (user!= null) {
            System.out.println("Usuário já existe");
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe");
        }

        //criptografia de dados, nesse caso da senha
       var passwordHashred = BCrypt.withDefaults()
              .hashToString(12, userModel.getPassword().toCharArray());
        //inserindo o codigo hash na senha
        userModel.setPassword(passwordHashred);


        var userCreat = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.OK).body(userCreat);
    }
    
}
