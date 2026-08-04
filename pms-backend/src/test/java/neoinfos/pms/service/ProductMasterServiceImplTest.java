package neoinfos.pms.service;

import com.sun.jdi.request.DuplicateRequestException;
import neoinfos.pms.common.exception.category.CategoryNotFoundException;
import neoinfos.pms.common.exception.productmaster.DuplicateProductMasterException;
import neoinfos.pms.dto.ProductMasterRequest;
import neoinfos.pms.dto.ProductMasterResponse;
import neoinfos.pms.entity.Category;
import neoinfos.pms.entity.ProductMaster;
import neoinfos.pms.mapper.ProductMasterMapper;
import neoinfos.pms.repository.CategoryRepository;
import neoinfos.pms.repository.ProductMasterRepository;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * packageName    : neoinfos.pms.service
 * fileName       : ProductMasterServiceImplTest
 * author         : JAEIK
 * date           : 8/4/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/4/26        JAEIK       최초 생성
 */
@ExtendWith(MockitoExtension.class)
class ProductMasterServiceImplTest {
    @InjectMocks
    private ProductMasterServiceImpl productMasterService;

    @Mock
    private ProductMasterRepository productMasterRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private ProductMasterMapper productMasterMapper;

    // ProductMaster 생성
    @Test
    @DisplayName("정상적으로 상품을 등록한다")
    void createProductMaster_success() {
        // given
        ProductMasterRequest request = ProductMasterRequest.builder()
                .productCode("P001")
                .categoryNo(1L)
                .build();

        Category category = Category.builder().categoryNo(1L).build();
        ProductMaster entity = ProductMaster.builder().productCode("P001").category(category).build();
        ProductMaster savedEntity = ProductMaster.builder().productCode("P001").category(category).build();
        ProductMasterResponse expected = ProductMasterResponse.builder().productCode("P001").build();

        given(productMasterRepository.existsByProductCode("P001")).willReturn(false);
        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));
        given(productMasterMapper.toEntity(request, category)).willReturn(entity);
        given(productMasterRepository.save(entity)).willReturn(savedEntity);
        given(productMasterMapper.toDto(savedEntity)).willReturn(expected);

        // when
        ProductMasterResponse result = productMasterService.createProductMaster(request);

        // then
        assertThat(result).isEqualTo(expected);
        verify(productMasterRepository).save(entity);
    }
    @Test
    @DisplayName("이미 존재하는 상품코드면 DuplicateProductMasterException 던진다")
    void createProductMaster_duplicateProductCode() {
        ProductMasterRequest request = ProductMasterRequest.builder()
                .productCode("P001").categoryNo(1L).build();

        given(productMasterRepository.existsByProductCode("P001")).willReturn(true);

        assertThatThrownBy(() -> productMasterService.createProductMaster(request))
                .isInstanceOf(DuplicateProductMasterException.class);

        verify(categoryRepository, never()).findById(any());
        verify(productMasterRepository, never()).save(any());
    }

    @Test
    @DisplayName("존재하지 않는 카테고리면 CategoryNotFoundException을 던진다")
    void createProductMaster_categoryNotFound() {
        ProductMasterRequest request = ProductMasterRequest.builder()
                .productCode("P001").categoryNo(999L).build();

        given(productMasterRepository.existsByProductCode("P001")).willReturn(false);
        given(categoryRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> productMasterService.createProductMaster(request))
                .isInstanceOf(CategoryNotFoundException.class);

        verify(productMasterRepository, never()).save(any());
    }
}