delete from mq_sys_param where param_key='SCHEDULE_DAILY_UPDATE';
insert into mq_sys_param (param_key, param_value, param_remark) values ('SCHEDULE_DAILY_UPDATE', '1', '是否启用每日拉取数据');