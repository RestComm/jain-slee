package org.mobicents.eclipslee.wizard.project;

/**
 * @author Pedro Reis
 * preis@av.it.pt
 * 2005
 */

public class ResourcesManager {
	private static String[] dtds = { "deployable-unit_1_0.dtd",
			"deployable-unit-template.xml", "event-jar_1_0.dtd",
			"event-jar-template.xml", "profile-spec-jar_1_0.dtd",
			"profile-spec-jar-template.xml",
			"resource-adaptor-jar-template.xml",
			"resource-adaptor-type-jar_1_0.dtd",
			"resource-adaptor-type-jar-template.xml", "sbb-jar_1_0.dtd",
			"sbb-jar-template.xml", "service-xml_1_0.dtd",
			 "standard-events.xml",
			"standard-profile-specs.xml", "SimpleSBB.java" };
	
	private static String[] jars = {"slee.jar"};
	
	public static String[] getLibs()
	{
		return jars;
	}

	public static String[] getDTDs() {
		return dtds;
	}

	public static int getDTDCount() {
		return dtds.length;
	}
}
