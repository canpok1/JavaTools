package jp.gr.java_conf.ktnet.redmine;

import java.text.SimpleDateFormat;

import jp.gr.java_conf.ktnet.redmine.client.RedmineClient;
import jp.gr.java_conf.ktnet.redmine.client.TicketFilter;
import jp.gr.java_conf.ktnet.redmine.client.data.Ticket;
import jp.gr.java_conf.ktnet.redmine.client.data.TicketContainer;

/**
 * チケット情報のレポートを作成するクラスです。
 */
public class TicketReporter {

    /**
     * RedmineのURL
     */
    private static final String REDMINE_URL = "http://192.168.0.105";

    /**
     * プログラムのエントリポイントです。
     */
    public static void main(String[] args) {
        try {
            RedmineClient client = new RedmineClient(REDMINE_URL);
            TicketFilter filter = new TicketFilter();
            filter.project = "Reversi";
            TicketContainer container =  client.getRequest(filter);
            if(container == null) {
                System.out.println("取得できませんでした");
                System.exit(-1);
            }
            System.out.println("取得しました");
            System.out.println();
            
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            for(Ticket ticket : container.getAllI()) {
                String limit = format.format(ticket.getDueDate());
                System.out.println("期限[" + limit + "] "
                                  +"タイトル[" + ticket.getSubject() + "]");
            }
            
            System.exit(0);
        }
        catch(Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
