package jp.gr.java_conf.ktnet.redmine.client.data;

import java.util.Date;

public class Ticket {

    private final Builder builder;
    
    /**
     * インスタンスを生成します。
     * @param builder 生成元
     */
    private Ticket(Builder builder) {
        this.builder = builder;
    }
    
    /**
     * チケットのIDを取得します。
     * @return チケットのID
     */
    public Integer getId() {
        return this.builder.id;
    }
    
    /**
     * チケット名を取得します。
     * @return チケット名
     */
    public String getSubject() {
        return this.builder.subject;
    }
    
    /**
     * 担当者を取得します。
     * @return 担当者
     */
    public String getAssignedTo() {
        return this.builder.assigned_to;
    }
    
    /**
     * 期日を取得します。
     * @return 期日
     */
    public Date getDueDate() {
        return this.builder.due_date;
    }
    
    @Override
    public String toString() {
        return builder.toString();
    }
    
    /**
     * Issuesのインスタンスを組み立てるクラスです。
     * @author tanabe
     *
     */
    public static class Builder {
        public Integer id;
        public String project;
        public String tracker;
        public String status;
        public String priority;
        public String author;
        public String assigned_to;
        public String category;
        public String subject;
        public String description;
        public Date start_date;
        public Date due_date;
        public Integer done_ratio;
        public Date estimated_hours;
        public Date created_on;
        public Date updated_on;
        
        /**
         * Issueインスタンスを生成します。
         * @return Issueインスタンス
         */
        public Ticket build() {
            return new Ticket(this);
        }
        
        @Override
        public String toString() {
            return this.subject;
        }
    }
}
