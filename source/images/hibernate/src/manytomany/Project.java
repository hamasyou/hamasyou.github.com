package jp.dip.xlegend.model;

import java.io.Serializable;
import java.util.Set;

/**
 * Project
 * 
 * @hibernate.class
 *      table="PROJECT"
 */
public class Project implements Serializable {
    
    private Integer projectNo;
    private String name;
    
    private Set employees;
    
    public Project() {
        super();
    }

    
    /**
     * �v���W�F�N�gNo
     * @return
     * 
     * @hibernate.id
     *      column="PROJECT_NO"
     *      generator-class="assigned"
     */
    public Integer getProjectNo() {
        return projectNo;
    }
    public void setProjectNo(Integer projectNo) {
        this.projectNo = projectNo;
    }
    
    
    /**
     * ���O
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
     * ���蓖�Ă�ꂽ�Ј�
     * @return
     * 
     * @hibernate.set
     *      inverse="true"
     *      cascade="all"
     *      lazy="true"
     *      table="PROJECT_ASSIGN"
     * @hibernate.collection-key
     *      column="PROJECT_NO"
     * @hibernate.collection-many-to-many
     *      column="EMP_NO"
     *      class="jp.dip.xlegend.model.Employee"
     */
    public Set getEmployees() {
        return employees;
    }
    public void setEmployees(Set employees) {
        this.employees = employees;
    }
    
    
    public String toString() {
        return "[�v���W�F�N�gNO�F" + projectNo +
                "  ���O�F" + name + "  " +
                "@" + Integer.toHexString(super.hashCode()) + "]";
    }
}
