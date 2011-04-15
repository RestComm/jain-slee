/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.slee.enabler.sip;

/**
 * 
 * Interface for SIP-Publication-Client(EPA/PUA). Upon creation of publication,
 * enabler stores information:
 * <ul>
 * <li>eventPackage</li>
 * <li>MIME type</li>
 * <li>entity</li>
 * <li>ecsAddress</li>
 * </ul>
 * Enabler also manages current etag value. Each enabler instance manages single
 * publication.
 * 
 * @author baranowb
 * @author martins
 */
public interface PublicationClientChild {

	/**
	 * This method should be used to create new publication.
	 * 
	 * @param entity
	 *            - entity id handling this part of publication. AOR of
	 *            resource.
	 * @param eventPackage
	 *            - name of event package, ie: "publish"
	 * @param document
	 *            - encoded document, which should be sent for publication
	 * @param contentType
	 *            - main type of MIME type
	 * @param contentSubType
	 *            - sub type of MIME type
	 * @param expires
	 *            - number of seconds for which publication should be active. If
	 *            its equal to 0, it means ESC/PA should determine expires
	 *            interval by itself.
	 */
	public void newPublication(String entity, String eventPackage, String document, String contentType, String contentSubType, int expires);

	/**
	 * Modifies publication with passed document. Update request uses stored
	 * information(ETag,entity,ecs address and MIME).
	 * 
	 * @param document
	 *            - encoded document, which should be sent for publication
	 * @param contentType
	 *            - main type of MIME type
	 * @param contentSubType
	 *            - sub type of MIME type
	 * @param expires
	 *            - number of seconds for which publication should be active
	 */
	public void modifyPublication(String document, String contentType, String contentSubType, int expires);

	/**
	 * Issues remove request. Request uses uses stored
	 * information(ETag,entity,ecs address). After this operation, Publication
	 * child can be used to public something new. 
	 */
	public void removePublication();

	/**
	 * Retrieve entity for which this child publishes.
	 * 
	 * @return
	 */
	public String getEntity();

	/**
	 * Retrieve ETag used in last publish request.
	 * 
	 * @return
	 */
	public String getETag();

}
