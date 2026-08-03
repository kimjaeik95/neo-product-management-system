package neoinfos.pms.service;

import com.sun.jdi.request.DuplicateRequestException;
import neoinfos.pms.dto.CategoryRequest;
import neoinfos.pms.dto.CategoryResponse;
import neoinfos.pms.dto.CategoryUpdateRequest;
import neoinfos.pms.entity.Category;
import neoinfos.pms.mapper.CategoryMapper;
import neoinfos.pms.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * packageName    : neoinfos.pms.service
 * fileName       : CategoryServiceImplTest
 * author         : JAEIK
 * date           : 8/3/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/3/26        JAEIK       최초 생성
 */
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    // category 생성

    @Test
    @DisplayName("중복된 제품분류코드면 중복 오류발생")
    void createCategory_duplicateCode() {
        // given
        CategoryRequest request = CategoryRequest.builder()
                .categoryCode("CATE001")
                .categoryName("의류")
                .build();

        given(categoryRepository.existsByCategoryCode("CATE001")).willReturn(true);

        // when & then
        assertThatThrownBy(() -> categoryService.createCategory(request))
                .isInstanceOf(DuplicateRequestException.class)
                .hasMessage("중복된 제품분류 코드입니다.");

        // 예외가 발생했을때 이 코드가 실행되지 않았는지??
        verify(categoryMapper, never()).toEntity(any());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("정상 요청이면 저장 후 응답 DTO를 반환한다")
    void createCategory_success() {
        // given
        CategoryRequest request = CategoryRequest.builder()
                .categoryCode("CATE001")
                .categoryName("의류")
                .build();

        Category category = Category.builder()
                .categoryCode("CATE001")
                .categoryName("의류")
                .used("Y")
                .build();

        Category savedCategory = Category.builder()
                .categoryNo(1L)
                .categoryCode("CATE001")
                .categoryName("의류")
                .used("Y")
                .build();

        CategoryResponse expectedResponse = CategoryResponse.builder()
                .categoryNo(1L)
                .categoryCode("CATE001")
                .categoryName("의류")
                .used("Y")
                .build();

        given(categoryRepository.existsByCategoryCode("CATE001")).willReturn(false);
        given(categoryMapper.toEntity(request)).willReturn(category);
        given(categoryRepository.save(category)).willReturn(savedCategory);
        given(categoryMapper.toDto(savedCategory)).willReturn(expectedResponse);

        // when
        CategoryResponse result = categoryService.createCategory(request);

        // then
        assertThat(result.getCategoryCode()).isEqualTo("CATE001");
        assertThat(result.getCategoryNo()).isEqualTo(1L);
        verify(categoryRepository).save(category);
    }

    // category 전체 조회

    @Test
    @DisplayName("제품분류 목록을 반환한다")
    void findAllCategory_success() {
        // given
        Category category1 = Category.builder().categoryCode("CATE001").categoryName("의류").build();
        Category category2 = Category.builder().categoryCode("CATE002").categoryName("잡화").build();

        CategoryResponse response1 = CategoryResponse.builder().categoryCode("CATE001").categoryName("의류").build();
        CategoryResponse response2 = CategoryResponse.builder().categoryCode("CATE002").categoryName("잡화").build();

        given(categoryRepository.findAll()).willReturn(List.of(category1, category2));
        given(categoryMapper.toDto(category1)).willReturn(response1);
        given(categoryMapper.toDto(category2)).willReturn(response2);

        // when
        List<CategoryResponse> result = categoryService.findAllCategory();

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("categoryCode").containsExactly("CATE001", "CATE002");
    }

    // category 단건 조회

    @Test
    @DisplayName("categoryNo로 제품분류 단건 조회하여 반환한다")
    void findCategoryById_success() {
        // given
        Long categoryNo = 1L;
        Category category = Category.builder().categoryNo(categoryNo).categoryCode("CATE001").categoryName("의류").build();
        CategoryResponse response = CategoryResponse.builder().categoryNo(categoryNo).categoryCode("CATE001").categoryName("의류").build();

        given(categoryRepository.findById(categoryNo)).willReturn(Optional.of(category));
        given(categoryMapper.toDto(category)).willReturn(response);

        // when
        CategoryResponse result = categoryService.findCategoryById(categoryNo);

        // then
        assertThat(result.getCategoryNo()).isEqualTo(categoryNo);
        assertThat(result.getCategoryCode()).isEqualTo("CATE001");
    }

    @Test
    @DisplayName("존재하지 않는 categoryNo면 예외를 던진다")
    void findCategoryById_notFound() {
        // given
        Long categoryNo = 999L;
        given(categoryRepository.findById(categoryNo)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> categoryService.findCategoryById(categoryNo))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("제품분류가 존재하지 않습니다 :" + categoryNo);

        verify(categoryMapper, never()).toDto(any());
    }

    // Update Category

    @Test
    @DisplayName("정상적으로 카테고리를 수정한다")
    void updateCategoryById_success() {
        // given
        Long categoryNo = 1L;
        Category category = Category.builder()
                .categoryNo(categoryNo).categoryCode("CATE001").categoryName("의류").used("Y").build();

        CategoryUpdateRequest updateRequest = new CategoryUpdateRequest("CATE002", "잡화", "N");

        CategoryResponse response = CategoryResponse.builder()
                .categoryNo(categoryNo).categoryCode("CATE002").categoryName("잡화").used("N").build();

        given(categoryRepository.findById(categoryNo)).willReturn(Optional.of(category));
        given(categoryRepository.existsByCategoryCode("CATE002")).willReturn(false);
        given(categoryMapper.toDto(category)).willReturn(response);

        // when
        CategoryResponse result = categoryService.updateCategoryById(categoryNo, updateRequest);

        // then
        assertThat(result.getCategoryCode()).isEqualTo("CATE002");
        assertThat(result.getUsed()).isEqualTo("N");
    }

    @Test
    @DisplayName("다른 카테고리가 이미 쓰고 있는 코드로 변경하면 예외를 던진다")
    void updateCategoryById_duplicateCode() {
        // given
        Long categoryNo = 1L;
        Category category = Category.builder()
                .categoryNo(categoryNo).categoryCode("CATE001").categoryName("의류").used("Y").build();

        CategoryUpdateRequest updateRequest = new CategoryUpdateRequest("CATE999", "의류", "Y");

        given(categoryRepository.findById(categoryNo)).willReturn(Optional.of(category));
        given(categoryRepository.existsByCategoryCode("CATE999")).willReturn(true);

        // when & then
        assertThatThrownBy(() -> categoryService.updateCategoryById(categoryNo, updateRequest))
                .isInstanceOf(DuplicateRequestException.class);
    }

    @Test
    @DisplayName("자기 자신의 코드로 수정 요청하면 중복 체크를 통과한다")
    void updateCategoryById_sameCodeNoDuplicateCheck() {
        // given
        Long categoryNo = 1L;
        Category category = Category.builder()
                .categoryNo(categoryNo).categoryCode("CATE001").categoryName("의류").used("Y").build();

        CategoryUpdateRequest updateRequest = new CategoryUpdateRequest("CATE001", "잡화", "N");

        given(categoryRepository.findById(categoryNo)).willReturn(Optional.of(category));
        given(categoryMapper.toDto(category)).willReturn(CategoryResponse.builder().categoryCode("CATE001").build());

        // when
        categoryService.updateCategoryById(categoryNo, updateRequest);

        // then: 코드가 안 바뀌었으니 existsByCategoryCode가 호출되지 않아야 함
        verify(categoryRepository, never()).existsByCategoryCode(any());
    }

    // 제품분류 삭제
    @Test
    @DisplayName("정상적으로 소프트 딜리트 처리된다")
    void softDeleteCategoryById_success() {
        // given
        Long categoryNo = 1L;
        Category category = Category.builder()
                .categoryNo(categoryNo).categoryCode("CATE001").categoryName("의류").used("Y").build();

        given(categoryRepository.findById(categoryNo)).willReturn(Optional.of(category));

        // when
        categoryService.softDeleteCategoryById(categoryNo);

        // then
        assertThat(category.getDeletedYn()).isEqualTo("Y");
        assertThat(category.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 categoryNo면 예외를 던진다")
    void softDeleteCategoryById_notFound() {
        // given
        Long categoryNo = 999L;
        given(categoryRepository.findById(categoryNo)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> categoryService.softDeleteCategoryById(categoryNo))
                .isInstanceOf(NoSuchElementException.class);
    }
}

