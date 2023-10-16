package br.com.esdras.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.esdras.todolist.user.UserRepository;
// o servelet é a base para qualquer framework para web, no java
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//O component serve para indicar ao spring para gerenciar a classe desejada, sendo o componente a classe mais generica do spring
@Component

public class FilterTaskAuth extends OncePerRequestFilter {
   @Autowired
   private UserRepository userRepository;

   @Override
   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
         throws ServletException, IOException {

      var serveletPath = request.getServletPath();
      System.out.println("PATH: " + serveletPath);

      // Esse if esta sendo usado para se entrar nas task,se nao for a rota de task,
      // entao vamos pular
      // ficar com o servelet path
      if (serveletPath.startsWith("/tasks/")) {
         // pegar a autenticacao(usuario e senha)
         var authorization = request.getHeader("Authorization");

         var authEncoded = authorization.substring("Basic".length()).trim();

         byte[] authDecode = Base64.getDecoder().decode(authEncoded);

         var authString = new String(authDecode);

         // ["Esdronio", 1234]
         String[] credentials = authString.split(":");
         String username = credentials[0];
         String password = credentials[1];

         // validadr usuario
         var user = this.userRepository.findByUsername(username);
         if (user == null)
            response.sendError(401);
         else {
            // validar senha
            var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
            if (passwordVerify.verified) {
               request.setAttribute("idUser", user.getId());
               // dentro do request ou response, deve ser inserida a informacao para o
               // controler recuperar
               // request: o que esta vindo da requisicao
               // response: o que esta enviando para o usuario
               filterChain.doFilter(request, response);
            } else {
               response.sendError(401);
            }

         }
      } else {
         filterChain.doFilter(request, response);
      }
      // segue viagem
   }

}
