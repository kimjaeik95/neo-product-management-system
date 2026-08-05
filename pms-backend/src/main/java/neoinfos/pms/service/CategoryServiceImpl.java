package neoinfos.pms.service;

import lombok.RequiredArgsConstructor;
import neoinfos.pms.common.exception.category.CategoryNotFoundException;
import neoinfos.pms.common.exception.category.CategoryUsedException;
import neoinfos.pms.common.exception.category.DuplicateCategoryCodeException;
import neoinfos.pms.dto.CategoryRequest;
import neoinfos.pms.dto.CategoryResponse;
import neoinfos.pms.dto.CategoryUpdateRequest;
import neoinfos.pms.entity.Category;
import neoinfos.pms.mapper.CategoryMapper;
import neoinfos.pms.repository.CategoryRepository;
import neoinfos.pms.repository.ProductMasterRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ProductMasterRepository productMasterRepository;

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        if (categoryRepository.existsByCategoryCode(categoryRequest.getCategoryCode())) {
            throw new DuplicateCategoryCodeException();
        }

        Category category = categoryMapper.toEntity(categoryRequest);

        Category saveCategory = categoryRepository.save(category);

        return categoryMapper.toDto(saveCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponse> findAllCategory(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse findCategoryById(Long categoryNo) {
        Category category = categoryRepository.findById(categoryNo)
                .orElseThrow(() -> new CategoryNotFoundException(categoryNo));

        return categoryMapper.toDto(category);

    }

    @Override
    @Transactional
    public CategoryResponse updateCategoryById(Long categoryNo, CategoryUpdateRequest updateRequest) {
        Category category = categoryRepository.findById(categoryNo)
                .orElseThrow(() -> new CategoryNotFoundException(categoryNo));

        if (!category.getCategoryCode().equals(updateRequest.getCategoryCode())
                && categoryRepository.existsByCategoryCode(updateRequest.getCategoryCode())) {
            throw new DuplicateCategoryCodeException();
        }

        category.updateCategory(updateRequest);

        categoryRepository.save(category);

        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public void softDeleteCategoryById(Long categoryNo) {
        Category category = categoryRepository.findById(categoryNo)
                .orElseThrow(() -> new CategoryNotFoundException(categoryNo));

        if (productMasterRepository.existsByCategory_CategoryNo(categoryNo)) {
            throw new CategoryUsedException();
        }

        category.softDelete();
    }
}
