/*
 * Created on Oct 28, 2004
 * 
 * The Open SLEE Project
 * 
 * A SLEE for the People
 * 
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.container.management.jmx;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.slee.facilities.Level;

import org.jboss.util.propertyeditor.TextPropertyEditorSupport;

/**
 * 
 * TODO Class Description
 * 
 * @author F.Moggia
 */
public class PropertiesPropertyEditor extends TextPropertyEditorSupport {
    public void setAsText( String props ) {
        Properties p = new Properties();
        ByteArrayInputStream is;
        try {
            is = new ByteArrayInputStream( props.getBytes() );
            p.load(is);
            this.setValue(p);
            
        } catch (IOException e1) {
            throw new IllegalArgumentException(e1.getMessage());
        }
        
    }
}
