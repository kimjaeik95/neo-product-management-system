package neoinfos.pms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import neoinfos.pms.dto.CategoryRequest;
import neoinfos.pms.dto.CategoryResponse;
import neoinfos.pms.dto.CategoryUpdateRequest;
import neoinfos.pms.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * packageName    : neoinfos.pms.controller
 * fileName       : CategoryController
 * author         : JAEIK
 * date           : 8/3/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/3/26        JAEIK       최초 생성
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/categories")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        CategoryResponse category = categoryService.createCategory(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @GetMapping("/categories")
    public ResponseEntity<Page<CategoryResponse>> getCategories(@PageableDefault(size = 20) Pageable pageable) {
        Page<CategoryResponse> pageCategory = categoryService.findAllCategory(pageable);
        return ResponseEntity.ok().body(pageCategory);
    }

    @GetMapping("/categories/{categoryNo}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable("categoryNo") Long categoryNo) {
        CategoryResponse category = categoryService.findCategoryById(categoryNo);
        return ResponseEntity.ok().body(category);
    }

    @PutMapping("/categories/{categoryNo}")
    public ResponseEntity<CategoryResponse> updateCategoryById(@PathVariable("categoryNo") Long categoryNo,@Valid @RequestBody CategoryUpdateRequest updateRequest) {
        CategoryResponse categoryResponse = categoryService.updateCategoryById(categoryNo, updateRequest);
        return ResponseEntity.ok().body(categoryResponse);
    }

    @DeleteMapping("/categories/{categoryNo}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable("categoryNo") Long categoryNo) {
        categoryService.softDeleteCategoryById(categoryNo);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
