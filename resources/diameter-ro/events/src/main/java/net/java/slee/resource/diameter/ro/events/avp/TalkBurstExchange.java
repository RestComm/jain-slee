package net.java.slee.resource.diameter.ro.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * Defines an interface representing the Talk-Burst-Exchange grouped AVP type.<br>
 * <br>
 * From the Diameter Ro Reference Point Protocol Details (3GPP TS 32.299 V7.1.0) specification:
 * <pre>
 * 7.2.108 Talk-Burst-Exchange AVP 
 * The Talk-Burst-Exchange AVP (AVP code 860) is of type Grouped and holds the talk burst related charging data. 
 * 
 * It has the following ABNF grammar: 
 *  Talk-Burst-Exchange ::= AVP Header: 860 
 *      [ Number-Of-Talk-Bursts ] #exclude 
 *      [ Talk-Burst-Volume ] #exclude 
 *      [ Talk-Bursts-Time ] #exclude 
 *      [ Number-Of-Received-Talk-Bursts ] #exclude 
 *      [ Received-Talk-Burst-Volume ] #exclude 
 *      [ Received-Talk-Burst-Time ] #exclude
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface TalkBurstExchange extends GroupedAvp {
  // Do we need this?
}
