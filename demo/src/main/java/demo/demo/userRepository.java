package demo.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface userRepository extends JpaRepository<user, Long> {
    user findByUsernameAndUserPassword(String username, String userPassword);
    user findByUsernameAndEmail(String username, String email);
    user findByUsername(String username);
    user findByEmail(String email);
}