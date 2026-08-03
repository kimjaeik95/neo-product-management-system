CREATE TABLE category
(
    category_no   BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '제품분류pk',
    category_code VARCHAR(100) NOT NULL UNIQUE      COMMENT '제품분류코드',
    category_name VARCHAR(100) NOT NULL             COMMENT '제품분류명',
    used          CHAR(1)      NOT NULL             COMMENT '시즌사용여부',
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
    updated_at    DATETIME     DEFAULT  NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일자'
);