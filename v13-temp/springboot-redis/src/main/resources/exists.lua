local bloomName=KEYS[1]
local value=KEYS[2]

local result=redis.call('bf.exists',bloomName,value)
return result