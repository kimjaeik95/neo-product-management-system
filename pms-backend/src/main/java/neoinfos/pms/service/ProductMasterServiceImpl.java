package neoinfos.pms.service;

import lombok.RequiredArgsConstructor;
import neoinfos.pms.common.exception.category.CategoryNotFoundException;
import neoinfos.pms.common.exception.productmaster.DuplicateProductMasterException;
import neoinfos.pms.common.exception.productmaster.ProductMasterNotFoundException;
import neoinfos.pms.dto.ProductMasterRequest;
import neoinfos.pms.dto.ProductMasterResponse;
import neoinfos.pms.dto.ProductMasterUpdateRequest;
import neoinfos.pms.entity.Category;
import neoinfos.pms.entity.ProductMaster;
import neoinfos.pms.mapper.ProductMasterMapper;
import neoinfos.pms.repository.CategoryRepository;
import neoinfos.pms.repository.ProductMasterRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 *packageName    : neoinfos.pms.service
 * fileName       : ProductMasterServcieImpl
 * author         : JAEIK
 * date           : 8/4/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/4/26        JAEIK       최초 생성
 */
@Service
@RequiredArgsConstructor
public class ProductMasterServiceImpl implements ProductMasterService{
    private final ProductMasterRepository productMasterRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMasterMapper productMasterMapper;

    @Override
    @Transactional
    public ProductMasterResponse createProductMaster(ProductMasterRequest productMasterRequest) {
        if (productMasterRepository.existsByProductCode(productMasterRequest.getProductCode())) {
            throw new DuplicateProductMasterException();
        }

        Category category = categoryRepository.findById(productMasterRequest.getCategoryNo())
                .orElseThrow(() -> new CategoryNotFoundException(productMasterRequest.getCategoryNo()));

        ProductMaster productMaster = productMasterMapper.toEntity(productMasterRequest, category);

        ProductMaster savedProductMaster;
        try {
            savedProductMaster = productMasterRepository.save(productMaster);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateProductMasterException("동시 요청 처리 중 상품 코드 중복이 발생했습니다. : " + productMasterRequest.getProductCode());
        }

        return productMasterMapper.toDto(savedProductMaster);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductMasterResponse> findALLProductMasters(Pageable pageable) {
        return productMasterRepository.findAll(pageable)
                .map(productMasterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductMasterResponse findProductMasterById(Long productNo) {
        ProductMaster productMaster = productMasterRepository.findById(productNo)
                .orElseThrow(() -> new ProductMasterNotFoundException(productNo));

        return productMasterMapper.toDto(productMaster);
    }

    @Override
    @Transactional
    public ProductMasterResponse updateProductMasterById(Long productNo, ProductMasterUpdateRequest updateRequest) {
        ProductMaster productMaster = productMasterRepository.findById(productNo)
                .orElseThrow(() -> new ProductMasterNotFoundException(productNo));

        Category category = categoryRepository.findById(updateRequest.getCategoryNo())
                .orElseThrow(() -> new CategoryNotFoundException(updateRequest.getCategoryNo()));

        if (!productMaster.getProductCode().equals(updateRequest.getProductCode())
                && productMasterRepository.existsByProductCode(updateRequest.getProductCode())) {
            throw new DuplicateProductMasterException();
        }

        productMaster.updateProductMaster(updateRequest, category);

        return productMasterMapper.toDto(productMaster);
    }

    @Override
    @Transactional
    public void softDeleteById(Long productNo) {
        ProductMaster productMaster = productMasterRepository.findById(productNo)
                .orElseThrow(() -> new ProductMasterNotFoundException(productNo));

        productMaster.softDelete();
    }
}
