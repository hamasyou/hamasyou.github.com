package jp.dip.xlegend.model.type;

import java.io.Serializable;

/**
 * OrderSpecificID
 * �󒍖��חp�̎�L�[�N���X
 */
public class OrderSpecificID implements Serializable {
    private Integer orderNo;
    private Integer specificNo;
    
    /**
     * ��No
     * 
     * @hibernate.property
     *      column="ORDER_NO"
     */
    public Integer getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }
    
    
    /**
     * �󒍍sNo
     * 
     * @hibernate.property
     *      column="SPECIFIC_NO"
     */
    public Integer getSpecificNo() {
        return specificNo;
    }
    public void setSpecificNo(Integer specificNo) {
        this.specificNo = specificNo;
    }        
    
    
    public String toString() {
        return "��No:" + orderNo + "  �󒍍sNo�F" + specificNo;
    }
    
    
    public boolean equals(Object obj) {
        if (obj instanceof OrderSpecificID) {
            OrderSpecificID id = (OrderSpecificID)obj;
            if (id.orderNo.equals(orderNo) &&
                id.specificNo.equals(specificNo)) {
                return true;
            }
        }
        return false;
    }
    
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + orderNo.hashCode();
        hashCode = 31 * hashCode + specificNo.hashCode();        
        return hashCode;
    }
}