/*
 * The Open SLEE Project.
 * 
 * This Code is in the public Domain
 */
package org.mobicents.slee.container.component;

/**
 * EJBReference implementation.
 * 
 * @author Emil Ivov
 * @version 1.0
 */

public class EJBReference {
    private String ejbRefName;
    private String ejbRefType;
    private String ejbHome;
    private String ejbRemote;
    private String ejbLink;
    private String jndiName;

    public EJBReference(String ejbRefName, String ejbRefType, String ejbHome,
            			String ejbRemote, String ejbLink)
    {
        this.ejbRefName = ejbRefName;
        this.ejbRefType = ejbRefType;
        this.ejbHome = ejbHome;
        this.ejbRemote = ejbRemote;
        this.ejbLink = ejbLink;        
    }

    public String getEjbRefName()
    {
        return ejbRefName;
    }

    public String getEjbRefType()
    {
        return ejbRefType;
    }

    public String getEjbHome()
    {
        return ejbHome;
    }

    public String getEjbRemote()
    {
        return ejbRemote;
    }

    public String getEjbLink()
    {
        return ejbLink;
    }
    
    public String getJndiName()
    {
        return jndiName;
    }
   
    public void setJndiName(String jndiName) { this.jndiName = jndiName; }

}
