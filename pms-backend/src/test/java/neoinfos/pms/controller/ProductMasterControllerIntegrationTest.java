package neoinfos.pms.controller;


import neoinfos.pms.dto.ProductMasterRequest;
import neoinfos.pms.dto.ProductMasterUpdateRequest;
import neoinfos.pms.entity.Category;
import neoinfos.pms.entity.ProductMaster;
import neoinfos.pms.repository.CategoryRepository;

import neoinfos.pms.repository.ProductMasterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;



/**
 * packageName    : neoinfos.pms.controller
 * fileName       : ProductMasterControllerIntegrationTest
 * author         : JAEIK
 * date           : 8/4/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/4/26        JAEIK       최초 생성
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class ProductMasterControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductMasterRepository productMasterRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    // ProductMaster 생성
    private Long categoryNo;

    private ProductMaster savedProduct;
    private Category savedCategory;
    @BeforeEach
    void setUp() {
        savedCategory = categoryRepository.save(Category.builder().categoryCode("TEST-001").categoryName("전자제품").used("Y").build());
        savedProduct = productMasterRepository.save(
                ProductMaster.builder()
                        .productCode("P-0001")
                        .productName("기존상품")
                        .category(savedCategory)
                        .price(BigDecimal.valueOf(5000))
                        .used("Y")
                        .address("서울")
                        .productCreated(LocalDate.now())
                        .build()
        );
    }

    @Test
    @DisplayName("상품 등록 성공 시 201과 생성된 리소스를 반환한다")
    void createProductMaster_success() throws Exception {
        ProductMasterRequest request = ProductMasterRequest.builder()
                .categoryNo(categoryNo)
                .productCode("P001")
                .productName("테스트 상품")
                .productCreated(LocalDate.now())
                .price(BigDecimal.valueOf(10000))
                .address("서울시 강남구")
                .build();

        mockMvc.perform(post("/api/product-masters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productCode").value("P001"));

        assertThat(productMasterRepository.existsByProductCode("P001")).isTrue();
    }

    @Test
    @DisplayName("중복된 상품코드로 등록하면 409(Conflict)를 반환한다")
    void createProductMaster_duplicateProductCode() throws Exception {
        productMasterRepository.save(
                ProductMaster.builder()
                        .productCode("P001")
                        .category(categoryRepository.findById(categoryNo).get())
                        .productName("테스트 상품")
                        .used("Y")
                        .productCreated(LocalDate.now())
                        .price(BigDecimal.valueOf(10000))
                        .address("서울시 강남구").build());

        ProductMasterRequest request = ProductMasterRequest.builder()
                .productCode("P001").categoryNo(categoryNo)
                .productName("테스트 상품")
                .productCreated(LocalDate.now())
                .price(BigDecimal.valueOf(10000))
                .address("서울시 강남구").build();

        mockMvc.perform(post("/api/product-masters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("존재하지 않는 제품분류로 등록하면 404를 반환한다")
    void createProductMaster_categoryNotFound() throws Exception {
        ProductMasterRequest request = ProductMasterRequest.builder()
                .productCode("P001").categoryNo(9999999999999L)
                .productName("테스트 상품")
                .productCreated(LocalDate.now())
                .price(BigDecimal.valueOf(10000))
                .address("서울시 강남구").build();

        mockMvc.perform(post("/api/product-masters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // ProductMaster 전체조회
    @Test
    @DisplayName("상품이 등록되어 있으면 목록 조회 시 200과 페이징 데이터를 반환한다")
    void getProductMasters_success() throws Exception {

        // given
        Category category = categoryRepository.save(
                Category.builder()
                        .categoryCode("TEST100")
                        .categoryName("전자기기")
                        .used("Y")
                        .build()
        );

        productMasterRepository.save(
                ProductMaster.builder()
                        .category(category)
                        .productCode("P001")
                        .productName("노트북")
                        .productCreated(LocalDate.of(2026, 8, 1))
                        .price(new BigDecimal("1000000"))
                        .used("Y")
                        .address("서울")
                        .build()
        );

        productMasterRepository.save(
                ProductMaster.builder()
                        .category(category)
                        .productCode("P002")
                        .productName("마우스")
                        .productCreated(LocalDate.of(2026, 8, 2))
                        .price(new BigDecimal("50000"))
                        .used("Y")
                        .address("경기")
                        .build()
        );


        // when & then
        mockMvc.perform(get("/api/product-masters")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())

                // content 개수
                .andExpect(jsonPath("$.content.length()").value(2))

                // 데이터 확인
                .andExpect(jsonPath("$.content[0].productCode")
                        .value("P001"))
                .andExpect(jsonPath("$.content[0].productName")
                        .value("노트북"))

                .andExpect(jsonPath("$.content[1].productCode")
                        .value("P002"))
                .andExpect(jsonPath("$.content[1].productName")
                        .value("마우스"))

                // 페이징 정보 확인
                .andExpect(jsonPath("$.totalElements")
                        .value(2))
                .andExpect(jsonPath("$.totalPages")
                        .value(1))
                .andExpect(jsonPath("$.number")
                        .value(0))
                .andExpect(jsonPath("$.size")
                        .value(10));
    }

    // ProductMaster 단건 조회
    @Test
    @DisplayName("존재하는 상품 번호로 조회하면 200과 상품 정보를 반환한다")
    void getProductMaster_success() throws Exception {
        // given
        Category category = categoryRepository.save(Category.builder().categoryCode("P001").categoryName("전자기기").used("Y").build());
        ProductMaster saved = productMasterRepository.save(
                ProductMaster.builder()
                        .category(category)
                        .productCode("P001")
                        .productName("무선 키보드")
                        .productCreated(LocalDate.of(2026, 8, 1))
                        .price(new BigDecimal("100000"))
                        .address("서울")
                        .used("Y")
                        .build());

        // when & then
        mockMvc.perform(get("/api/product-masters/{productMasterNo}", saved.getProductNo()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productNo").value(saved.getProductNo()))
                .andExpect(jsonPath("$.productCode").value("P001"))
                .andExpect(jsonPath("$.productName").value("무선 키보드"));
    }

    @Test
    @DisplayName("존재하지 않는 상품 번호로 조회하면 404를 반환한다")
    void getProductMaster_notFound() throws Exception {
        mockMvc.perform(get("/api/product-masters/{productMasterNo}", 999999L))
                .andExpect(status().isNotFound());
    }

    // ProductMaster 업데이트

    @Test
    @DisplayName("PUT /product-masters/{productNo} - 정상 수정 시 200과 변경된 값을 반환한다")
    void updateProductMaster_success() throws Exception {
        ProductMasterUpdateRequest request = ProductMasterUpdateRequest.builder()
                .categoryNo(savedCategory.getCategoryNo())
                .productCode("P-9999")
                .productName("수정된상품")
                .productCreated(LocalDate.now())
                .price(BigDecimal.valueOf(9999))
                .used("N")
                .address("부산")
                .build();

        mockMvc.perform(put("/api/product-masters/{productNo}", savedProduct.getProductNo())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productCode").value("P-9999"))
                .andExpect(jsonPath("$.productName").value("수정된상품"));
    }

    @Test
    @DisplayName("존재하지 않는 productNo면 404를 반환한다")
    void updateProductMaster_notFound() throws Exception {

        ProductMasterUpdateRequest request =
                validRequest(savedCategory.getCategoryNo());

        mockMvc.perform(
                        put("/api/product-masters/{productNo}", 999999L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("존재하지 않는 categoryNo면 404를 반환한다")
    void updateProductMaster_categoryNotFound() throws Exception {

        ProductMasterUpdateRequest request =
                validRequest(999999L);

        mockMvc.perform(
                        put("/api/product-masters/{productNo}", savedProduct.getProductNo())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("이미 존재하는 productCode로 변경 시 409를 반환한다")
    void updateProductMaster_duplicateProductCode() throws Exception {

        productMasterRepository.save(
                ProductMaster.builder()
                        .productCode("P-DUP")
                        .productName("다른상품")
                        .category(savedCategory)
                        .price(BigDecimal.valueOf(1000))
                        .used("Y")
                        .address("인천")
                        .productCreated(LocalDate.now())
                        .build()
        );

        ProductMasterUpdateRequest request =
                ProductMasterUpdateRequest.builder()
                        .categoryNo(savedCategory.getCategoryNo())
                        .productCode("P-DUP")
                        .productName("정상상품")
                        .productCreated(LocalDate.now())
                        .price(BigDecimal.valueOf(1000))
                        .used("Y")
                        .address("경기")
                        .build();

        mockMvc.perform(
                        put("/api/product-masters/{productNo}", savedProduct.getProductNo())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isConflict());
    }

    private ProductMasterUpdateRequest validRequest(Long categoryNo) {
        return ProductMasterUpdateRequest.builder()
                .categoryNo(categoryNo)
                .productCode("P-VALID")
                .productName("정상상품")
                .productCreated(LocalDate.now())
                .price(BigDecimal.valueOf(1000))
                .used("Y")
                .address("경기")
                .build();
    }
}
