package neoinfos.pms.controller;


import neoinfos.pms.dto.ProductMasterRequest;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @BeforeEach
    void setUp() {
        Category category = categoryRepository.saveAndFlush(Category.builder().categoryCode("TEST001").categoryName("전자기기").used("Y").build());
        categoryNo = category.getCategoryNo();
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
}