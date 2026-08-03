package neoinfos.pms.mapper;

import neoinfos.pms.dto.CategoryRequest;
import neoinfos.pms.dto.CategoryResponse;
import neoinfos.pms.entity.Category;
import org.springframework.stereotype.Component;

/**
 * packageName    : neoinfos.pms.mapper
 * fileName       : categoryMapper
 * author         : JAEIK
 * date           : 8/3/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/3/26        JAEIK       최초 생성
 */
@Component
public class CategoryMapper {
    public Category toEntity(CategoryRequest request) {
        return Category.builder()
                .categoryCode(request.getCategoryCode())
                .categoryName(request.getCategoryName())
                .used("Y")
                .build();
    }

    public CategoryResponse toDto(Category category) {
        return CategoryResponse.builder()
                .categoryNo(category.getCategoryNo())
                .categoryCode(category.getCategoryCode())
                .categoryName(category.getCategoryName())
                .used(category.getUsed())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .deletedYn(category.getDeletedYn())
                .deletedAt(category.getDeletedAt())
                .build();
    }
}
