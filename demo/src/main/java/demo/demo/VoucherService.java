package demo.demo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoucherService {

    @Autowired
    VoucherRepository voucherRepo;

    // Called when a new user registers — gives them 3 welcome vouchers
    public void createWelcomeVouchers(Long userId) {
        int[] discounts = {5, 10, 20};
        for (int discount : discounts) {
            String code = "WELCOME" + discount + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            Voucher voucher = new Voucher(userId, code, discount);
            voucherRepo.save(voucher);
        }
    }

    // Returns all unused vouchers for a user.
    // If the user has NEVER had any vouchers at all (e.g. they signed up
    // before welcome vouchers existed, or signup failed to grant them),
    // backfill the 3 welcome vouchers now so they aren't stuck with none.
    public List<Voucher> getUnusedVouchers(Long userId) {
        List<Voucher> all = voucherRepo.findByUserId(userId);

        if (all.isEmpty()) {
            createWelcomeVouchers(userId);
            all = voucherRepo.findByUserId(userId);
        }

        return all.stream()
            .filter(v -> !v.isUsed())
            .toList();
    }

    // Applies a voucher code — returns discount percent, or 0 if invalid
    public int applyVoucher(Long userId, String code) {
        Optional<Voucher> voucher = voucherRepo.findByUserIdAndCodeAndUsedFalse(userId, code);
        return voucher.map(Voucher::getDiscountPercent).orElse(0);
    }

    // Marks a voucher as used after checkout
    public void markUsed(Long userId, String code) {
        voucherRepo.findByUserIdAndCodeAndUsedFalse(userId, code).ifPresent(v -> {
            v.setUsed(true);
            voucherRepo.save(v);
        });
    }

    // Friendly display label for a voucher, used in the cart dropdown.
    // e.g. "5% Off New User Discount", "20% Off New User Discount"
    public String getDisplayLabel(Voucher voucher) {
        if (voucher.getCode() != null && voucher.getCode().startsWith("WELCOME")) {
            return voucher.getDiscountPercent() + "% OFF New User Discount";
        }
        return voucher.getDiscountPercent() + "% OFF Voucher";
    }
}