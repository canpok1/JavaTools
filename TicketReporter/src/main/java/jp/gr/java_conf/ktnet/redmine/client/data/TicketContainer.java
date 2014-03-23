package jp.gr.java_conf.ktnet.redmine.client.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Issueを格納するクラスです。
 * @author tanabe
 *
 */
public class TicketContainer {

    /**
     * 保持しているTicket
     */
    private ArrayList<Ticket> tickets;
    
    /**
     * インスタンスを生成します。
     */
    public TicketContainer() {
        this.tickets = new ArrayList<Ticket>();
    }
    
    /**
     * IDが一致するTicketへの参照を取得します。
     * @param id TicketのID
     * @return IDが一致するTicketへの参照
     */
    public Ticket getIssueById(int id) {
        for(Ticket ticket : this.tickets) {
            if(ticket.getId().equals(id)) {
                return ticket;
            }
        }
        return null;
    }
    
    /**
     * 全てのTicketを取得します。
     * @return 全てのTicket
     */
    public List<Ticket> getAllI() {
        return this.tickets;
    }
    
    /**
     * Ticketを追加します。
     * @param ticket 追加するTicket
     */
    public void add(Ticket ticket) {
        this.tickets.add(ticket);
    }
    
    /**
     * Ticketを追加します。
     * @param other 追加するTicketを保持するコンテナ
     */
    public void addAll(TicketContainer other) {
        if(other == null) {
            throw new IllegalArgumentException("Arg must not be null");
        }
        
        this.tickets.addAll(other.tickets);
    }
}
