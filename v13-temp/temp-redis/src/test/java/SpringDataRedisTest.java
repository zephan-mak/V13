import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author maizifeng
 * @Date 2019/6/24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:redis.xml")
public class SpringDataRedisTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void Test1(){
        redisTemplate.opsForValue().set("k1", "v1");
        System.out.println(redisTemplate.opsForValue().get("k1"));
    }
    @Test
    public void Test2(){
        redisTemplate.opsForValue().set("money", "100");
        redisTemplate.opsForValue().increment("money", 100);
        System.out.println(redisTemplate.opsForValue().get("money"));
    }
    @Test
    public void Test3(){
//        redisTemplate.opsForHash().put("person1", "name", "aa");
//        System.out.println(redisTemplate.opsForHash().get("person1", "name"));
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("stuName", "aa");
        map.put("stuAge", 11);
        redisTemplate.opsForHash().put("person2", "name", map);
        System.out.println(redisTemplate.opsForHash().get("person2", "name"));
    }

    @Test
    public void Test4(){

//        Set<String> set=new HashSet();
//        set.add("aa");
//        set.add("ss");
//        redisTemplate.opsForZSet().add("aa",set );
//
//        Set<String> set2=new HashSet();
//        set.add("aa");
//        set.add("bb");
//        redisTemplate.opsForZSet().add("bb",set2 );

        redisTemplate.opsForZSet().add("aa","aa",1);
        redisTemplate.opsForZSet().add("aa","bb",3);
        redisTemplate.opsForZSet().add("aa","cc",2);
        redisTemplate.opsForZSet().add("aa","dd",5);

        redisTemplate.opsForZSet().add("bb","aa",1);
        redisTemplate.opsForZSet().add("bb","cc",3);
        redisTemplate.opsForZSet().add("bb","ww",2);
        redisTemplate.opsForZSet().add("bb","ee",5);

        redisTemplate.opsForZSet().unionAndStore("aa", "bb", "aa");
        Set<ZSetOperations.TypedTuple<String>> aa = redisTemplate.opsForZSet().rangeWithScores("aa", 0, -1);
        Iterator<ZSetOperations.TypedTuple<String>> iterator = aa.iterator();
        while (iterator.hasNext()){
            ZSetOperations.TypedTuple<String> next = iterator.next();
            System.out.println(next.getValue()+"----"+next.getScore());
        }
//        redisTemplate.opsForSet().add("aa", "aa","cc");
//        List<String> list=new ArrayList();
//        list.add("name");
//        redisTemplate.opsForSet().add("name", "cc","bb");
//        System.out.println(redisTemplate.opsForSet().members("name"));
//        System.out.println(redisTemplate.opsForSet().pop("name"));
//        差集
//        System.out.println(redisTemplate.opsForSet().difference("aa", list));
//        System.out.println(redisTemplate.opsForSet().difference("aa", "name"));
//        差集并保存
//        redisTemplate.opsForSet().differenceAndStore("aa", list, "aa-1");
//        System.out.println(redisTemplate.opsForSet().members("aa-1"));

//        交集
//        System.out.println(redisTemplate.opsForSet().intersect("aa", "name"));
//        System.out.println(redisTemplate.opsForSet().intersect("aa", list));
//        交集并保存
//        redisTemplate.opsForSet().intersectAndStore("aa", "name", "aa-2");
//        System.out.println(redisTemplate.opsForSet().members("aa-2"));

//        合集
//        System.out.println(redisTemplate.opsForSet().union("aa", "name"));
//        System.out.println(redisTemplate.opsForSet().union("aa", list));
//        合集并保存
//        redisTemplate.opsForSet().unionAndStore("aa", "name", "aa-3");
//        System.out.println(redisTemplate.opsForSet().members("aa-3"));
    }

    @Test
    public void batchTest(){
        long startTime=System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            redisTemplate.opsForValue().set("k"+i, "v"+i);
        }
        long endTime=System.currentTimeMillis();
        System.out.println(endTime - startTime);//4199
    }

    @Test
    public void batchTest2(){
        long startTime=System.currentTimeMillis();
        redisTemplate.executePipelined(new SessionCallback() {
            public Object execute(RedisOperations operations) throws DataAccessException {
                for (int i = 0; i < 10000; i++) {
                    redisTemplate.opsForValue().set("k"+i, "v"+i);
                }
                return null;
            }
        });
        long endTime=System.currentTimeMillis();
        System.out.println(endTime - startTime);//222
    }
    @Test
    public void timeOut(){
        redisTemplate.execute(new SessionCallback() {
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.opsForValue().set("k1", "v1");
                operations.expire("k1", 10, TimeUnit.SECONDS);
                System.out.println(operations.getExpire("k1", TimeUnit.SECONDS))
                ;

                return null;
            }
        });
    }

}

