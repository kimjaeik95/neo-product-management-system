package neoinfos.pms.service;

import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import neoinfos.pms.dto.CategoryRequest;
import neoinfos.pms.dto.CategoryResponse;
import neoinfos.pms.entity.Category;
import neoinfos.pms.mapper.CategoryMapper;
import neoinfos.pms.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        if (categoryRepository.existsByCategoryCode(categoryRequest.getCategoryCode())) {
            throw new DuplicateRequestException("중복된 제품분류 코드입니다.");
        }

        Category category = categoryMapper.toEntity(categoryRequest);

        Category saveCategory = categoryRepository.save(category);

        return categoryMapper.toDto(saveCategory);
    }

    @Override
    public List<CategoryResponse> findAllCategory() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }
}
