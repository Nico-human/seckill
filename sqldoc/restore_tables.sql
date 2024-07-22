delete from t_seckill_orders where user_id like '13477%';
delete from t_orders where user_id like '13477%';
update t_seckill_goods set stock_count = 10 where id in (1,2,3);