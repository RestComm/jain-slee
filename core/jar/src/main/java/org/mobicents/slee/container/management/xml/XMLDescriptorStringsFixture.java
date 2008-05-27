/*
* The Open SLEE project
*
* The source code contained in this file is in in the public domain.
* It can be used in any project or product without prior permission,
* license or royalty payments. There is no claim of correctness and
* NO WARRANTY OF ANY KIND provided with this code.
*/
package org.mobicents.slee.container.management.xml;

import javax.slee.*;

/**
 * @author Emil Ivov
 * @version 1.0
 */

public class XMLDescriptorStringsFixture {
    
    public static String SBB_JAR_XML1 =
        "<?xml version=\"1.0\"?>" + "\n"
        //+"<!DOCTYPE sbb-jar PUBLIC \"-//Sun Microsystems, Inc.//DTD JAIN SLEE SBB 1.0//EN\" \"http://java.sun.com/dtd/slee-sbb-jar_1_0.dtd\">" + "\n"
        +"<sbb-jar>" + "\n"
        +"    <sbb>" + "\n"
        +"        <description>Null Activity Sbb</description>" + "\n"
        +"        <sbb-name>NullActivityTestSbb</sbb-name>" + "\n"
        +"        <sbb-vendor>org.mobicents</sbb-vendor>" + "\n"
        +"        <sbb-version>1.0</sbb-version>" + "\n"
        +"\n"
        +"        <sbb-ref>" + "\n"
        +"           <description></description>" + "\n"
        +"        	<sbb-name>NullActivityChildSbb</sbb-name>" + "\n"
        +"        	<sbb-vendor>org.mobicents</sbb-vendor>" + "\n"
        +"        	<sbb-version> 1.0 </sbb-version>" + "\n"
        +"        	<sbb-alias>NullActivityChildSbb</sbb-alias>" + "\n"
        +"        </sbb-ref>" + "\n"
        +"        <sbb-classes>" + "\n"
        +"            <sbb-abstract-class>" + "\n"
        +"                <sbb-abstract-class-name>org.mobicents.slee.test.services.sip.nullactivitytest.NullActivitySbb</sbb-abstract-class-name>" + "\n"
        +"             		<get-child-relation-method>" + "\n"
        +"             		       <description />" + "\n"
        +"             		       <sbb-alias-ref>NullActivityChildSbb</sbb-alias-ref>" + "\n"
        +"             		<get-child-relation-method-name>getNullActivityChildSbb</get-child-relation-method-name>" + "\n"
        +"             		<default-priority>1</default-priority>" + "\n"
        +"             		</get-child-relation-method>" + "\n"
        +"            </sbb-abstract-class>" + "\n"
        +"           <sbb-activity-context-interface>" + "\n"
        +"           		<description />" + "\n"
        +"           		<sbb-activity-context-interface-name>" + "\n"
        + 						"org.mobicents.slee.test.services.sip.nullactivitytest.NullActivitySbbActivityContextInterface" 
        +					"</sbb-activity-context-interface-name>" + "\n"
        +"           </sbb-activity-context-interface>" + "\n"
        +"        </sbb-classes>" + "\n"
        + "\n"
        +"		<event event-direction=\"FireAndReceive\" initial-event=\"true\">" + "\n"
        +"		<description>" + "\n"
        +"			This SBB fires events of the com.foobar.FooEvent.StartEvent" + "\n"
        +"			event type and assigns StartEvent as the event name of" + "\n"
        +"		    this event type." + "\n"
        +"       </description>" + "\n"
        +"       <event-name>Message</event-name>" + "\n"
        +"       <event-type-ref>" + "\n"
        +"		<event-type-name>javax.sip.message.Request.MESSAGE</event-type-name>" + "\n"
        +"       <event-type-vendor> javax.sip </event-type-vendor>" + "\n"
        +"       <event-type-version> 1.1 </event-type-version>" + "\n"
        +"       </event-type-ref>" + "\n"
        +"		<initial-event-select variable=\"EventType\"/>" + "\n"
        +"		</event>" + "\n"
        + "\n"
        +"    </sbb>" + "\n"  
        + "   <sbb>" + "\n"
        +"        <description>Child Sbb</description>" + "\n"
        +"        <sbb-name>NullActivityChildSbb</sbb-name>" + "\n"
        +"        <sbb-vendor>org.mobicents</sbb-vendor>" + "\n"
        +"        <sbb-version>1.0</sbb-version>" + "\n"
        +"\n"
        +"        <sbb-classes>" + "\n"
        +"            <sbb-abstract-class>" + "\n"
        +"                <sbb-abstract-class-name>org.mobicents.slee.test.services.sip.nullactivitytest.NullActivityChildSbb</sbb-abstract-class-name>" + "\n"
        +"            </sbb-abstract-class>" + "\n"
        +"                   <sbb-activity-context-interface>" + "\n"
        +"                   <description />" + "\n"
        +"                   <sbb-activity-context-interface-name>org.mobicents.slee.test.services.sip.nullactivitytest.NullActivitySbbActivityContextInterface</sbb-activity-context-interface-name>" + "\n"
        +"                   </sbb-activity-context-interface>" + "\n"
        +"        </sbb-classes>" + "\n"
        + "\n"
        +"		<event event-direction=\"Receive\">" + "\n"
        +"		<description>" + "\n"
        +"			This SBB is fired on a SIP MESSAGE" + "\n"
        +"       </description>" + "\n"
        +"       <event-name>Message</event-name>" + "\n"
        +"       <event-type-ref>" + "\n"
        +"		<event-type-name>javax.sip.message.Request.MESSAGE</event-type-name>" + "\n"
        +"       <event-type-vendor> javax.sip </event-type-vendor>" + "\n"
        +"       <event-type-version> 1.1 </event-type-version>" + "\n"
        +"       </event-type-ref>" + "\n"
        +"		</event>" + "\n"
        +"		<resource-adaptor-type-binding>" + "\n"
        +"		<resource-adaptor-type-ref>" + "\n"
        +"			<resource-adaptor-type-name>JAINSIP</resource-adaptor-type-name>" + "\n"
        +"		    <resource-adaptor-type-vendor>javax.sip</resource-adaptor-type-vendor>" + "\n"
        +"		    <resource-adaptor-type-version>1.1</resource-adaptor-type-version>" + "\n"
        +"		</resource-adaptor-type-ref>" + "\n"
        +"		<activity-context-interface-factory-name>" + "\n"
        +"		    slee/resources/jainsip/FactoryProvider" + "\n"
        +"		</activity-context-interface-factory-name>" + "\n"
        +"		<resource-adaptor-entity-binding>" + "\n"        
        +"		    <resource-adaptor-object-name>" + "\n"
        +"		        slee/resources/jainsip/1.1/provider" + "\n"
        +"		    </resource-adaptor-object-name>" + "\n"            
        +"		    <resource-adaptor-entity-link>" + "\n"
        +"		        local_nist" + "\n"
        +"		    </resource-adaptor-entity-link>" + "\n"
        +"		</resource-adaptor-entity-binding>" + "\n"
        +"		</resource-adaptor-type-binding>" + "\n"
        + "\n"
        +"    </sbb>" + "\n"
        +"</sbb-jar>" + "\n";
    
