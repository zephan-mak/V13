package zookeeperTest;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.io.IOException;

/**
 * @author maizifeng
 * @Date 2019/7/3
 */
public class ZookeeperTest {

    @Test
    public void create() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper=new ZooKeeper("192.168.216.129:2181", 2000, new NewWatch());
        zooKeeper.create("/newNode1", "newNode1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        Thread.sleep(600000);
    }

    @Test
    public void del() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper=new ZooKeeper("192.168.216.129:2181", 2000, null);
        zooKeeper.delete("/newNode", -1);
    }

    @Test
    public void query() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper=new ZooKeeper("192.168.216.129:2181", 2000, null);
        Stat exists = zooKeeper.exists("/newNode", false);
        System.out.println(exists.getMzxid());
        System.out.println(exists.getCzxid());
        System.out.println(exists.getDataLength());
    }

    @Test
    public void queryDate() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper=new ZooKeeper("192.168.216.129:2181", 2000, null);
        byte[] data = zooKeeper.getData("/newNode", false, null);
        System.out.println(new String(data));
    }

    @Test
    public void modify() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper=new ZooKeeper("192.168.216.129:2181", 2000, null);
        Stat stat = zooKeeper.setData("/newNode", "hello".getBytes(), 0);
        System.out.println(stat.getDataLength());
    }

    @Test
    public void watch() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper=new ZooKeeper("192.168.216.129:2181", 2000,null );
        zooKeeper.exists("/newNode", new NewWatch());
        Thread.sleep(600000);
    }

    @Test
    public void watch2() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper=new ZooKeeper("192.168.216.129:2181", 2000,null );
       zooKeeper.getData("/newNode", new NewWatch(), null);
        Thread.sleep(600000);
    }

    @Test
    public void watch3() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper=new ZooKeeper("192.168.216.129:2181", 2000,null );
        zooKeeper.getChildren("/newNode", new NewWatch());
        Thread.sleep(600000);
    }
}
