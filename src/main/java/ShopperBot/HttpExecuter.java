package ShopperBot;


import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class HttpExecuter {

    private CloseableHttpClient httpclient = HttpClientBuilder.create()
            .setSSLHostnameVerifier(new NoopHostnameVerifier())
            .setConnectionTimeToLive(70L, TimeUnit.SECONDS)
            .setMaxConnTotal(100).build();
    private CloseableHttpResponse response;
    private static HttpExecuter exc;
    private HttpExecuter(){}
    public static HttpExecuter getHttpExecuter(){
        if (exc ==null)exc = new HttpExecuter();
        return exc;
    }

    public synchronized boolean isLinkValid(String link){
        try {
            response = httpclient.execute(new HttpGet(link));
            if (response.getStatusLine().getStatusCode()==200)return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public synchronized String[] getButtonsByName(String link, String buttonName){
        Document doc = null;
        String[]result = null;
        try {
            doc = Jsoup.connect(link).get();
            Elements btns = doc.select("button");
            Elements clss = doc.select("#new_addcart");
            btns.retainAll(clss);
            Elements names = doc.getElementsContainingText(buttonName);
            btns.retainAll(names);
            result = new String[btns.size()];
            for (int i = 0; i<btns.size();i++){
                result[i] = btns.get(i).toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public synchronized InputStream requestForStream (String request){
        try {
            response = httpclient.execute(new HttpGet(request));
            return response.getEntity().getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream getStreamForFileUrl(String url){
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
            return connection.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } return null;
    }
    public String[] getButtonIds(String link, String buttonName){
        String[] result = new String[1];
        return result;
    }

}
