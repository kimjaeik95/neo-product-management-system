package neoinfos.pms.mapper;

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
        return ProductMasterResponse.builder()
                .productNo(productMaster.getProductNo())
                .categoryNo(productMaster.getCategory().getCategoryNo())
                .productCode(productMaster.getProductCode())
                .productName(productMaster.getProductName())
                .productCreated(productMaster.getProductCreated())
                .price(productMaster.getPrice())
                .address(productMaster.getAddress())
                .createdAt(productMaster.getCreatedAt())
                .updatedAt(productMaster.getUpdatedAt())
                .deletedYn(productMaster.getDeletedYn())
                .deletedAt(productMaster.getDeletedAt())
                .build();
    }
}
