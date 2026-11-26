package br.com.danielflavio.todolist.user;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component 
public interface IUserRepository extends JpaRepository<UserModel, UUID> {
    
    Optional<UserModel> findByUsername(String username);

}