    public static String   SBB_JAR_XML =
         "<?xml version=\"1.0\"?>" + "\n"
        //TODO make the dtd accessible locally
        //+"<!DOCTYPE sbb-jar PUBLIC \"-//Sun Microsystems, Inc.//DTD JAIN SLEE SBB 1.0//EN\" \"http://java.sun.com/dtd/slee-sbb-jar_1_0.dtd\">" + "\n"
        +"<sbb-jar>" + "\n"
        +"    <sbb>" + "\n"
        +"        <description>JCC Call Forwarding SBB for JCC 1.0a</description>" + "\n"
        +"        <sbb-name>JCC Call Forwarding SBB</sbb-name>" + "\n"
        +"        <sbb-vendor>The Open Source Community</sbb-vendor>" + "\n"
        +"        <sbb-version>1.0</sbb-version>" + "\n"
        +"\n"
        +"        <profile-spec-ref>" + "\n"
        +"            <profile-spec-name>CallForwardingProfile</profile-spec-name>" + "\n"
        +"            <profile-spec-vendor>The Open Source Community</profile-spec-vendor>" + "\n"
        +"            <profile-spec-version>1.0</profile-spec-version>" + "\n"
        +"            <profile-spec-alias>CFP</profile-spec-alias>" + "\n"
        +"        </profile-spec-ref>" + "\n"
        +"\n"
        +"        <sbb-classes>" + "\n"
        +"            <sbb-abstract-class>" + "\n"
        +"                <sbb-abstract-class-name>org.mobicents.slee.services.InviteSbb</sbb-abstract-class-name>" + "\n"
        +"                <get-profile-cmp-method>" + "\n"
        +"                    <profile-spec-alias-ref>CFP</profile-spec-alias-ref>" + "\n"
        +"                    <get-profile-cmp-method-name>getProfile</get-profile-cmp-method-name>" + "\n"
        +"                </get-profile-cmp-method>" + "\n"
        +"            </sbb-abstract-class>" + "\n"
        +"        </sbb-classes>" + "\n"
        +"\n"
        +"        <event event-direction=\"Receive\" initial-event=\"True\">" + "\n"
        +"            <event-name>CallDeliveryEvent</event-name>" + "\n"
        +"            <event-type-ref>" + "\n"
        +"                <event-type-name>javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_AUTHORIZE_CALL_ATTEMPT</event-type-name>" + "\n"
        +"                <event-type-vendor>javax.csapi.cc.jcc</event-type-vendor>" + "\n"
        +"                <event-type-version>1.1</event-type-version>" + "\n"
        +"            </event-type-ref>" + "\n"
        +"            <initial-event-select variable=\"AddressProfile\" />" + "\n"
        +"            <event-resource-option>block</event-resource-option>" + "\n"
        +"        </event>" + "\n"
        +"    </sbb>" + "\n"
        +"    <sbb>" + "\n"
        +"        <description>JCC Call Blocking SBB for JCC 1.0a</description>" + "\n"
        +"        <sbb-name>JCC Call Blocking SBB</sbb-name>" + "\n"
        +"        <sbb-vendor>The Open Source Community</sbb-vendor>" + "\n"
        +"        <sbb-version>1.0</sbb-version>" + "\n"
        +"\n"
        +"        <profile-spec-ref>" + "\n"
        +"            <profile-spec-name>CallBlockingProfile</profile-spec-name>" + "\n"
        +"            <profile-spec-vendor>The Open Source Community</profile-spec-vendor>" + "\n"
        +"            <profile-spec-version>1.0</profile-spec-version>" + "\n"
        +"            <profile-spec-alias>CBP</profile-spec-alias>" + "\n"
        +"        </profile-spec-ref>" + "\n"
        +"\n"
        +"        <sbb-classes>" + "\n"
        +"            <sbb-abstract-class>" + "\n"
        +"                <sbb-abstract-class-name>com.opencloud.slee.services.jcc.callblocking.CallBlockingSbb</sbb-abstract-class-name>" + "\n"
        +"                <get-profile-cmp-method>" + "\n"
        +"                    <profile-spec-alias-ref>CBP</profile-spec-alias-ref>" + "\n"
        +"                    <get-profile-cmp-method-name>getProfile</get-profile-cmp-method-name>" + "\n"
        +"                </get-profile-cmp-method>" + "\n"
        +"            </sbb-abstract-class>" + "\n"
        +"        </sbb-classes>" + "\n"
        +"\n"
        +"        <event event-direction=\"Receive\" initial-event=\"True\">" + "\n"
        +"            <event-name>CallDeliveryEvent</event-name>" + "\n"
        +"            <event-type-ref>" + "\n"
        +"                <event-type-name>javax.csapi.cc.jcc.JccConnectionEvent.CONNECTION_AUTHORIZE_CALL_ATTEMPT</event-type-name>" + "\n"
        +"                <event-type-vendor>javax.csapi.cc.jcc</event-type-vendor>" + "\n"
        +"                <event-type-version>1.1</event-type-version>" + "\n"
        +"            </event-type-ref>" + "\n"
        +"            <initial-event-select variable=\"AddressProfile\" />" + "\n"
        +"            <event-resource-option>block</event-resource-option>" + "\n"
        +"        </event>" + "\n"
        +"    </sbb>" + "\n"
        +"</sbb-jar>" + "\n";

