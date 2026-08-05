package neoinfos.pms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * packageName    : neoinfos.pms.dto
 * fileName       : ProductMasterListProjection
 * author         : JAEIK
 * date           : 8/6/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/6/26        JAEIK       최초 생성
 */
@Getter
@Builder
@AllArgsConstructor
public class ProductMasterListProjection {
    private Long productNo;

    private Long categoryNo;

    private String categoryName;

    private String productCode;

    private String productName;

    private LocalDate productCreated;

    private BigDecimal price;

    private String used;

    private String address;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
