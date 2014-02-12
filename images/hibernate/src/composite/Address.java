package jp.dip.xlegend.model;

import java.io.Serializable;

/**
 * Address
 */
public class Address implements Serializable {
    
    private String zip;
    private String prefectural;
    private String city;

    public Address() {
        super();
    }
    

    /**
     * ésí¨ë∫
     * @return
     * 
     * @hibernate.property
     *      column="CITY"
     *      not-null="true"
     */
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    
    
    /**
     * ìsìπï{åß
     * @return
     * 
     * @hibernate.property
     *      column="PREFECTURAL"
     *      not-null="true"
     */
    public String getPrefectural() {
        return prefectural;
    }
    public void setPrefectural(String prefectural) {
        this.prefectural = prefectural;
    }
    
    
    /**
     * óXï÷î‘çÜ
     * @return
     * 
     * @hibernate.property
     *      column="ZIP"
     *      not-null="true"
     */
    public String getZip() {
        return zip;
    }
    public void setZip(String zip) {
        this.zip = zip;
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[Address:");
        buffer.append(" zip: ");
        buffer.append(zip);
        buffer.append(" prefectural: ");
        buffer.append(prefectural);
        buffer.append(" city: ");
        buffer.append(city);
        buffer.append("]");
        return buffer.toString();
    }
}
