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
            // One-to-One �Ŋ֘A�t������Ă���̂ŁA
            // ���ڎ擾�ł���B
            Product product = (Product)iter.next();
            ProductAttribute attr = product.getAttribute();
            
            System.out.println(product + " " + attr);
        }
        
        
        // �o�^����
        Transaction transaction = session.beginTransaction();
        
        Product newProduct = new Product();
        newProduct.setProductNo(new Integer(4));
        newProduct.setName("�e���r");
        
        ProductAttribute attr = new ProductAttribute();
        attr.setProductNo(new Integer(4));
        attr.setCost(new Integer(40000));
        
        newProduct.setAttribute(attr);
        session.saveOrUpdateCopy(newProduct);
        
        transaction.commit();
        
        
        session.close();
        factory.close();
    }
}
