package neoinfos.pms.mapper;

import lombok.extern.slf4j.Slf4j;
import neoinfos.pms.dto.ProductMasterRequest;
import neoinfos.pms.dto.ProductMasterResponse;
import neoinfos.pms.entity.Category;
import neoinfos.pms.entity.ProductMaster;
import org.springframework.stereotype.Component;

/**
 * packageName    : neoinfos.pms.mapper
 * fileName       : ProductMasterMapper
 * author         : JAEIK
 * date           : 8/4/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/4/26        JAEIK       최초 생성
 */
@Component
@Slf4j
public class ProductMasterMapper {

    public ProductMaster toEntity(ProductMasterRequest masterRequest, Category category) {
        return ProductMaster.builder()
                .category(category)
                .productCode(masterRequest.getProductCode())
                .productName(masterRequest.getProductName())
                .productCreated(masterRequest.getProductCreated())
                .price(masterRequest.getPrice())
                .used("Y")
                .address(masterRequest.getAddress())
                .build();
    }

    public ProductMasterResponse toDto(ProductMaster productMaster) {
        log.info("N + 1 테스트 category = " + productMaster.getCategory().getCategoryNo());
        return ProductMasterResponse.builder()
                .categoryNo(productMaster.getCategory().getCategoryNo())
                .categoryName(productMaster.getCategory().getCategoryName())
                .productNo(productMaster.getProductNo())
                .productCode(productMaster.getProductCode())
                .productName(productMaster.getProductName())
                .productCreated(productMaster.getProductCreated())
                .price(productMaster.getPrice())
                .used(productMaster.getUsed())
                .address(productMaster.getAddress())
                .createdAt(productMaster.getCreatedAt())
                .updatedAt(productMaster.getUpdatedAt())
                .deletedYn(productMaster.getDeletedYn())
                .deletedAt(productMaster.getDeletedAt())
                .build();
    }
}
