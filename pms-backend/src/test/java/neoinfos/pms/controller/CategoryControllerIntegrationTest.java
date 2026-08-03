package neoinfos.pms.service;

import neoinfos.pms.dto.CategoryRequest;
import neoinfos.pms.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.test.web.servlet.MockMvc;


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
    @DisplayName("카테고리 생성 성공 - 201/200 응답과 DB 저장 확인")
    void createCategory_success() throws Exception {
        // given
        CategoryRequest request = CategoryRequest.builder()
                .categoryCode("CATE001")
                .categoryName("의류")
                .build();

        // when & then
        mockMvc.perform(post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryCode").value("CATE001"))
                .andExpect(jsonPath("$.categoryName").value("의류"));

        // DB에 실제로 반영됐는지까지 확인
        assertThat(categoryRepository.existsByCategoryCode("CATE001")).isTrue();
    }

}
