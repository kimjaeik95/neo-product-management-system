package neoinfos.pms.repository;

import neoinfos.pms.dto.ProductMasterListProjection;
import neoinfos.pms.dto.ProductMasterResponse;
import neoinfos.pms.entity.ProductMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    Optional<ProductMaster> findById(Long productNo);

    boolean existsByCategory_CategoryNo(Long categoryNo);

    @Query("""
            select new neoinfos.pms.dto.ProductMasterListProjection(
            p.productNo,
            c.categoryNo,
            c.categoryName,
            p.productCode,
            p.productName,
            p.productCreated,
            p.price,
            p.used,
            p.address,
            p.createdAt,
            p.updatedAt
             ) 
             from ProductMaster p join p.category c
             """)
    Page<ProductMasterListProjection> findProductList(Pageable pageable);

    @Query("SELECT p FROM ProductMaster p JOIN FETCH p.category WHERE p.category.categoryNo = :categoryNo AND p.deletedYn = 'N'")
    List<ProductMaster> findByCategoryIdWithCategory(@Param("categoryNo") Long categoryNo);
    List<ProductMaster> findProductMasterByCategory_CategoryNo(Long categoryNo);
}
