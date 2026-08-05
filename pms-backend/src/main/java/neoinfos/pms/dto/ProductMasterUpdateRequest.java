package neoinfos.pms.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @NotNull
    private Long categoryNo;

    @NotBlank
    @Size(max = 100)
    private String productCode;

    @NotBlank
    @Size(max = 100)
    private String productName;

    @NotBlank
    @Pattern(regexp = "[YN]")
    private String used;

    @PastOrPresent(message = "생산일자는 미래 날짜를 입력할 수 없습니다.")
    private LocalDate productCreated;

    @DecimalMin(value = "0")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;

    private String address;
}
