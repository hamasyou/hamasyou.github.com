package jp.dip.xlegend.model;

import java.io.Serializable;

/**
 * Company
 * 
 * @hibernate.class
 *      table="COMPANY" 
 */
public class Company implements Serializable {
    
    private Integer companyNo;
    private String name;
    private Address address;
    

    public Company() {
        super();
    }

    
    /**
     * ZŠ
     * @return
     * 
     * @hibernate.component
     *      class="jp.dip.xlegend.model.Address"
     */
    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) {
        this.address = address;
    }
    
    
    /**
     * ‰ïĞNo
     * @return
     * 
     * @hibernate.id
     *      column="COMPANY_NO"
     *      generator-class="assigned"
     */
    public Integer getCompanyNo() {
        return companyNo;
    }
    public void setCompanyNo(Integer companyNo) {
        this.companyNo = companyNo;
    }
    
    
    /**
     * ‰ïĞ–¼
     * @return
     * 
     * @hibernate.property
     *      column="NAME"
     *      not-null="true"
     */
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[Company:");
        buffer.append(" companyNo: ");
        buffer.append(companyNo);
        buffer.append(" name: ");
        buffer.append(name);
        buffer.append("]");
        return buffer.toString();
    }
}
