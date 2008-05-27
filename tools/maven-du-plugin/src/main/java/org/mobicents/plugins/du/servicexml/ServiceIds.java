package org.mobicents.plugins.du.servicexml;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ServiceIds {

	private final ArrayList<String> ids;
	
	public static ServiceIds parse(Element element) {
		ArrayList<String> ids = new ArrayList<String>();
		
		/*
		 <service-xml>
    		<service>
        		<service-name>SimpleCallSetupTerminatedByServerWithDialogsTestService</service-name>
        		<service-vendor>org.mobicents</service-vendor>
        		<service-version>1.0</service-version>
        		...
    		</service>
		</service-xml>
		 */
		NodeList nodeList = element.getElementsByTagName("service");
		for(int i=0;i<nodeList.getLength();i++) {
			Element service = (Element) nodeList.item(i);
			String name = ((Element)service.getElementsByTagName("service-name").item(0)).getTextContent();
			String vendor = ((Element)service.getElementsByTagName("service-vendor").item(0)).getTextContent();
			String version = ((Element)service.getElementsByTagName("service-version").item(0)).getTextContent();
			ids.add(name+"#"+vendor+"#"+version);
		}
		return new ServiceIds(ids);
	}
	
	public ServiceIds(ArrayList<String> ids) {
		this.ids = ids;
	}
	
	public ArrayList<String> getIds() {
		return ids;
	}
}
