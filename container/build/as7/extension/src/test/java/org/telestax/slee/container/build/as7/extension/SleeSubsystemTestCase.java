package org.telestax.slee.container.build.as7.extension;

import java.io.IOException;

import org.jboss.as.subsystem.test.AbstractSubsystemBaseTest;

/**
 * 
 * @author mobicents
 *
 */
public class SleeSubsystemTestCase extends AbstractSubsystemBaseTest {

    public SleeSubsystemTestCase() {
        super(SleeExtension.SUBSYSTEM_NAME, new SleeExtension());
    }

    @Override
    protected String getSubsystemXml() throws IOException {
        return readResource("subsystem.xml");
    }
    
}
