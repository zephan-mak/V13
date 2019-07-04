package zookeeperTest;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * @author maizifeng
 * @Date 2019/7/3
 */
public class NewWatch implements Watcher {

    public void process(WatchedEvent watchedEvent) {
        System.out.println("发生变化的节点"+watchedEvent.getPath());
        System.out.println("发生变化的类型"+watchedEvent.getType());
    }
}
