package neoinfos.pms.controller;

import jakarta.persistence.EntityManager;
import neoinfos.pms.dto.CategoryRequest;
import neoinfos.pms.dto.CategoryUpdateRequest;
import neoinfos.pms.entity.Category;
import neoinfos.pms.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

/**
 * packageName    : neoinfos.pms.service
 * fileName       : CategoryControllerIntegrationTest
 * author         : JAEIK
 * date           : 8/3/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/3/26        JAEIK       최초 생성
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private CategoryRepository categoryRepository;

    // category 생성
    @Test
    @DisplayName("카테고리 생성 성공 - 200 응답과 DB 저장 확인")
    void createCategory_success() throws Exception {
        // given
        CategoryRequest request = CategoryRequest.builder()
                .categoryCode("CATE001")
                .categoryName("의류")
                .build();

        // when & then
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryCode").value("CATE001"))
                .andExpect(jsonPath("$.categoryName").value("의류"));

        // DB에 실제로 반영됐는지까지 확인
        assertThat(categoryRepository.existsByCategoryCode("CATE001")).isTrue();
    }

    @Test
    @DisplayName("필수값 누락 시 밸리데이션 오류로 400을 반환한다")
    void createCategory_validationFail() throws Exception {
        // given: categoryCode 없이 요청
        String invalidJson = """
                {
                    "categoryCode" : "",
                    "categoryName": "의류"
                             
                }
                """;

        // when & then
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    // category 전체조회
    @Test
    @DisplayName("저장된 제품분류 목록을 조회한다")
    void getCategories_success() throws Exception {
        // given
        categoryRepository.save(Category.builder().categoryCode("CATE001").categoryName("의류").used("Y").build());
        categoryRepository.save(Category.builder().categoryCode("CATE002").categoryName("잡화").used("Y").build());

        // when & then
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].categoryCode")
                        .value(hasItems("CATE001", "CATE002")));
    }

    // category 단건 조회
    @Test
    @DisplayName("존재하는 categoryNo로 조회하면 정상 응답을 반환한다")
    void getCategoryById_success() throws Exception {
        // given
        Category saved = categoryRepository.save(
                Category.builder().categoryCode("CATE001").categoryName("의류").used("Y").build());

        // when & then
        mockMvc.perform(get("/api/categories/{categoryNo}", saved.getCategoryNo()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryCode").value("CATE001"));
    }

    // Update Category
    @Test
    @DisplayName("정상적으로 카테고리를 수정한다")
    void updateCategoryById_success() throws Exception {
        // given
        Category saved = categoryRepository.save(
                Category.builder().categoryCode("CATE001").categoryName("의류").used("Y").build());

        CategoryUpdateRequest request = new CategoryUpdateRequest("CATE002", "잡화", "N");

        // when & then
        mockMvc.perform(put("/api/categories/{categoryNo}", saved.getCategoryNo())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryCode").value("CATE002"))
                .andExpect(jsonPath("$.used").value("N"));
    }

    @Test
    @DisplayName("used 값이 Y/N이 아니면 400 밸리데이션 오류를 반환한다")
    void updateCategoryById_invalidUsedValue() throws Exception {
        // given
        Category saved = categoryRepository.save(
                Category.builder().categoryCode("CATE001").categoryName("의류").used("Y").build());

        String invalidJson = """
                {
                    "categoryCode": "CATE001",
                    "categoryName": "의류",
                    "used": "X"
                }
                """;

        // when & then
        mockMvc.perform(put("/api/categories/{categoryNo}", saved.getCategoryNo())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("존재하지 않는 categoryNo로 수정하면 오류를 반환한다")
    void updateCategoryById_notFound() throws Exception {
        CategoryUpdateRequest request = new CategoryUpdateRequest("CATE001", "의류", "Y");

        mockMvc.perform(put("/api/categories/{categoryNo}", 9999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("이미 존재하는 코드로 변경하면 오류를 반환한다")
    void updateCategoryById_duplicateCode() throws Exception {
        categoryRepository.save(Category.builder().categoryCode("CATE001").categoryName("의류").used("Y").build());
        Category target = categoryRepository.save(
                Category.builder().categoryCode("CATE002").categoryName("잡화").used("Y").build());

        CategoryUpdateRequest request = new CategoryUpdateRequest("CATE001", "잡화", "Y");

        mockMvc.perform(put("/api/categories/{categoryNo}", target.getCategoryNo())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }

    // 제품 분류 삭제

    @Test
    @DisplayName("삭제 요청 시 204를 반환하고 실제 DB에 소프트 딜리트가 반영된다")
    void deleteCategoryById_success() throws Exception {
        // given
        Category saved = categoryRepository.save(
                Category.builder().categoryCode("CATE001").categoryName("의류").used("Y").build());

        // when & then
        mockMvc.perform(delete("/api/categories/{categoryNo}", saved.getCategoryNo()))
                .andExpect(status().isNoContent());

        // @SQLRestriction 때문에 기본 findById로는 조회 안 됨  (네이티브 쿼리로 직접 검증)
        Object deletedYn =  em.createNativeQuery(
                        """
                        SELECT deleted_yn
                        FROM category
                        WHERE category_no = :no
                        """
                )
                .setParameter("no", saved.getCategoryNo())
                .getSingleResult();

        assertThat(deletedYn.toString()).isEqualTo("Y");
    }

    @Test
    @DisplayName("삭제 후에는 목록 조회에서 제외된다")
    void deleteCategoryById_excludedFromList() throws Exception {
        // given
        Category saved = categoryRepository.save(
                Category.builder().categoryCode("CATE001").categoryName("의류").used("Y").build());

        // when
        mockMvc.perform(delete("/api/categories/{categoryNo}", saved.getCategoryNo()))
                .andExpect(status().isNoContent());

        // then
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.categoryCode == 'CATE001')]").doesNotExist());
    }

    @Test
    @DisplayName("존재하지 않는 categoryNo 삭제 시 오류를 반환한다")
    void deleteCategoryById_notFound() throws Exception {
        mockMvc.perform(delete("/api/categories/{categoryNo}", 9999L))
                .andExpect(status().is4xxClientError());
    }
}



