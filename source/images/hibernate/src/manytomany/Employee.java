package jp.dip.xlegend.model;

import java.io.Serializable;
import java.util.Set;

/**
 * Employee
 * 
 * @hibernate.class
 *      table="EMPLOYEE"
 */
public class Employee implements Serializable {
    
    private Integer empNo;    
    private String name;
    
    private Set projects;
    
    public Employee() {
        super();
    }
    
    
    /**
     * 社員No
     * @return
     * 
     * @hibernate.id
     *      column="EMP_NO"
     *      generator-class="assigned"
     */
    public Integer getEmpNo() {
        return empNo;
    }
    public void setEmpNo(Integer empNo) {
        this.empNo = empNo;
    }
    
    
    /**
     * 名前
     * @return
     * 
     * @hibernate.property
     *      column="NAME"
     *      not-null="true"
     */
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    
    /**
     * 割り当てプロジェクト
     * @return 
     * 
     * @hibernate.set
     *      cascade="all"
     *      lazy="true"
     *      table="PROJECT_ASSIGN"
     * @hibernate.collection-key 
     *      column="EMP_NO"
     * @hibernate.collection-many-to-many 
     *      column="PROJECT_NO"
     *      class="jp.dip.xlegend.model.Project"
     */
    public Set getProjects() {
        return projects;
    }    
    public void setProjects(Set projects) {
        this.projects = projects;
    }
    
    
    public String toString() {        
        return "[社員No:" + empNo + "  名前：" + name + "  " + 
               "@" + Integer.toHexString(super.hashCode()) + "]";        
    }
}
