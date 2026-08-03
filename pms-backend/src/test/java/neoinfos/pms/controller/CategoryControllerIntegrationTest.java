package neoinfos.pms.controller;

import neoinfos.pms.dto.CategoryRequest;
import neoinfos.pms.entity.Category;
import neoinfos.pms.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
public class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

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
                .andExpect(status().isOk())
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
}


