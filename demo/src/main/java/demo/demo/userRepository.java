package demo.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface userRepository extends JpaRepository<user, Long> {
    user findByUsernameAndUserPassword(String username, String password);
    user findByUsernameAndEmail(String username, String email);
}   


