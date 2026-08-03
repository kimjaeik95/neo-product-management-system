package neoinfos.pms.service;

import com.sun.jdi.request.DuplicateRequestException;
import neoinfos.pms.dto.CategoryRequest;
import neoinfos.pms.dto.CategoryResponse;
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

    @Test
    @DisplayName("카테고리 목록을 반환한다")
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
}