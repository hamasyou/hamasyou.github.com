package jp.dip.xlegend.model;

import java.io.Serializable;

/**
 * Product
 * 
 * @hibernate.class
 *      table="PRODUCT"
 *      discriminator-value="0"
 * @hibernate.discriminator
 *      column="CATEGORY"
 */
public abstract class Product implements Serializable {
    
    private Integer productNo;
    private String name;
    private Integer cost;

    
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
     * ����
     * @return
     * 
     * @hibernate.property
     *      column="COST"
     *      not-null="true"
     */
    public Integer getCost() {
        return cost;
    }
    public void setCost(Integer cost) {
        this.cost = cost;
    }
    
    
    public abstract Integer calcCost();
    
    
    public String toString() {
        return "[�i��No:" + productNo + "  ���O�F" + name + 
               " #" + getClass() +
               "@" + Integer.toHexString(super.hashCode()) + "]";
    }
}
