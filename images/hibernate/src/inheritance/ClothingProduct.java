/*
 * ì¬“ú: 2004/11/18
 * ì¬Ò: hamasyou
 */
package jp.dip.xlegend.model;

/**
 * ClothingProduct
 * 
 * @hibernate.subclass
 *      discriminator-value="2" 
 */
public class ClothingProduct extends Product {
    
    public ClothingProduct() {
        super();
    }

    
    public Integer calcCost() {
        return new Integer((int)(getCost().intValue() * 0.8));
    }
}
