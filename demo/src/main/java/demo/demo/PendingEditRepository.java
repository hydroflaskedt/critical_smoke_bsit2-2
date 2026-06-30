package demo.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingEditRepository extends JpaRepository<PendingEdit, Long> {
    List<PendingEdit> findByOwnerId(Long ownerId);
    PendingEdit findByGameId(Long gameId);
}