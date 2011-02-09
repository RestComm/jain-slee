package org.mobicents.ant;

import org.mobicents.util.DTDResolver;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.File;
 
public class SleeDTDResolver extends DTDResolver {

    private static final String DTD_DIRECTORY = "dtd";

    public SleeDTDResolver() {
        super();
        try {        	
            registerDTDURL("-//Sun Microsystems, Inc.//DTD JAIN SLEE Deployable Unit 1.0//EN",getDTDURL("slee-deployable-unit_1_0.dtd"));
            registerDTDURL("-//Sun Microsystems, Inc.//DTD JAIN SLEE SBB 1.0//EN",getDTDURL("slee-sbb-jar_1_0.dtd"));
            registerDTDURL("-//Sun Microsystems, Inc.//DTD JAIN SLEE Profile Specification 1.0//EN",getDTDURL("slee-profile-spec-jar_1_0.dtd"));
            registerDTDURL("-//Sun Microsystems, Inc.//DTD JAIN SLEE Event 1.0//EN",getDTDURL("slee-event-jar_1_0.dtd"));
            registerDTDURL("-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor Type 1.0//EN",getDTDURL("slee-resource-adaptor-type-jar_1_0.dtd"));
            registerDTDURL("-//Sun Microsystems, Inc.//DTD JAIN SLEE Resource Adaptor 1.0//EN",getDTDURL("slee-resource-adaptor-jar_1_0.dtd"));
            registerDTDURL("-//Sun Microsystems, Inc.//DTD JAIN SLEE Service 1.0//EN",getDTDURL("slee-service-xml_1_0.dtd"));
        } catch(MalformedURLException e) {
            throw new RuntimeException("Caught MalformedURLException: "+e);
        }
    }

    private static URL getDTDURL(String dtdName) throws MalformedURLException {
        return new File(DTD_DIRECTORY+File.separator+dtdName).toURL();
    }
}
