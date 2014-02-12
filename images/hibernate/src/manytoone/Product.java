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
     * 品目No
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
     * 品目名
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
     * カテゴリ
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
        return "[品目No:" + productNo + "  名前：" + name + "  " + 
               "@" + Integer.toHexString(super.hashCode()) + "]";        
    }
}
