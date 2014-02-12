package jp.dip.xlegend.model;

import java.io.Serializable;

/**
 * Category
 * 
 * @hibernate.class
 *      table="CATEGORY"
 */
public class Category implements Serializable {
    
    private Integer categoryNo;
    private String name;
    
    public Category() {
        super();
    }
    
    /**
     * カテゴリNo
     * @return
     * 
     * @hibernate.id
     *      column="CATEGORY_NO"
     *      generator-class="assigned"      
     */
    public Integer getCategoryNo() {
        return categoryNo;
    }
    public void setCategoryNo(Integer categoryNo) {
        this.categoryNo = categoryNo;
    }
    
    
    /**
     * カテゴリ名
     * @return
     * 
     * @hibernate.property 
     *      column="NAME"
     *      not-null="false"
     */
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    
    public String toString() {
        return "[カテゴリNo:" + categoryNo + "  名前：" + name + "  " +
                "@" + Integer.toHexString(super.hashCode()) + "]";
    }    

}
