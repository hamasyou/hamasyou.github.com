/*
 * ì¬“ú: 2004/11/18
 * ì¬Ò: hamasyou
 */
package jp.dip.xlegend.model;

/**
 * ElectricProduct
 * 
 * @hibernate.subclass 
 *      discriminator-value="1"
 */
public class ElectricProduct extends Product {

    public ElectricProduct() {
        super();
    }
    
    
    public Integer calcCost() {
        return new Integer((int)(getCost().intValue() * 1.05));
    }
}
