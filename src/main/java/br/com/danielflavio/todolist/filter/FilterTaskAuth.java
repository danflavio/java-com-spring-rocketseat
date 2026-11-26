package br.com.danielflavio.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.danielflavio.todolist.task.ITaskRepository;
import br.com.danielflavio.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    private final IUserRepository userRepository;

    public FilterTaskAuth(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
        var user = this.userRepository.findByUsername(username);

        if (user.isEmpty()) {
            response.sendError(401);
        } else {
            
            var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.get().getPassword());

            if (passwordVerify.verified) {
                filterChain.doFilter(request, response);
            } else {
                response.sendError(401);
            }

        }
        // Validar a senha

        filterChain.doFilter(request, response);
    }
    
}
