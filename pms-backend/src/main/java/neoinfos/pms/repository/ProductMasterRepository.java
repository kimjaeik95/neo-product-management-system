package neoinfos.pms.repository;

import neoinfos.pms.entity.ProductMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * packageName    : neoinfos.pms.repository
 * fileName       : ProductMasterRepository
 * author         : JAEIK
 * date           : 8/4/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/4/26        JAEIK       최초 생성
 */
@Repository
public interface ProductMasterRepository extends JpaRepository<ProductMaster, Long> {
    boolean existsByProductCode(String productCode);
}
