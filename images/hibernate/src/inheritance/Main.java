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
        List products = session.find("from Product");
        Iterator iter = products.iterator();
        while (iter.hasNext()) {
            Product product = (Product)iter.next();
            System.out.println(product);
        }
        
        
        // �o�^����
        Transaction transaction = session.beginTransaction();
        
        Product newProduct = new ElectricProduct();
        newProduct.setProductNo(new Integer(5));
        newProduct.setName("�I�[�f�B�I�R���|");
        newProduct.setCost(new Integer(60000));
        
        session.saveOrUpdateCopy(newProduct);
        
        transaction.commit();
        
        
        session.close();
        factory.close();
    }
}
