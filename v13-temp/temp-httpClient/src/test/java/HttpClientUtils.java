import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;

/**
 * @author maizifeng
 * @Date 2019/6/18
 */
public class HttpClientUtils {
    private HttpClientUtils(){}

    public static String doGet(String url, Map<String,String> params){
        CloseableHttpClient client = HttpClients.createDefault();

        try {
            URIBuilder uriBuilder=new URIBuilder(url);
            if(params!=null){
                Set<Map.Entry<String, String>> entries = params.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    uriBuilder.addParameter(entry.getKey(), entry.getValue());
                }
            }
            HttpGet get=new HttpGet(uriBuilder.build());
            CloseableHttpResponse response = null;
            response = client.execute(get);
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode==200){
                return EntityUtils.toString(response.getEntity());
            }else{
                return statusCode+" ";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return "error";
        } finally {
            if(client!=null){
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static String doGet(String url){

       return doGet(url, null);
    }
}
