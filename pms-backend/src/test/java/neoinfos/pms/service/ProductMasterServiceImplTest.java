package neoinfos.pms.service;

import neoinfos.pms.common.exception.category.CategoryNotFoundException;
import neoinfos.pms.common.exception.productmaster.DuplicateProductMasterException;
import neoinfos.pms.common.exception.productmaster.ProductMasterNotFoundException;
import neoinfos.pms.dto.ProductMasterListProjection;
import neoinfos.pms.dto.ProductMasterRequest;
import neoinfos.pms.dto.ProductMasterResponse;
import neoinfos.pms.dto.ProductMasterUpdateRequest;
import neoinfos.pms.entity.Category;
import neoinfos.pms.entity.ProductMaster;
import neoinfos.pms.mapper.ProductMasterMapper;
import neoinfos.pms.repository.CategoryRepository;
import neoinfos.pms.repository.ProductMasterRepository;
import org.junit.jupiter.api.BeforeEach;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
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
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductMasterMapper productMasterMapper;

    private ProductMaster productMaster;
    private Category category;
    private ProductMasterUpdateRequest updateRequest;

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

        ProductMasterListProjection product1 = ProductMasterListProjection.builder()
                .productCode("P001")
                .productName("노트북")
                .productCreated(LocalDate.of(2026, 8, 1))
                .price(new BigDecimal("1000000"))
                .address("서울")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        ProductMasterListProjection product2 = ProductMasterListProjection.builder()
                .productCode("P002")
                .productName("마우스")
                .productCreated(LocalDate.of(2026, 8, 2))
                .price(new BigDecimal("50000"))
                .address("경기")
                .build();


        Pageable pageable = PageRequest.of(0, 10);

        Page<ProductMasterListProjection> productPage = new PageImpl<>(
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


        given(productMasterRepository.findProductList(pageable)).willReturn(productPage);

        // when
        Page<ProductMasterListProjection> result = productMasterService.findALLProductMasters(pageable);


        // then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(10);
        verify(productMasterRepository).findProductList(pageable);
    }

    // ProductMaster 단건 조회
    @Test
    @DisplayName("존재하는 productNo로 조회하면 상품 정보를 반환한다")
    void findProductMasterById_success() {
        // given
        Long productNo = 1L;

        Category category = Category.builder()
                .categoryNo(10L)
                .categoryName("전자기기")
                .build();

        ProductMaster productMaster = ProductMaster.builder()
                .productNo(productNo)
                .category(category)
                .productCode("P001")
                .productName("키보드")
                .productCreated(LocalDate.of(2026, 8, 1))
                .price(new BigDecimal("100000"))
                .address("서울")
                .used("Y")
                .build();

        ProductMasterResponse expected = ProductMasterResponse.builder()
                .productNo(productNo)
                .productCode("P001")
                .build();

        given(productMasterRepository.findById(productNo))
                .willReturn(Optional.of(productMaster));

        given(productMasterMapper.toDto(productMaster))
                .willReturn(expected);

        // when
        ProductMasterResponse result =
                productMasterService.findProductMasterById(productNo);

        // then
        assertThat(result).isEqualTo(expected);

        verify(productMasterRepository).findById(productNo);
        verify(productMasterMapper).toDto(productMaster);
    }

    @Test
    @DisplayName("존재하지 않는 productMasterNo로 조회하면 ProductMasterNotFoundException을 던진다")
    void findProductMasterById_notFound() {
        // given
        Long productMasterNo = 999L;
        given(productMasterRepository.findById(productMasterNo)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> productMasterService.findProductMasterById(productMasterNo))
                .isInstanceOf(ProductMasterNotFoundException.class);

        verify(productMasterMapper, never()).toDto(any());
    }

    // ProductMaster 업데이트
    @BeforeEach
    void setUp() {
        category = Category.builder().categoryNo(1L).build();
        productMaster = ProductMaster.builder()
                .productNo(100L)
                .productCode("OLD-CODE")
                .category(category)
                .build();

        updateRequest = ProductMasterUpdateRequest.builder()
                .categoryNo(1L)
                .productCode("NEW-CODE")
                .productName("변경된 상품명")
                .productCreated(LocalDate.now())
                .price(BigDecimal.valueOf(1000))
                .used("N")
                .address("서울시 강남구")
                .build();
    }

    @Test
    @DisplayName("정상적으로 상품 정보를 수정한다")
    void updateProductMasterById_success() {
        given(productMasterRepository.findById(100L)).willReturn(Optional.of(productMaster));
        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));
        given(productMasterRepository.existsByProductCode("NEW-CODE")).willReturn(false);
        given(productMasterMapper.toDto(productMaster)).willReturn(mock(ProductMasterResponse.class));

        ProductMasterResponse response = productMasterService.updateProductMasterById(100L, updateRequest);

        assertThat(response).isNotNull();
        assertThat(productMaster.getProductCode()).isEqualTo("NEW-CODE");
        assertThat(productMaster.getProductName()).isEqualTo("변경된 상품명");
        verify(productMasterRepository, never()).save(any()); // dirty checking으로 처리되는지 확인
    }

    @Test
    @DisplayName("상품이 존재하지 않으면 ProductMasterNotFoundException을 던진다")
    void updateProductMasterById_productNotFound() {
        given(productMasterRepository.findById(100L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> productMasterService.updateProductMasterById(100L, updateRequest))
                .isInstanceOf(ProductMasterNotFoundException.class);

        verify(categoryRepository, never()).findById(any());
    }

    @Test
    @DisplayName("카테고리가 존재하지 않으면 CategoryNotFoundException을 던진다")
    void updateProductMasterById_categoryNotFound() {
        given(productMasterRepository.findById(100L)).willReturn(Optional.of(productMaster));
        given(categoryRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> productMasterService.updateProductMasterById(100L, updateRequest))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    @DisplayName("변경하려는 productCode가 이미 존재하면 DuplicateProductMasterException을 던진다")
    void updateProductMasterById_duplicateProductCode() {
        given(productMasterRepository.findById(100L)).willReturn(Optional.of(productMaster));
        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));
        given(productMasterRepository.existsByProductCode("NEW-CODE")).willReturn(true);

        assertThatThrownBy(() -> productMasterService.updateProductMasterById(100L, updateRequest))
                .isInstanceOf(DuplicateProductMasterException.class);
    }

    @Test
    @DisplayName("productCode를 변경하지 않으면 중복 체크를 하지 않고 정상 처리된다")
    void updateProductMasterById_sameProductCode_noDuplicateCheck() {
        updateRequest = ProductMasterUpdateRequest.builder()
                .categoryNo(1L)
                .productCode("OLD-CODE")
                .productName("변경된 상품명")
                .productCreated(LocalDate.now())
                .price(BigDecimal.valueOf(1000))
                .used("Y")
                .address("서울시 은평구")
                .build();

        given(productMasterRepository.findById(100L)).willReturn(Optional.of(productMaster));
        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));
        given(productMasterMapper.toDto(productMaster)).willReturn(mock(ProductMasterResponse.class));

        productMasterService.updateProductMasterById(100L, updateRequest);

        verify(productMasterRepository, never()).existsByProductCode(any());
    }

    // ProductMaster 삭제
    @Test
    @DisplayName("존재하는 상품을 소프트 삭제한다")
    void softDeleteById_success() {
        ProductMaster productMaster = mock(ProductMaster.class);
        given(productMasterRepository.findById(100L)).willReturn(Optional.of(productMaster));

        productMasterService.softDeleteById(100L);

        verify(productMaster).softDelete();
    }
    @Test
    @DisplayName("존재하지 않는 상품이면 ProductMasterNotFoundException을 던진다")
    void softDeleteById_notFound() {
        given(productMasterRepository.findById(100L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> productMasterService.softDeleteById(100L))
                .isInstanceOf(ProductMasterNotFoundException.class);
    }

    // 제품분류에속한 제품마스터 조회
    @Test
    @DisplayName("제품분류 번호로 제품 목록 조회 성공")
    void findByProductMastersByCategoryNo_success() {

        // given
        Long categoryNo = 1L;

        Category category = Category.builder()
                .categoryNo(categoryNo)
                .categoryName("전자제품")
                .build();


        ProductMaster productMaster = ProductMaster.builder()
                .productNo(1L)
                .productName("노트북")
                .category(category)
                .build();


        ProductMasterResponse response =
                ProductMasterResponse.builder()
                        .productNo(1L)
                        .productName("노트북")
                        .categoryName("전자제품")
                        .build();


        given(categoryRepository.findById(categoryNo)).willReturn(Optional.of(category));
        given(productMasterRepository.findByCategoryIdWithCategory(categoryNo)).willReturn(List.of(productMaster));
        given(productMasterMapper.toDto(productMaster)).willReturn(response);
        
        // when
        List<ProductMasterResponse> result = productMasterService.findByProductMastersByCategoryNo(categoryNo);
        
        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductName())
                .isEqualTo("노트북");
        
        verify(categoryRepository).findById(categoryNo);
        verify(productMasterRepository).findByCategoryIdWithCategory(categoryNo);

    }

    @Test
    @DisplayName("존재하지 않는 카테고리 조회 시 예외 발생")
    void findByProductMastersByCategoryNo_categoryNotFound() {

        // given
        Long categoryNo = 999L;

        given(categoryRepository.findById(categoryNo))
                .willReturn(Optional.empty());

        // when & then

        assertThatThrownBy(() -> productMasterService.findByProductMastersByCategoryNo(categoryNo))
                .isInstanceOf(CategoryNotFoundException.class);

        verify(productMasterRepository, never()).findByCategoryIdWithCategory(categoryNo);
    }
}

