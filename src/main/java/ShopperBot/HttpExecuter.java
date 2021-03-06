package ShopperBot;


import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class HttpExecuter {


    public static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";

    private CookieStore cookieStore = new BasicCookieStore();
    private CloseableHttpClient httpclient = HttpClientBuilder.create()
            .setSSLHostnameVerifier(new NoopHostnameVerifier())
            .setConnectionTimeToLive(70L, TimeUnit.SECONDS)
            .setMaxConnTotal(100)
            .setDefaultCookieStore(cookieStore)
            .build();

    HttpClientContext context = HttpClientContext.create();
    {
        context.setCookieStore(cookieStore);
    }

    static String phantomJsPath;
    private CloseableHttpResponse response;
    private static HttpExecuter exc;
    private HttpExecuter(){
    }
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
    // todo переделать чтобы искал совпадения из списка слов в сыром html
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

    public long pingCounter(String link){
        long result = -1;
        try {

            HttpGet get = new HttpGet(link);
            long temp = System.currentTimeMillis();
            response = httpclient.execute(get);
            long temp2 = System.currentTimeMillis();
            result = temp2-temp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void executeRequest(String link){
        try {
            HttpGet get = new HttpGet(link);
            response = httpclient.execute(get, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            connection.setRequestProperty("User-Agent",USER_AGENT);
            return connection.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } return null;
    }

//    public static void main(String[] args) throws IOException {
//        WebClient client = new WebClient();
//        HtmlPage page1 = client.getPage("http://ya.ru");
//        HtmlTextInput input = (HtmlTextInput)page1.getElementsByTagName("input").get(1);
//        input.setText("sequence");
//        HtmlButton button = (HtmlButton) page1.getElementsByTagName("button").get(0);
//        HtmlPage page2 = button.click();
//        System.out.println(page2.getUrl());
//    }
}
