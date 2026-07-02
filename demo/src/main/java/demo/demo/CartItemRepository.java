package demo.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(Long userId);
    List<CartItem> findByGameId(Long gameId);
    void deleteByUserIdAndGameId(Long userId, Long gameId);
}