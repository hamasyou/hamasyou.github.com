package jp.dip.xlegend.model;

import java.sql.Timestamp;
import java.util.*;

import jp.dip.xlegend.model.type.OrderSpecificID;

import net.sf.hibernate.*;
import net.sf.hibernate.cfg.*;

/**
 * Main
 */
public class Main {
    
    public static void main(String[] args) throws Exception {
        
        Configuration conf = new Configuration().configure(); 
        SessionFactory factory = conf.buildSessionFactory();
        Session session = factory.openSession();
        
        // åüçıèàóù
        List products = session.find("from Order");
        Iterator iter = products.iterator();        
        while (iter.hasNext()) {
            Order product = (Order)iter.next();
            Set specifics = product.getSpecifics();
            
            Iterator specificIter = specifics.iterator();
            System.out.println(product);
            while (specificIter.hasNext()) {
                OrderSpecific s = (OrderSpecific)specificIter.next();
                System.out.println("  Ñ§ " + s);
            }
        }
        
        
        // ìoò^èàóù
        Transaction transaction = session.beginTransaction();
        
        Order newOrder = new Order();
        newOrder.setOrderNo(new Integer(3));        
        newOrder.setOrderDate(new Timestamp(System.currentTimeMillis()));
        
        // ñæç◊ÇÃçÏê¨
        OrderSpecific specific1 = new OrderSpecific();
        OrderSpecificID id = new OrderSpecificID();
        id.setOrderNo(new Integer(3));
        id.setSpecificNo(new Integer(1));
        specific1.setSpecificId(id);
        specific1.setMoney(new Integer(5000));
        
        OrderSpecific specific2 = new OrderSpecific();
        OrderSpecificID id2 = new OrderSpecificID();
        id2.setOrderNo(new Integer(3));
        id2.setSpecificNo(new Integer(2));
        specific2.setSpecificId(id2);
        specific2.setMoney(new Integer(3000));        
        
        Set specificSet = new HashSet();
        specificSet.add(specific1);
        specificSet.add(specific2);        
        newOrder.setSpecifics(specificSet);
        session.saveOrUpdateCopy(newOrder);
        
        transaction.commit();
        
        
        session.close();
        factory.close();
    }
}
