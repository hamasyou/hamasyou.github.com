package jp.dip.xlegend.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

/**
 * Order
 * 
 * @hibernate.class
 *      table="ORDER_HEADER"
 */
public class Order implements Serializable {
    
    private Integer orderNo;    
    private Timestamp orderDate;
    
    private Set specifics;
    
    public Order() {
        super();
    }
    
    /**
     * 受注No
     * @return
     * 
     * @hibernate.id
     *      column="ORDER_NO"
     *      generator-class="assigned"
     */
    public Integer getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }   
    
    
    /**
     * 受注日
     * @return
     * 
     * @hibernate.property
     *      column="ORDER_DATE"
     *      not-null="true"
     *      type="java.sql.Timestamp"
     */
    public Timestamp getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    
    /**
     * 受注明細
     * @return
     * 
     * @hibernate.set
     *      table="ORDER_SPECIFIC"
     *      cascade="all"
     *      lazy="true"
     * @hibernate.collection-key
     *      column="ORDER_NO"
     * @hibernate.collection-one-to-many 
     *      class="jp.dip.xlegend.model.OrderSpecific"
     */
    public Set getSpecifics() {
        return specifics;
    }
    public void setSpecifics(Set specifics) {
        this.specifics = specifics;
    }
    
    
    public String toString() {        
        return "[受注No:" + orderNo + "  受注日：" + orderDate + "  " + 
               "@" + Integer.toHexString(super.hashCode()) + "]";        
    }
}
