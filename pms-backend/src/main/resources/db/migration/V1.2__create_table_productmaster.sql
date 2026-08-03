CREATE TABLE product_master(
    product_no      BIGINT         PRIMARY KEY AUTO_INCREMENT     COMMENT '제품마스터pk',
    category_no     BIGINT         NOT NULL,
    product_code    VARCHAR(100)   NOT NULL UNIQUE                COMMENT '제품코드',
    product_name    VARCHAR(100)   NOT NULL                       COMMENT '제품명',
    product_created DATETIME                                      COMMENT '제품생산일자',
    price           DECIMAL(10,2)                                 COMMENT '제품단가',
    used            CHAR(1)        NOT NULL                       COMMENT '사용여부',
    address         VARCHAR(255)                                  COMMENT '제품생산지주소',
    created_at      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP  COMMENT '등록일자',
    updated_at      DATETIME       DEFAULT  NULL ON UPDATE  CURRENT_TIMESTAMP  COMMENT '수정일자',
    CONSTRAINT fk_product_category FOREIGN KEY (category_no) REFERENCES category(category_no)
);