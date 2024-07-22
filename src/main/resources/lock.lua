--获取锁, 比较锁, 删除锁
--lua脚本保证原子性
if redis.call("get", KEYS[1])==ARGV[1] then
    return redis.call("del", KEYS[1])
else
    return 0
end
