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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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

    // ProductMaster 전체조회
    @DisplayName("등록된 상품이 있으면 페이징된 전체 목록을 반환한다")
    @Test
    void findAllProductMaster_success() {
        // given
        Category category = Category.builder()
                .categoryNo(1L)
                .categoryCode("CAFE001")
                .categoryName("전자기기")
                .build();

        ProductMaster product1 = ProductMaster.builder()
                .category(category)
                .productCode("P001")
                .productName("노트북")
                .productCreated(LocalDate.of(2026, 8, 1))
                .price(new BigDecimal("1000000"))
                .address("서울")
                .build();

        ProductMaster product2 = ProductMaster.builder()
                .category(category)
                .productCode("P002")
                .productName("마우스")
                .productCreated(LocalDate.of(2026, 8, 2))
                .price(new BigDecimal("50000"))
                .address("경기")
                .build();


        Pageable pageable = PageRequest.of(0, 10);

        Page<ProductMaster> productPage = new PageImpl<>(
                List.of(product1, product2),
                pageable,
                2
        );


        ProductMasterResponse response1 = ProductMasterResponse.builder()
                .productCode("P001")
                .productName("노트북")
                .build();

        ProductMasterResponse response2 = ProductMasterResponse.builder()
                .productCode("P002")
                .productName("마우스")
                .build();


        given(productMasterRepository.findAll(pageable)).willReturn(productPage);
        given(productMasterMapper.toDto(product1)).willReturn(response1);
        given(productMasterMapper.toDto(product2)).willReturn(response2);


        // when
        Page<ProductMasterResponse> result = productMasterService.findALLProductMasters(pageable);


        // then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(10);
        verify(productMasterRepository).findAll(pageable);
    }
}