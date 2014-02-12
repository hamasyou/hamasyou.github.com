package jp.dip.xlegend.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
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
     * ��No
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
     * �󒍓�
     * @return
     * 
     * @hibernate.property
     *      column="ORDER_DATE"
     *      not-null="true"
     *      type="timestamp"
     */
    public Timestamp getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    
    /**
     * �󒍖���
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
      
    
    /**
     * ���z���X�g
     * @return
     * 
     * @hibernate.list 
     *      lazy="true"
     *      table="ORDER_SPECIFIC"
     * @hibernate.collection-key
     *      column="ORDER_NO"
     * @hibernate.collection-index
     *      column="SPECIFIC_NO"
     *      type="integer" 
     * @hibernate.collection-element 
     *      column="MONEY"
     *      type="integer" 
     */
    public List getMoneyList() {
        return moneyList;
    }    
    public  void setMoneyList(List moneys) {
        this.moneyList = moneys;
    }
    
    private List moneyList;
    
    
    public String toString() {        
        return "[��No:" + orderNo + "  �󒍓��F" + orderDate + "  " + 
               "@" + Integer.toHexString(super.hashCode()) + "]";        
    }
}
