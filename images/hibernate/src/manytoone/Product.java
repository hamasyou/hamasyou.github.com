package jp.dip.xlegend.model;

import java.io.Serializable;

/**
 * Product
 * 
 * @hibernate.class
 *      table="PRODUCT"
 */
public class Product implements Serializable {
    
    private Integer productNo;    
    private String name;
    
    private Category category;
    
    public Product() {
        super();
    }
    
    /**
     * �i��No
     * @return
     * 
     * @hibernate.id
     *      column="PRODUCT_NO"
     *      generator-class="assigned"        
     */
    public Integer getProductNo() {
        return productNo;
    }
    public void setProductNo(Integer productNo) {
        this.productNo = productNo;
    }
    
    /**
     * �i�ږ�
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
    
    
    /**
     * �J�e�S��
     * @return
     * 
     * @hibernate.many-to-one
     *      column="CATEGORY_NO"
     *      class="jp.dip.xlegend.model.Category"
     *      not-null="true"
     *      cascade="all"
     */
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    
    public String toString() {        
        return "[�i��No:" + productNo + "  ���O�F" + name + "  " + 
               "@" + Integer.toHexString(super.hashCode()) + "]";        
    }
}
