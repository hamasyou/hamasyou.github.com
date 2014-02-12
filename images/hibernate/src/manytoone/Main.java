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
            Category category = product.getCategory();
            
            System.out.println(product + " " + category);
        }
        
        
        // 登録処理
        Transaction transaction = session.beginTransaction();
        
        Product newProduct = new Product();
        newProduct.setProductNo(new Integer(5));        
        newProduct.setName("長袖の服");
        
        Category newCategory = new Category();
        newCategory.setCategoryNo(new Integer(3));
        newCategory.setName("衣服");        
        
        newProduct.setCategory(newCategory);
        session.saveOrUpdateCopy(newProduct);
        
        transaction.commit();
        
        
        session.close();
        factory.close();
    }
}
