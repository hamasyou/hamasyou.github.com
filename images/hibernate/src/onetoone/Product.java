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
    
    private ProductAttribute attribute;
    
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
     * 販売品目属性
     * @return
     *
     * @hibernate.one-to-one 
     *      class="jp.dip.xlegend.model.ProductAttribute"
     *      cascade="all"
     */
    public ProductAttribute getAttribute() {
        return attribute;
    }
    public void setAttribute(ProductAttribute attribute) {
        this.attribute = attribute;
    }
    
    
    public String toString() {
        return "[品目No:" + productNo + "  名前：" + name + "]";
    }
}
