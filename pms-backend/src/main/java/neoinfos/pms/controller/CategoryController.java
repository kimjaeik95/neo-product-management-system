package neoinfos.pms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import neoinfos.pms.dto.CategoryRequest;
import neoinfos.pms.dto.CategoryResponse;
import neoinfos.pms.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/category")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        CategoryResponse category = categoryService.createCategory(categoryRequest);
        return ResponseEntity.ok().body(category);
    }
}
