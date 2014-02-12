package jp.dip.xlegend.model;

import java.util.*;

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
        List orders = session.find("from Order");
        Iterator iter = orders.iterator();
        while (iter.hasNext()) {
            Order o = (Order)iter.next();
            List money = o.getMoneyList();
            
            Iterator i = money.iterator();
            System.out.println(o);
            while (i.hasNext()) {
                System.out.println("Ñ§ Ç®ã‡ÅF" + i.next());
            }
        }
        
        session.close();
        factory.close();
    }
}
