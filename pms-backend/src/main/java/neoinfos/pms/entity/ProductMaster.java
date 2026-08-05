package neoinfos.pms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import neoinfos.pms.dto.ProductMasterUpdateRequest;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * packageName    : neoinfos.pms.entity
 * fileName       : ProductMaster
 * author         : JAEIK
 * date           : 8/3/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/3/26        JAEIK       최초 생성
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@SQLRestriction("deleted_yn = 'N'")
@Table(name = "product_master")
public class ProductMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_no")
    private Long productNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_no")
    private Category category;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_created")
    private LocalDate productCreated;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "used")
    private String used;

    @Column(name = "address")
    private String address;

    @Column(
            name = "created_at",
            insertable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "updated_at",
            insertable = false,
            updatable = false
    )
    private LocalDateTime updatedAt;

    @Column(
            name = "deleted_yn",
            insertable = false
    )
    private String deletedYn;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public void updateProductMaster(ProductMasterUpdateRequest updateRequest, Category category) {
        this.category = category;
        this.productCode = updateRequest.getProductCode();
        this.productName = updateRequest.getProductName();
        this.productCreated = updateRequest.getProductCreated();
        this.price = updateRequest.getPrice();
        this.used = updateRequest.getUsed();
        this.address = updateRequest.getAddress();
    }

    public void softDelete() {
        this.deletedYn = "Y";
        this.deletedAt = LocalDateTime.now();
    }
}
