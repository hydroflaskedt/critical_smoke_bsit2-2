package demo.demo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    List<Voucher> findByUserId(Long userId);
    Optional<Voucher> findByUserIdAndCodeAndUsedFalse(Long userId, String code);
}