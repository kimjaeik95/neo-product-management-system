package neoinfos.pms.repository;

import neoinfos.pms.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * packageName    : neoinfos.pms.repository
 * fileName       : CategoryRepository
 * author         : JAEIK
 * date           : 8/3/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/3/26        JAEIK       최초 생성
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByCategoryCode(String categoryCode);
}
