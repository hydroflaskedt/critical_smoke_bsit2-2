package demo.demo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
public interface GameRepository extends JpaRepository<Game, Long> {
List<Game> findByOwnerId(Long ownerId);
List<Game> findByGenre(String genre);
Game findByGameId(Long gameId);
List<Game> findByApprovedFalse();
List<Game> findByApprovedTrue();
List<Game> findByOwnerIdAndApprovedTrue(Long ownerId);
List<Game> findByApprovedFalseAndDeletedFalse();
List<Game> findByApprovedTrueAndDeletedFalse();
List<Game> findByApprovedFalseAndDeletedTrue();
List<Game> findByApprovedTrueAndDeletedTrue();
List<Game> findByUnderReviewTrue();
}