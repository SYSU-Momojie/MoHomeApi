create table USER (
id BIGINT NOT NULL COMMENT '自增ID',
motalk_openid VARCHAR(100) COMMENT 'MoTalk小程序的openid',
PRIMARY KEY (id) USING BTREE,
KEY (motalk_openid) USING BTREE
)