import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * @author maizifeng
 * @Date 2019/6/24
 */
public class JedisTest {

    @Test
    public void connection(){
        Jedis jedis=new Jedis("192.168.216.129", 6379);
        jedis.auth("java1902");
        jedis.set("k1", "v1");
        System.out.println(jedis.get("k1"));
    }

    @Test
    public void connection2(){
        Jedis jedis=new Jedis("192.168.216.129", 6379);
        jedis.auth("java1902");
        jedis.mset("name", "aa", "age","11" );
        System.out.println(jedis.mget("name", "age"));
    }

    @Test
    public void connection3(){
        Jedis jedis=new Jedis("192.168.216.129", 6379);
        jedis.auth("java1902");
        jedis.hset("person1", "name", "aa");
        jedis.hset("person1", "age", "11");
        System.out.println(jedis.hget("person1", "age"));
    }

    @Test
    public void connection4(){
        Jedis jedis=new Jedis("192.168.216.129", 6379);
        jedis.auth("java1902");
//        jedis.del("name");
        jedis.rpush("name", "aa", "bb", "cc");
//        System.out.println(jedis.rpop("name"));
        System.out.println(jedis.lrange("name", 0, -1));
    }

    @Test
    public void connection5(){
        Jedis jedis=new Jedis("192.168.216.129", 6379);
        jedis.auth("java1902");
//        jedis.sadd("age", "11","12","13");
//        System.out.println(jedis.smembers("age"));
        System.out.println(jedis.sismember("age", "11"));
        System.out.println(jedis.srandmember("age"));
    }

    @Test
    public void connection6(){
        Jedis jedis=new Jedis("192.168.216.129", 6379);
        jedis.auth("java1902");
        jedis.watch("k1");
        Transaction transaction=jedis.multi();
        transaction.set("t1", "v1");
        transaction.set("t2", "v2");
        transaction.exec();

    }
}
