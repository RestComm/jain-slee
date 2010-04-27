/*
 * JBoss, Home of Professional Open Source
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */ 
package org.mobicents.examples.convergeddemo.seam.action;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public interface ShoppingCart
{
    public boolean getIsEmpty();

    public void addProduct(org.mobicents.examples.convergeddemo.seam.model.Product product, int quantity);
    public List<org.mobicents.examples.convergeddemo.seam.model.OrderLine> getCart();
    @SuppressWarnings("unchecked")
    public Map getCartSelection();

    public BigDecimal getSubtotal();
    public BigDecimal getTax();
    public BigDecimal getTotal();

    public void updateCart();
    public void resetCart();

    public void destroy();
}
