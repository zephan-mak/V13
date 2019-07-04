import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author maizifeng
 * @Date 2019/6/18
 */
public class HttpClientTest {
    @Test
    public void Html() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String url="http://localhost:9093/item/creatHtml/23";
        HttpGet get=new HttpGet(url);
        CloseableHttpResponse response = client.execute(get);
        int statusCode = response.getStatusLine().getStatusCode();
        if(statusCode==200){
//            HttpEntity entity = response.getEntity();
//            InputStream inputStream = entity.getContent();
//            byte[] bs=new byte[1024];
//            int len;
//            while ((len=inputStream.read(bs))!=-1){
//                System.out.println(new String(bs, 0, len));
//            }
        }else{
            System.out.println(statusCode);
        }
    }
    @Test
    public void UtilsTest(){
//        String response = HttpClientUtils.doGet("http://localhost:9093/item/createHtml/24");
//        System.out.println(response);

        Map<String, String> map=new HashMap<String, String>();
        map.put("username", "aaa");
        map.put("password", "123");
        System.out.println(HttpClientUtils.doGet("http://localhost:9093/item/parem", map));
    }

}
