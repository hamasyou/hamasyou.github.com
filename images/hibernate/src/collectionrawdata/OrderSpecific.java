package jp.dip.xlegend.model;

import java.io.Serializable;

import jp.dip.xlegend.model.type.OrderSpecificID;

/**
 * OrderSpecific
 * 
 * @hibernate.class
 *      table="ORDER_SPECIFIC"
 */
public class OrderSpecific implements Serializable {
    
    private OrderSpecificID specificId;
    private Integer money ;
    
    public OrderSpecific() {
        super();
    }

    
    /**
     * �󒍖��ׂ̎�L�[
     * @return
     * 
     * @hibernate.id
     *      generator-class="assigned"
     */
    public OrderSpecificID getSpecificId() {
        return specificId;
    }
    public void setSpecificId(OrderSpecificID specificId) {
        this.specificId = specificId;
    }
    /**
     * �l�i
     * @return
     * 
     * @hibernate.property
     *      column="MONEY"
     *      not-null="true"
     */
    public Integer getMoney() {
        return money;
    }
    public void setMoney(Integer money) {
        this.money = money;
    }
    
    
    public String toString() {
        return "[" + specificId +
                "  �l�i�F" + money + "  " +
                "@" + Integer.toHexString(super.hashCode()) + "]";
    }
}
