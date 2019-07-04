package com.qf.springbootredis;

import com.qf.springbootredis.config.GoogleBloom;
import com.qf.springbootredis.entity.ProductType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootRedisApplicationTests {

	@Resource(name = "redisTemplate1")
	private RedisTemplate redisTemplate;

	@Autowired
	private GoogleBloom googleBloom;

	@Test
	public void contextLoads() {
		redisTemplate.opsForValue().set("k2", "v2");
		System.out.println(redisTemplate.opsForValue().get("k2"));
	}

	@Test
	public void Test2() throws InterruptedException {
		String key="product:type";
		List<ProductType> types= (List<ProductType>) redisTemplate.opsForValue().get(key);
		if(types==null){
			String uuid= UUID.randomUUID().toString();
			Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid, 10, TimeUnit.MINUTES);
			if(lock){
				System.out.println("缓存没有需要去数据库查找");
				types=new ArrayList<>();
				types.add(new ProductType(1,"衣服"));
				types.add(new ProductType(2,"裤子"));
				redisTemplate.opsForValue().set(key, types);
				System.out.println("放入缓存成功");
				String oldLock = (String) redisTemplate.opsForValue().get("lock");
				if(uuid.equals(oldLock)){
					redisTemplate.delete("lock");
				}
			}else{
				Thread.sleep(100);
			}
		}else{
			System.out.println("从缓存中获取数据");
			for (ProductType type : types) {
				System.out.println(type);
			}
		}

	}

	@Test
	public void batchTest() throws InterruptedException {
		ExecutorService pool=new ThreadPoolExecutor(10, 20, 100, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(100));
		for (int i = 0; i < 100; i++) {
			pool.submit(new Runnable() {
				public void run() {
					try {
						Test2();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}
		Thread.sleep(1000000);
	}

	@Test
	public void luaTest(){
		DefaultRedisScript<Boolean> redisScript=new DefaultRedisScript<>();
		redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lock.lua")));
		redisScript.setResultType(Boolean.class);
		List<String> list=new ArrayList<>();
		list.add("lock");
		String uuid=UUID.randomUUID().toString();
		list.add(uuid);
		list.add("60");
		Boolean result = (Boolean) redisTemplate.execute(redisScript, list);
		System.out.println(result);
		if(result){
			Long expire = redisTemplate.getExpire("lock");
			System.out.println(expire);
		}

	}
	@Test
	public void cacheTest(){
		String key = "1";
		ProductType productType = (ProductType) redisTemplate.opsForValue().get(key);
		if(productType==null){
			System.out.println("查数据库");
			redisTemplate.opsForValue().set(key, new ProductType(), 60, TimeUnit.SECONDS);
		}else{
			System.out.println("查缓存");
			System.out.println(productType);
		}
	}

	@Test
	public void googleBloom(){
		int count=0;
		for (long i = 100; i < 200; i++) {
			boolean exists = googleBloom.isExists(i);
			if(exists){
				count++;
			}
		}
		System.out.println(count);
	}
	@Test
	public void SpringLuaTest(){
		DefaultRedisScript<Boolean> redisScript=new DefaultRedisScript<Boolean>();
		redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("add.lua")));
		redisScript.setResultType(Boolean.class);
		List<String> list=new ArrayList<>();
		list.add("addTest");
		list.add("hello");
		boolean result = (boolean) redisTemplate.execute(redisScript, list);
		System.out.println(result);
	}

	@Test
	public void SpringLuaTest2(){
		DefaultRedisScript<Boolean> redisScript=new DefaultRedisScript<Boolean>();
		redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("exists.lua")));
		redisScript.setResultType(Boolean.class);
		List<String> list=new ArrayList<>();
		list.add("addTest");
		list.add("hello");
		boolean result = (boolean) redisTemplate.execute(redisScript, list);
		System.out.println(result);
	}
}
