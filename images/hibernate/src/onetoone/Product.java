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
     * �̔��i�ڑ���
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
        return "[�i��No:" + productNo + "  ���O�F" + name + "]";
    }
}
