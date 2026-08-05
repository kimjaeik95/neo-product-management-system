package neoinfos.pms.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import neoinfos.pms.entity.Category;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * packageName    : neoinfos.pms.service
 * fileName       : ProductMasterRequest
 * author         : JAEIK
 * date           : 8/3/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/3/26        JAEIK       최초 생성
 */
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProductMasterRequest {

    @NotNull
    private Long categoryNo;

    @NotBlank
    @Size(max = 100)
    private String productCode;

    @NotBlank
    @Size(max = 100)
    private String productName;

    private LocalDate productCreated;

    @DecimalMin(value = "0")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;

    private String address;
}