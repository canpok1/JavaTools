package jp.gr.java_conf.ktnet.redmine.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jp.gr.java_conf.ktnet.redmine.client.data.TicketContainer;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * Redmineから情報を取得するクライアントです。
 */
public class RedmineClient {

    private static final String ISSUE_URL = "/issues.xml";
    
    /**
     * RedmineへのURL
     */
    private String redmineUrl;
    
    /**
     * Redmineの情報を取得するインスタンスを生成します。
     * @param url RedmineへのURL
     */
    public RedmineClient(String url) {
        if(url == null) {
            throw new IllegalArgumentException("url must not be null");
        }
        
        this.redmineUrl = url;
    }

    /**
     * HTTPのGetリクエストを送信します。
     * @param filter 絞り込む条件
     * @return 取得したチケット情報
     * @throws 接続に失敗した場合
     */
    public TicketContainer getRequest(TicketFilter filter)
            throws IOException {
        if(filter == null) {
            throw new IllegalArgumentException("filter must not be null");
        }
        
        String url = this.buildRequestUrl(filter);
        System.out.println("url [" + url + "]");
        // HTTPのGetメソッド
        GetMethod httpget = new GetMethod(url);
        try {
            // レスポンス
            InputStream stream = httpRequest(httpget);
            if(stream == null) {
                return null;
            }
            TicketContainer tickets = TicketLoader.load(stream);
            return tickets;
        }
        finally {
            httpget.releaseConnection();
        }
    }
    
    /**
     * HTTPリクエストを送信します。
     * このメソッド呼び出し後、getMethodのreleseConnectionを呼び出してください。
     * 取得できない場合はnullを返します。
     * @param getMethod Get用のメソッド
     * @return 取得情報読み込み用InputStream
     * @throws HttpException
     * @throws IOException
     */
    private InputStream httpRequest(GetMethod getMethod)
            throws HttpException, IOException {
        HttpClient httpclient = new HttpClient();
        
        int statusCode = httpclient.executeMethod(getMethod);

        if(statusCode != HttpStatus.SC_OK) {
            System.err.println("Method failed: "
                    + getMethod.getStatusLine());
            return null;
        }

        // レスポンス
        return getMethod.getResponseBodyAsStream();
    }

    /**
     * チケット情報を取得するためのURLを組み立てます。
     * @param filter フィルター
     * @return URL
     */
    private String buildRequestUrl(TicketFilter filter) {
        String url = this.redmineUrl + ISSUE_URL;
        ArrayList<String> paramList = new ArrayList<String>();
        
        // TODO 定数化
        if(filter.project != null) {
            paramList.add("project_id=" + filter.project);
        }
        
        StringBuilder urlParam = new StringBuilder();
        for(String param : paramList) {
            if(urlParam.length() == 0) {
                urlParam.append("?" + param);
            }
            else {
                urlParam.append("&" + param);
            }
        }
        
        return url + urlParam.toString();
    }
}
