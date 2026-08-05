package neoinfos.pms.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * packageName    : neoinfos.pms.dto
 * fileName       : ProductMasterUpdateRequest
 * author         : JAEIK
 * date           : 8/4/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/4/26        JAEIK       최초 생성
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductMasterUpdateRequest {

    @NotBlank
    @Size(max = 100)
    private String productCode;

    @NotBlank
    @Size(max = 100)
    private String productName;

    @NotBlank
    @Pattern(regexp = "[YN]")
    private String used;

    private LocalDateTime productCreated;

    @DecimalMin(value = "0")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;

    private String address;
}
