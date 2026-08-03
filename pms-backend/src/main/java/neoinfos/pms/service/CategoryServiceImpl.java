package neoinfos.pms.service;

import lombok.RequiredArgsConstructor;
import neoinfos.pms.dto.CategoryRequest;
import neoinfos.pms.dto.CategoryResponse;
import neoinfos.pms.dto.ProductMasterRequest;
import neoinfos.pms.entity.Category;
import neoinfos.pms.mapper.CategoryMapper;
import neoinfos.pms.repository.CategoryRepository;
import org.springframework.stereotype.Service;

/**
 * packageName    : neoinfos.pms.service
 * fileName       : ProductMasterService
 * author         : JAEIK
 * date           : 8/3/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/3/26        JAEIK       최초 생성
 */
@Service
@RequiredArgsConstructor
public class ProductMasterServiceImpl implements ProductMasterService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createProduct(CategoryRequest categoryRequest) {
        Category category = categoryMapper.toEntity(categoryRequest);
        categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }
}
