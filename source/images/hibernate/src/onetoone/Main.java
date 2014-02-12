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
        
        // 検索処理
        List products = session.find("from Product");
        Iterator iter = products.iterator();
        while (iter.hasNext()) {
            // One-to-One で関連付けされているので、
            // 直接取得できる。
            Product product = (Product)iter.next();
            ProductAttribute attr = product.getAttribute();
            
            System.out.println(product + " " + attr);
        }
        
        
        // 登録処理
        Transaction transaction = session.beginTransaction();
        
        Product newProduct = new Product();
        newProduct.setProductNo(new Integer(4));
        newProduct.setName("テレビ");
        
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
