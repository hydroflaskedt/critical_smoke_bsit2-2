package demo.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vouchers")
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voucherId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "code")
    private String code;

    @Column(name = "discount_percent")
    private Integer discountPercent;

    @Column(name = "used")
    private boolean used = false;

    public Voucher() {}

    public Voucher(Long userId, String code, Integer discountPercent) {
        this.userId = userId;
        this.code = code;
        this.discountPercent = discountPercent;
    }

    public Long getVoucherId() { return voucherId; }
    public Long getUserId() { return userId; }
    public String getCode() { return code; }
    public Integer getDiscountPercent() { return discountPercent; }
    public boolean isUsed() { return used; }

    public void setVoucherId(Long voucherId) { this.voucherId = voucherId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setCode(String code) { this.code = code; }
    public void setDiscountPercent(Integer discountPercent) { this.discountPercent = discountPercent; }
    public void setUsed(boolean used) { this.used = used; }
}