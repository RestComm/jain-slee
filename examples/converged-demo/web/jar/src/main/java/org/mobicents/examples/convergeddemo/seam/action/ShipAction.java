/*
 * JBoss, Home of Professional Open Source
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */ 
package org.mobicents.examples.convergeddemo.seam.action;

import java.io.Serializable;

import javax.ejb.Remove;
import javax.ejb.Stateful;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.bpm.BeginTask;
import org.jboss.seam.annotations.bpm.EndTask;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.mobicents.examples.convergeddemo.seam.model.Order;

@Stateful
@Name("ship")
public class ShipAction
    implements Ship,
               Serializable
{
    private static final long serialVersionUID = -5284603520443473953L;
    
    @In 
    Order order;
    
    String track;

    @NotNull
    @Length(min=4,max=10)
    public String getTrack() {
        return track;
    }
    
    public void setTrack(String track) {
        this.track=track;
    }

    @BeginTask
    public String viewTask() {
        return "ship";
    }
    
    @EndTask
    public String ship() {        
        order.ship(track);
        return "admin";
    }

    @Remove
    public void destroy() { }
}
