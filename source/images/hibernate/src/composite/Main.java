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
        
        // ��������
        List companys = session.find("from Company");
        Iterator iter = companys.iterator();
        while (iter.hasNext()) {
            Company c = (Company)iter.next();
            System.out.println(c);
            System.out.println(" �� �Z�� " + c.getAddress());
        }
        
        
        // �o�^����
        Transaction transaction = session.beginTransaction();
        
        Company newCompany = new Company();
        newCompany.setCompanyNo(new Integer(3));
        newCompany.setName("������� �v���O���}�[�Y");
        
        Address addr = new Address();
        addr.setZip("192-1111");
        addr.setPrefectural("������");
        addr.setCity("��Îᏼ�s");
        
        newCompany.setAddress(addr);
        session.saveOrUpdateCopy(newCompany);
        
        transaction.commit();
        
        
        session.close();
        factory.close();
    }
}
