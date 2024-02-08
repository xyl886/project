local key = KEYS[1]
local count = tonumber(ARGV[1])
local time = tonumber(ARGV[2])
local current = redis.call('get', key)
if current and count and tonumber(current) > count then
       return tonumber(current)
end
current = redis.call('incr', key)
if tonumber(current) == 1 then
       redis.call('expire', key, time)
end
return tonumber(current)



--local c
--c = redis.call('get', KEYS[1])
--if c and tonumber(c) > tonumber(ARGV[1]) then
--    return c;
--end
--c = redis.call('incr', KEYS[1])
--if tonumber(c) == 1 then
--    redis.call('expire', KEYS[1], ARGV[2])
--end
--return c;
