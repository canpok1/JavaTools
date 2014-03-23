package jp.gr.java_conf.ktnet.redmine.client;

/**
 * フィルターの条件を表すクラスです。
 * @author tanabe
 *
 */
public class TicketFilter {

    /**
     * ステータスを表す列挙
     * @author tanabe
     *
     */
    public enum Status {
        OPEN,   // 未完了
        CLOSED, // 完了
        ALL     // すべて
    }
    
    /**
     * プロジェクト
     */
    public String project = null;
    
    /**
     * トラッカーID
     */
    public String tracker_id = null;
    
    /**
     * ステータス
     */
    public Status status = Status.ALL;
    
    /**
     * 担当者
     */
    public Integer assignedTo = null;
    
}
