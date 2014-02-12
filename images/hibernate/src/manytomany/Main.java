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
        
        // 検索処理(Employee -> Project)
        System.out.println("--- 社員からプロジェクトを検索 ---");
        List employees = session.find("from Employee");
        Iterator iter = employees.iterator();        
        while (iter.hasNext()) {
            Employee emp = (Employee)iter.next();
            Set projects = emp.getProjects();
            
            Iterator projectIter = projects.iterator();
            System.out.println(emp);
            while (projectIter.hasNext()) {
                Project p = (Project)projectIter.next();
                System.out.println("  └ " + p);
            }
        }
        
        
        // 検索処理(Project -> Employee)
        System.out.println("--- プロジェクトから社員を検索 ---");        
        List projects = session.find("from Project");
        Iterator projectsIter = projects.iterator();        
        while (projectsIter.hasNext()) {
            Project project = (Project)projectsIter.next();
            Set empSet = project.getEmployees();
            
            Iterator empSetIter = empSet.iterator();
            System.out.println(project);
            while (empSetIter.hasNext()) {
                Employee e = (Employee)empSetIter.next();
                System.out.println("  └ " + e);
            }
        }        
        
        
        // 登録処理
//        Transaction transaction = session.beginTransaction();
//        
//        Employee newOrder = new Employee();
//        newOrder.setOrderNo(new Integer(3));        
//        newOrder.setOrderDate(new Timestamp(System.currentTimeMillis()));
//        
//        // 明細の作成
//        Project specific1 = new Project();
//        OrderSpecificID id = new OrderSpecificID();
//        id.setOrderNo(new Integer(3));
//        id.setSpecificNo(new Integer(1));
//        specific1.setSpecificId(id);
//        specific1.setMoney(new Integer(5000));
//        
//        Project specific2 = new Project();
//        OrderSpecificID id2 = new OrderSpecificID();
//        id2.setOrderNo(new Integer(3));
//        id2.setSpecificNo(new Integer(2));
//        specific2.setSpecificId(id2);
//        specific2.setMoney(new Integer(3000));        
//        
//        Set specificSet = new HashSet();
//        specificSet.add(specific1);
//        specificSet.add(specific2);        
//        newOrder.setSpecifics(specificSet);
//        session.saveOrUpdateCopy(newOrder);
//        
//        transaction.commit();
        
        
        session.close();
        factory.close();
    }
}
