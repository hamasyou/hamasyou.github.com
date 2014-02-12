package jp.dip.xlegend.model;

import java.io.Serializable;

/**
 * ProductAttribute
 *
 * @hibernate.class
 *      table="PRODUCT_ATTR"
 */
public class ProductAttribute implements Serializable {
    
    private Integer productNo;
    private Integer cost;

    public ProductAttribute() {
        super();
    }

    /**
     * ïiñ⁄No
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
     * å¥âø 
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
    
    
    public String toString() {
        return "[ïiñ⁄No:" + productNo + "  å¥âøÅF" + cost + "]";
    }    
}
