

create table mq_share_note_relation (
    id BIGINT NOT NULL COMMENT '自增ID',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    ts_code VARCHAR(10) NOT NULL COMMENT '股票编码',
    note_id BIGINT NOT NULL COMMENT 'mq_share_note表ID',
    PRIMARY KEY (id) USING BTREE,
    KEY t (update_time) USING BTREE,
    KEY st (ts_code, update_time) USING BTREE,
    KEY n (note_id) USING BTREE
)