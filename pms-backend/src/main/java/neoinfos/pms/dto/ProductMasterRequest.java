package neoinfos.pms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
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
@AllArgsConstructor
public class ProductMasterRequest {
    @NotNull
    private Long categoryNo;

    @NotBlank
    @Size(max = 100)
    private String productCode;

    @NotBlank()
    @Size(max = 100)
    private String productName;

    private LocalDateTime productCreated;

    private BigDecimal price;

    @NotBlank
    @Pattern(regexp = "[YN]")
    private String used;

    private String address;
}