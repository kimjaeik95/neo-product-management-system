package neoinfos.pms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * packageName    : neoinfos.pms.dto
 * fileName       : CategoryResponse
 * author         : JAEIK
 * date           : 8/3/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/3/26        JAEIK       최초 생성
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private Long categoryNo;

    private String categoryCode;

    private String categoryName;

    private String used;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String deletedYn;

    private LocalDateTime deletedAt;
}



