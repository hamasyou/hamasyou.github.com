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
        
        // ŒŸõˆ—
        List companys = session.find("from Company");
        Iterator iter = companys.iterator();
        while (iter.hasNext()) {
            Company c = (Company)iter.next();
            System.out.println(c);
            System.out.println(" „¤ ZŠ " + c.getAddress());
        }
        
        
        // “o˜^ˆ—
        Transaction transaction = session.beginTransaction();
        
        Company newCompany = new Company();
        newCompany.setCompanyNo(new Integer(3));
        newCompany.setName("Š”®‰ïĞ ƒvƒƒOƒ‰ƒ}[ƒY");
        
        Address addr = new Address();
        addr.setZip("192-1111");
        addr.setPrefectural("•Ÿ“‡Œ§");
        addr.setCity("‰ï’Ãá¼s");
        
        newCompany.setAddress(addr);
        session.saveOrUpdateCopy(newCompany);
        
        transaction.commit();
        
        
        session.close();
        factory.close();
    }
}
