package jp.gr.java_conf.ktnet.redmine.client;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jp.gr.java_conf.ktnet.redmine.client.data.Ticket;
import jp.gr.java_conf.ktnet.redmine.client.data.TicketContainer;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * Restで取得したチケットデータを保持するクラスです。
 * @author tanabe
 */
public class TicketLoader {

    /**
     * コンストラクタは使用不可
     */
    private TicketLoader() {
    }

    /**
     * Ticket情報を読み込みます。
     * @param is RestApiで得られたxmlへのInputStream
     * @return 読み込んだTicket情報
     */
    public static TicketContainer load(InputStream is) {
        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            Node root = builder.parse(is);
            
            TicketContainer tickets = new TicketContainer();
            NodeList nodeList = root.getChildNodes();
            for(int i = 0; i < nodeList.getLength(); i++) {
                TicketContainer newTickets = loadIssuesTag(nodeList.item(i));
                if(newTickets != null) {
                    tickets.addAll(newTickets);
                }
            }
            
            return tickets;
        } catch (ParserConfigurationException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * issuesタグを読み込みます。
     * @param node issuesタグに対応するNode
     * @return 読み込んだ情報
     */
    private static TicketContainer loadIssuesTag(Node node) {
        if(node == null) {
            throw new IllegalArgumentException("arg is null");
        }
        // TODO 定数化
        if(!node.getNodeName().equals("issues")) {
            return null;
        }
        
        // issueタグを読み込む
        TicketContainer ticketList = new TicketContainer();
        NodeList childList = node.getChildNodes();
        for(int index = 0; index < childList.getLength(); index++ ) {
            Ticket data = loadIssueTag(childList.item(index));
            if(data != null) {
                ticketList.add(data);
            }
        }
        
        return ticketList;
    }
    
    /**
     * issueタグを読み込みます。
     * @param node タグに対応するNode
     * @return 読み込んだ情報。Nodeがタグに対応していない場合はnull。
     */
    private static Ticket loadIssueTag(Node node) {
        if(node == null) {
            throw new IllegalArgumentException("arg is null");
        }
        // TODO 定数化
        if(!node.getNodeName().equals("issue")) {
            return null;
        }
        
        Ticket.Builder builder = new Ticket.Builder();
        NodeList childList = node.getChildNodes();
        for(int index = 0; index < childList.getLength(); index++ ) {
            Node child = childList.item(index);
            
            if(child.getNodeName().equals("id")) {
                builder.id = loadTagText2Integer(child);
            }
            else if(child.getNodeName().equals("project")) {
                builder.project = loadTagAttribute2String(child, "name");
            }
            else if(child.getNodeName().equals("tracker")) {
                builder.tracker = loadTagAttribute2String(child, "name");
            }
            else if(child.getNodeName().equals("status")) {
                builder.status = loadTagAttribute2String(child, "name");
            }
            else if(child.getNodeName().equals("priority")) {
                builder.priority = loadTagAttribute2String(child, "name");
            }
            else if(child.getNodeName().equals("author")) {
                builder.author = loadTagAttribute2String(child, "name");
            }
            else if(child.getNodeName().equals("assigned_to")) {
                builder.assigned_to = loadTagAttribute2String(child, "name");
            }
            else if(child.getNodeName().equals("subject")) {
                builder.subject = loadTagText2String(child);
            }
            else if(child.getNodeName().equals("description")) {
                builder.description = loadTagText2String(child);
            }
            else if(child.getNodeName().equals("start_date")) {
                builder.start_date = loadTagText2Date(child, "yyyy-MM-dd");
            }
            else if(child.getNodeName().equals("due_date")) {
                builder.due_date = loadTagText2Date(child, "yyyy-MM-dd");
            }
            else if(child.getNodeName().equals("done_ratio")) {
                builder.done_ratio = loadTagText2Integer(child);
            }
            // TODO フォーマットを要調査
//            else if(child.getNodeName().equals("estimated_hours")) {
//                builder.estimated_hours = loadTagText2Date(child);
//            }
            else if(child.getNodeName().equals("created_on")) {
                builder.created_on = loadTagText2Date(child, "yyyy-MM-dd'T'HH:mm:ss'Z'");
            }
            else if(child.getNodeName().equals("updated_on")) {
                builder.updated_on = loadTagText2Date(child, "yyyy-MM-dd'T'HH:mm:ss'Z'");
            }
        }
        
        return builder.build();
    }
    
    
    /**
     * テキスト情報を文字列として読み込みます。
     * @param node 読み込み対象のNode
     * @return 読み込んだ情報。対応する情報がない場合はnull。
     */
    private static String loadTagText2String(Node node) {
        if(node == null) {
            throw new IllegalArgumentException("arg is null");
        }
        
        return node.getTextContent();
    }
    
    /**
     * テキスト情報を整数値として読み込みます。
     * @param node 読み込み対象のNode
     * @return 読み込んだ情報。対応する情報がない場合はnull。
     */
    private static Integer loadTagText2Integer(Node node) {
        String text = loadTagText2String(node);
        
        if(text == null) {
            return null;
        }
        
        try {
            return Integer.valueOf(text);
        }
        catch(NumberFormatException e) {
            System.out.println(e);
            return null;
        }
    }
    
    /**
     * テキスト情報を日付時刻として読み込みます。
     * @param node 読み込み対象のNode
     * @param pattern 日付のパターン
     * @return 読み込んだ情報。対応する情報がない場合はnull。
     */
    private static Date loadTagText2Date(Node node, String pattern) {
        String text = loadTagText2String(node);
        
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        
        try {
            return format.parse(text);
        } catch (ParseException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 属性を文字列として読み込みます。
     * @param node 読み込み対象のNode
     * @param attributeName 属性名
     * @return 読み込んだ情報。対応する情報がない場合はnull。
     */
    private static String loadTagAttribute2String(Node node, String attributeName) {
        if(node == null) {
            throw new IllegalArgumentException("node is null");
        }
        
        NamedNodeMap attributeMap = node.getAttributes();
        
        Node attributeNode = attributeMap.getNamedItem(attributeName);
        if(attributeNode == null) {
            return null;
        }
        return attributeNode.getNodeValue();
    }
    
    /**
     * 属性を整数値として読み込みます。
     * @param node 読み込み対象のNode
     * @param attributeName 属性名
     * @return 読み込んだ情報。対応する情報がない場合はnull。
     */
    private static Integer loadTagAttribute2Integer(Node node, String attributeName) {
        String text = loadTagAttribute2String(node, attributeName);
        
        if(text == null) {
            return null;
        }
        
        try {
            return Integer.valueOf(text);
        }
        catch(NumberFormatException e) {
            System.out.println(e);
            return null;
        }
    }
}
