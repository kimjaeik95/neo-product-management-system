ALTER TABLE category
    ADD COLUMN deleted_yn CHAR(1) NOT NULL DEFAULT 'N'
        COMMENT '삭제여부',
    ADD COLUMN deleted_at DATETIME NULL
        COMMENT '삭제일시';


-- 제품마스터 소프트 딜리트 컬럼 추가
ALTER TABLE product_master
    ADD COLUMN deleted_yn CHAR(1) NOT NULL DEFAULT 'N'
        COMMENT '삭제여부',
    ADD COLUMN deleted_at DATETIME NULL
        COMMENT '삭제일시';