    public static String   EVENT_JAR_XML =
         "<?xml version=\"1.0\"?>" + "\n"
        //TODO make the dtd accessible locally
        //+"<!DOCTYPE sbb-jar PUBLIC \"-//Sun Microsystems, Inc.//DTD JAIN SLEE SBB 1.0//EN\" \"http://java.sun.com/dtd/slee-sbb-jar_1_0.dtd\">" + "\n"
        +"<event-jar>" + "\n"
        +"    <event-definition>" + "\n"
        +"        <event-type-name> com.foobar.event.HelpRequestedEvent </event-type-name>" + "\n"
        +"        <event-type-vendor> com.foobar </event-type-vendor>" + "\n"
        +"        <event-type-version> 1.0a </event-type-version>" + "\n"
        +"        <event-class-name> com.foobar.event.HelpRequestedEvent </event-class-name>" + "\n"
        +"    </event-definition>" + "\n"
        +"\n"
        +"    <event-definition>" + "\n"
        +"        <event-type-name> com.foobar.event.IVRCompletedEvent </event-type-name>" + "\n"
        +"        <event-type-vendor> com.foobar </event-type-vendor>" + "\n"
        +"        <event-type-version> 1.1 </event-type-version>" + "\n"
        +"        <event-class-name> com.foobar.event.IVRCompletedEvent </event-class-name>" + "\n"
        +"    </event-definition>" + "\n"
        +"</event-jar>" + "\n";

