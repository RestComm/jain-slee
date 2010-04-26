package org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references;

import javax.slee.SbbID;

import org.mobicents.slee.container.component.sbb.SbbRefDescriptor;

/**
 * Start time:10:25:19 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MSbbRef implements SbbRefDescriptor {

	private String sbbAlias;

	private SbbID sbbID;

	public MSbbRef(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.SbbRef sbbRef11) {
		String sbbName = sbbRef11.getSbbName().getvalue();
		String sbbVendor = sbbRef11.getSbbVendor().getvalue();
		String sbbVersion = sbbRef11.getSbbVersion().getvalue();

		this.sbbAlias = sbbRef11.getSbbAlias().getvalue();

		this.sbbID = new SbbID(sbbName, sbbVendor, sbbVersion);
	}

	public MSbbRef(
			org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.SbbRef sbbRef10) {
		String sbbName = sbbRef10.getSbbName().getvalue();
		String sbbVendor = sbbRef10.getSbbVendor().getvalue();
		String sbbVersion = sbbRef10.getSbbVersion().getvalue();

		this.sbbAlias = sbbRef10.getSbbAlias().getvalue();

		this.sbbID = new SbbID(sbbName, sbbVendor, sbbVersion);
	}

	public String getSbbAlias() {
		return sbbAlias;
	}

	public SbbID getComponentID() {
		return this.sbbID;
	}

}
