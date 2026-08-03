package neoinfos.pms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

/**
 * packageName    : neoinfos.pms.entity
 * fileName       : Category
 * author         : JAEIK
 * date           : 8/3/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/3/26        JAEIK       최초 생성
 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLRestriction("deleted_yn = 'N'")
@Table(name = "category")
public class Category {

    @Id
    @Column(name = "category_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryNo;

    @Column(name = "category_code")
    private String categoryCode;


    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "used")
    private String used;

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

    public void updateCategory(String categoryCode, String categoryName, String used) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.used = used;
    }

    public void softDelete() {
        this.deletedYn = "Y";
        this.deletedAt = LocalDateTime.now();
    }
}