    public static String SERVICE_JAR_XML =
        "<?xml version=\"1.0\"?>" + "\n" +
        "<service-xml>" + "\n" +
        "    <service>" + "\n" +
        "        <description> A Test Service XML </description>" + "\n" +
        "        <service-name> FooService </service-name>" + "\n" +
        "        <service-vendor> com.foobar </service-vendor>" + "\n" +
        "        <service-version> 1.0 </service-version>" + "\n" +
        "        <root-sbb>" + "\n" +
        "            <description> The root sbb test reference </description>" + "\n" +
        "            <sbb-name> FooSBB </sbb-name>" + "\n" +
        "            <sbb-vendor> com.foobar </sbb-vendor>" + "\n" +
        "            <sbb-version> 1.0 </sbb-version>" + "\n" +
        "        </root-sbb>" + "\n" +
        "        <default-priority> 55 </default-priority>" + "\n" +
        "        <address-profile-table> MyAddressProfileTable </address-profile-table>" + "\n" +
        "        <resource-info-profile-table> MyResourceInfoProfileTable </resource-info-profile-table>" + "\n" +
        "    </service>" + "\n" +
        "</service-xml>";
    
    public static String PROFILE_SPECIFICATION_XML =
        "<?xml version=\"1.0\"?>" + "\n" +
        "<profile-spec-jar>" + "\n" +
        "<description>" + "\n" +
        "    This XML file defines the standard profile specifications described" + "\n" +
        "    in the SLEE specification." + "\n" +
        "</description>" + "\n" +
        "<profile-spec>" + "\n" +
        "    <description>" + "\n" +
        "        Profile specification for the standard Address Profile Table" + "\n" +
        "    </description>" + "\n" +
        "    <profile-spec-name>AddressProfileSpec</profile-spec-name>" + "\n" +
        "    <profile-spec-vendor>javax.slee</profile-spec-vendor>" + "\n" +
        "    <profile-spec-version>1.0</profile-spec-version>" + "\n" +
        "    <profile-classes>" + "\n" +
        "        <profile-cmp-interface-name>" + "\n" +
        "            javax.slee.profile.AddressProfileCMP" + "\n" +
        "        </profile-cmp-interface-name>" + "\n" +
        "    </profile-classes>" + "\n" +
        "    <profile-index unique=\"True\" >addresses</profile-index>" + "\n" +
        "</profile-spec>" + "\n" +
        "</profile-spec-jar>" + "\n";        
}
