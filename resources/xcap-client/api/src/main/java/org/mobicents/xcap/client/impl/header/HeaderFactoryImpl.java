package org.mobicents.xcap.client.impl.header;

import org.mobicents.xcap.client.header.Header;
import org.mobicents.xcap.client.header.HeaderFactory;

/**
 * Implementation of the header factory. 
 * @author martins
 *
 */
public class HeaderFactoryImpl implements HeaderFactory {

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.xcap.client.header.HeaderFactory#getBasicHeader(java.lang.String, java.lang.String)
	 */
	public Header getBasicHeader(String name, String value) {
		return new HeaderImpl(name, value);
	}
}
