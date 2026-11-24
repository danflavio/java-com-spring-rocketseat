package br.com.danielflavio.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Pegar Autenticação do usuário
        var authorization = request.getHeader("Authorization");
        System.out.println("Authorization: " + authorization);
        
        var passwordHashed = authorization.substring("Basic".length()).trim();
        byte[] authDecode = Base64.getDecoder().decode(passwordHashed);
        var authDecodeString = new String(authDecode);
        
        String[] credentials = authDecodeString.split(":");
        String username = credentials[0];
        String password = credentials[1];
        
        System.out.println("passwordHashed: " + passwordHashed);
        System.out.println("authDecode: " +authDecodeString);
        System.out.println("username: " + username);
        System.out.println("password: " + password);
                

        // Validar se o usuário tem permissão para acessar a rota
        // Validar a senha

        filterChain.doFilter(request, response);
    }
    
}
