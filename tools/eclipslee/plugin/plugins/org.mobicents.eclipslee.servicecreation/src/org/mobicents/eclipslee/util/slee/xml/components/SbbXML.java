/**
 *   Copyright 2005 Open Cloud Ltd.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.mobicents.eclipslee.util.slee.xml.components;

import org.mobicents.eclipslee.util.slee.xml.DTDHandler;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * @author allenc
 */
public class SbbXML extends DTDXML {

	protected SbbXML(Document document, Element root, DTDHandler dtd) {
		super(document, root, dtd);
	}

	public void setReentrant(boolean reentrant) {
		getRoot().setAttribute("reentrant", new Boolean(reentrant).toString());
	}

	public boolean getReentrant() {
		String attr = getRoot().getAttribute("reentrant");
		if (attr == null)
			return false;

		return Boolean.getBoolean(attr);
	}

	public String getName() {
		return getChildText(getRoot(), "sbb-name");
	}

	public void setName(String name) {
		setChildText(getRoot(), "sbb-name", name);
	}

	public String getVendor() {
		return getChildText(getRoot(), "sbb-vendor");
	}

	public void setVendor(String vendor) {
		setChildText(getRoot(), "sbb-vendor", vendor);
	}

	public String getVersion() {
		return getChildText(getRoot(), "sbb-version");
	}

	public void setVersion(String version) {
		setChildText(getRoot(), "sbb-version", version);
	}

	public String getDescription() {
		return getChildText(getRoot(), "description");
	}

	public void setDescription(String description) {
		setChildText(getRoot(), "description", description);
	}

	public SbbChildRelationXML getChildRelation(String methodName) {
		SbbChildRelationXML children[] = getChildRelations();

		for (int i = 0; i < children.length; i++)
			if (methodName.equals(children[i].getChildRelationMethodName()))
				return children[i];

		return null;
	}

	public SbbChildRelationXML getChildRelation(SbbRefXML ref) {
		SbbChildRelationXML children[] = getChildRelations();

		for (int i = 0; i < children.length; i++) {
			if (ref.getAlias().equals(children[i].getSbbAliasRef())) {
				return children[i];
			}
		}

		return null;
	}

	public void deleteChildRelation(SbbChildRelationXML child) {
		child.getRoot().getParentNode().removeChild(child.getRoot());
	}

	private SbbChildRelationXML addChildRelation() {

		Element classes = getChild(getRoot(), "sbb-classes");
		if (classes == null)
			classes = addElement(getRoot(), "sbb-classes");

		Element absClass = getChild(classes, "sbb-abstract-class");
		if (absClass == null)
			absClass = addElement(classes, "sbb-abstract-class");

		Element childRelation = addElement(absClass,
				"get-child-relation-method");
		return new SbbChildRelationXML(document, childRelation, dtd);
	}

	public void removeChildRelation(SbbChildRelationXML child) {
		child.getRoot().getParentNode().removeChild(child.getRoot());
	}

	public SbbChildRelationXML addChildRelation(SbbRefXML ref) {
		SbbChildRelationXML childXML = addChildRelation();
		childXML.setSbbAliasRef(ref);
		return childXML;
	}

	public SbbChildRelationXML[] getChildRelations() {

		Element classes = getChild(getRoot(), "sbb-classes");
		if (classes == null)
			return new SbbChildRelationXML[0];

		Element absClass = getChild(classes, "sbb-abstract-class");
		if (absClass == null)
			return null;

		Element childRelations[] = getNodes(absClass,
				"sbb-abstract-class/get-child-relation-method");
		SbbChildRelationXML children[] = new SbbChildRelationXML[childRelations.length];
		for (int i = 0; i < childRelations.length; i++)
			children[i] = new SbbChildRelationXML(document, childRelations[i],
					dtd);

		return children;
	}

	public SbbRefXML[] getSbbRefs() {

		Element refs[] = getNodes("sbb/sbb-ref");
		SbbRefXML refXML[] = new SbbRefXML[refs.length];

		for (int i = 0; i < refs.length; i++)
			refXML[i] = new SbbRefXML(document, refs[i], dtd);

		return refXML;
	}

	public SbbRefXML getSbbRef(String name, String vendor, String version) {
		SbbRefXML refs[] = getSbbRefs();

		for (int i = 0; i < refs.length; i++) {
			if (name.equals(refs[i].getName())
					&& vendor.equals(refs[i].getVendor())
					&& version.equals(refs[i].getVersion()))
				return refs[i];
		}

		return null;
	}

	public SbbRefXML getSbbRef(String alias) {
		SbbRefXML refs[] = getSbbRefs();

		for (int i = 0; i < refs.length; i++)
			if (alias.equals(refs[i].getAlias()))
				return refs[i];

		return null;
	}

	public void removeSbbRef(SbbRefXML ref) {
		ref.getRoot().getParentNode().removeChild(ref.getRoot());
	}

	/**
	 * Removes all unreferenced SbbRefs.
	 */

	public void removeUnusedSbbRefs() {
		SbbRefXML refs[] = getSbbRefs();
		SbbCMPField cmps[] = getCMPFields();
		outer: for (int i = 0; i < refs.length; i++) {
			SbbRefXML ref = refs[i];

			// Children
			SbbChildRelationXML child = getChildRelation(ref);
			if (child != null)
				continue;

			// CMP Fields
			for (int cmp = 0; cmp < cmps.length; cmp++) {
				if (ref.getAlias().equals(cmps[cmp].getSbbAliasRef()))
					continue outer;
			}

			// Unused by any CMP Field or Child Relation, remove it.
			this.removeSbbRef(ref);
		}
	}

	public SbbRefXML addSbbRef() {
		Element ref = addElement(getRoot(), "sbb-ref");
		SbbRefXML xml = new SbbRefXML(document, ref, dtd);
		xml.setAlias(getUniqueSbbRefAlias());
		return xml;
	}

	/* TODO: Remove
	private SbbRefXML addSbbRef(String name, String vendor, String version) {
		SbbRefXML xml = addSbbRef();
		xml.setName(name);
		xml.setVendor(vendor);
		xml.setVersion(version);
		return xml;
	}*/

	private String getUniqueSbbRefAlias() {
		String template = "sbb_";

		int count = 0;
		while (true) {
			SbbRefXML xml = getSbbRef(template + count);
			if (xml == null)
				return template + count;

			count++;
		}
	}

	public SbbProfileSpecRefXML[] getProfileSpecRefs() {

		Element refs[] = getNodes("sbb/profile-spec-ref");
		SbbProfileSpecRefXML refXML[] = new SbbProfileSpecRefXML[refs.length];

		for (int i = 0; i < refs.length; i++)
			refXML[i] = new SbbProfileSpecRefXML(document, refs[i], dtd);

		return refXML;
	}

	public SbbProfileSpecRefXML getProfileSpecRef(String name, String vendor,
			String version) {
		SbbProfileSpecRefXML refs[] = getProfileSpecRefs();

		for (int i = 0; i < refs.length; i++) {
			if (name.equals(refs[i].getName())
					&& vendor.equals(refs[i].getVendor())
					&& version.equals(refs[i].getVersion()))
				return refs[i];
		}

		return null;
	}

	public SbbProfileSpecRefXML getProfileSpecRef(String alias) {
		SbbProfileSpecRefXML refs[] = getProfileSpecRefs();

		for (int i = 0; i < refs.length; i++)
			if (alias.equals(refs[i].getAlias()))
				return refs[i];

		return null;
	}

	public void removeProfileSpecRef(SbbProfileSpecRefXML ref) {
		ref.getRoot().getParentNode().removeChild(ref.getRoot());
	}

	public SbbProfileSpecRefXML addProfileSpecRef() {
		Element ref = addElement(getRoot(), "profile-spec-ref");
		SbbProfileSpecRefXML xml = new SbbProfileSpecRefXML(document, ref, dtd);
		xml.setAlias(getUniqueProfileSpecRefAlias());
		return xml;
	}

	/* TODO: Remove
	private SbbProfileSpecRefXML addProfileSpecRef(String name, String vendor,
			String version) {
		SbbProfileSpecRefXML xml = addProfileSpecRef();
		xml.setName(name);
		xml.setVendor(vendor);
		xml.setVersion(version);
		return xml;
	}*/

	private String getUniqueProfileSpecRefAlias() {
		String template = "profile_spec_";

		int count = 0;
		while (true) {
			SbbProfileSpecRefXML xml = getProfileSpecRef(template + count);
			if (xml == null)
				return template + count;

			count++;
		}
	}

	public SbbCMPField addCMPField() {
		Element classes = getChild(getRoot(), "sbb-classes");
		if (classes == null)
			classes = addElement(getRoot(), "sbb-classes");

		Element absClass = getChild(classes, "sbb-abstract-class");
		if (absClass == null)
			absClass = addElement(classes, "sbb-abstract-class");

		Element cmpElement = addElement(absClass, "cmp-field");
		SbbCMPField cmp = new SbbCMPField(document, cmpElement, dtd);
		return cmp;
	}

	public void removeCMPField(SbbCMPField cmpField) {
		cmpField.getRoot().getParentNode().removeChild(cmpField.getRoot());
	}

	public SbbCMPField getCMPField(String name) {

		SbbCMPField fields[] = getCMPFields();
		if (fields == null)
			return null;

		for (int i = 0; i < fields.length; i++)
			if (fields[i].getName().equals(name))
				return fields[i];

		return null;
	}

	public SbbCMPField[] getCMPFields() {

		Element classes = getChild(getRoot(), "sbb-classes");
		if (classes == null)
			return null;

		Element absClass = getChild(classes, "sbb-abstract-class");
		if (absClass == null)
			return null;

		Element fields[] = this.getNodes(absClass,
				"sbb-abstract-class/cmp-field");
		SbbCMPField cmpFields[] = new SbbCMPField[fields.length];

		for (int i = 0; i < fields.length; i++)
			cmpFields[i] = new SbbCMPField(document, fields[i], dtd);

		return cmpFields;
	}

	public void setAbstractClassName(String name) {
		Element classes = getChild(getRoot(), "sbb-classes");
		if (classes == null)
			classes = addElement(getRoot(), "sbb-classes");

		Element absClass = getChild(classes, "sbb-abstract-class");
		if (absClass == null)
			absClass = addElement(classes, "sbb-abstract-class");

		setChildText(absClass, "sbb-abstract-class-name", name);
	}

	public String getAbstractClassName() {

		Element classes = getChild(getRoot(), "sbb-classes");
		if (classes == null)
			return null;
		Element absClass = getChild(classes, "sbb-abstract-class");
		if (absClass == null)
			return null;

		return getChildText(absClass, "sbb-abstract-class-name");
	}

	public void setUsageInterfaceName(String name) {

		Element classes = getChild(getRoot(), "sbb-classes");
		if (classes == null)
			classes = addElement(getRoot(), "sbb-classes");

		Element absClass = getChild(classes, "sbb-usage-parameters-interface");
		if (absClass == null)
			absClass = addElement(classes, "sbb-usage-parameters-interface");

		if (name == null) { // Remove this element
			absClass.getParentNode().removeChild(absClass);
			return;
		}

		setChildText(absClass, "sbb-usage-parameters-interface-name", name);
	}

	public String getUsageInterfaceName() {

		Element classes = getChild(getRoot(), "sbb-classes");
		if (classes == null)
			return null;

		Element usageIface = getChild(classes, "sbb-usage-parameters-interface");
		if (usageIface == null)
			return null;

		return getChildText(usageIface, "sbb-usage-parameters-interface-name");
	}

	public void setLocalInterfaceName(String name) {

		Element classes = getChild(getRoot(), "sbb-classes");
		if (classes == null)
			classes = addElement(getRoot(), "sbb-classes");

		Element localIface = getChild(classes, "sbb-local-interface");
		if (localIface == null)
			localIface = addElement(classes, "sbb-local-interface");

		if (name == null) {
			localIface.getParentNode().removeChild(localIface);
			return;
		}

		setChildText(localIface, "sbb-local-interface-name", name);
	}

	public String getLocalInterfaceName() {
		Element classes = getChild(getRoot(), "sbb-classes");
		if (classes == null)
			return null;
		Element localIface = getChild(classes, "sbb-local-interface");
		if (localIface == null)
			return null;

		return getChildText(localIface, "sbb-local-interface-name");
	}

	public void setActivityContextInterfaceName(String name) {

		Element classes = getChild(getRoot(), "sbb-classes");
		if (classes == null)
			classes = addElement(getRoot(), "sbb-classes");

		Element localIface = getChild(classes, "sbb-activity-context-interface");
		if (localIface == null)
			localIface = addElement(classes, "sbb-activity-context-interface");

		if (name == null) {
			localIface.getParentNode().removeChild(localIface);
			return;
		}

		setChildText(localIface, "sbb-activity-context-interface-name", name);
	}

	public String getActivityContextInterfaceName() {
		Element classes = getChild(getRoot(), "sbb-classes");
		if (classes == null)
			return null;
		Element localIface = getChild(classes, "sbb-activity-context-interface");
		if (localIface == null)
			return null;

		return getChildText(localIface, "sbb-activity-context-interface-name");
	}

	public SbbProfileCMPMethod[] getProfileCMPMethods() {

		Element methods[] = getNodes("sbb/sbb-classes/sbb-abstract-class/get-profile-cmp-method");
		SbbProfileCMPMethod cmp[] = new SbbProfileCMPMethod[methods.length];

		for (int i = 0; i < methods.length; i++)
			cmp[i] = new SbbProfileCMPMethod(document, methods[i], dtd);

		return cmp;
	}

	public SbbProfileCMPMethod getProfileCMPMethod(String methodName) {
		SbbProfileCMPMethod methods[] = getProfileCMPMethods();

		for (int i = 0; i < methods.length; i++) {
			if (methodName.equals(methods[i].getProfileCMPMethodName()))
				return methods[i];

			if (methodName.equals(methods[i].getProfileSpecAliasRef()))
				return methods[i];
		}

		return null;
	}

	public SbbProfileCMPMethod addProfileCMPMethod(SbbProfileSpecRefXML ref) {

		Element classes = getChild(getRoot(), "sbb-classes");
		if (classes == null)
			classes = addElement(getRoot(), "sbb-classes");

		Element absClass = getChild(classes, "sbb-abstract-class");
		if (absClass == null)
			absClass = addElement(classes, "sbb-abstract-class");

		Element child = addElement(absClass, "get-profile-cmp-method");
		SbbProfileCMPMethod method = new SbbProfileCMPMethod(document, child,
				dtd);
		method.setProfileSpecAliasRef(ref);
		return method;
	}

	public void removeProfileCMPMethod(SbbProfileCMPMethod method) {
		method.getRoot().getParentNode().removeChild(method.getRoot());
	}

	public void setAddressProfileSpecAliasRef(SbbProfileSpecRefXML ref) {
		setChildText(getRoot(), "address-profile-spec-alias-ref", ref
				.getAlias());
	}

	public SbbProfileSpecRefXML getAddressProfileSpecAliasRef() {
		String alias = getChildText(getRoot(), "address-profile-spec-alias-ref");
		if (alias == null)
			return null;

		return this.getProfileSpecRef(alias);
	}

	public void removeAddressProfileSpecAliasRef() {
		Element child = getChild(getRoot(), "address-profile-spec-alias-ref");
		if (child != null)
			child.getParentNode().removeChild(child);
	}

	public SbbEventXML[] getEvents() {
		Element nodes[] = getNodes("sbb/event");
		SbbEventXML xml[] = new SbbEventXML[nodes.length];

		for (int i = 0; i < nodes.length; i++)
			xml[i] = new SbbEventXML(document, nodes[i], dtd);

		return xml;
	}

	public SbbEventXML getEvent(String name, String vendor, String version) {
		SbbEventXML events[] = getEvents();

		for (int i = 0; i < events.length; i++)
			if (name.equals(events[i].getName())
					&& vendor.equals(events[i].getVendor())
					&& version.equals(events[i].getVersion()))
				return events[i];

		return null;
	}

	public SbbEventXML getEvent(String alias) {
		SbbEventXML events[] = getEvents();

		for (int i = 0; i < events.length; i++)
			if (alias.equals(events[i].getScopedName()))
				return events[i];

		return null;
	}

	public SbbEventXML addEvent(EventXML event) {
		Element ele = addElement(getRoot(), "event");
		SbbEventXML xml = new SbbEventXML(document, ele, dtd);

		xml.setName(event.getName());
		xml.setVendor(event.getVendor());
		xml.setVersion(event.getVersion());

		
		String clazzName = event.getEventClassName();
		clazzName = clazzName.substring(clazzName.lastIndexOf('.') + 1);
		xml.setScopedName(clazzName);

		return xml;
	}

	public void removeEvent(SbbEventXML event) {
		event.getRoot().getParentNode().removeChild(event.getRoot());
	}

	public SbbEnvEntryXML addEnvEntry() {
		Element ele = addElement(getRoot(), "env-entry");
		SbbEnvEntryXML xml = new SbbEnvEntryXML(document, ele, dtd);
		return xml;
	}
	
	public SbbEnvEntryXML[] getEnvEntries() {
		Element nodes[] = getNodes("sbb/env-entry");
		SbbEnvEntryXML xml[] = new SbbEnvEntryXML[nodes.length];
		for (int i = 0; i < nodes.length; i++)
			xml[i] = new SbbEnvEntryXML(document, nodes[i], dtd);
		return xml;
	}
	
	public void removeEnvEntry(SbbEnvEntryXML xml) {
		xml.getRoot().getParentNode().removeChild(xml.getRoot());
	}
	
	public String toString() {
		return "SBB: " + getName() + "," + getVersion() + "," + getVendor();
	}

	public SbbResourceAdaptorTypeBindingXML addResourceAdaptorTypeBinding() {
		Element ele = addElement(getRoot(), "resource-adaptor-type-binding");
		return new SbbResourceAdaptorTypeBindingXML(document, ele, dtd);
	}
	
	public SbbResourceAdaptorTypeBindingXML[] getResourceAdaptorTypeBindings() {
		Element nodes[] = getNodes("sbb/resource-adaptor-type-binding");
		SbbResourceAdaptorTypeBindingXML xml[] = new SbbResourceAdaptorTypeBindingXML[nodes.length];
		for (int i = 0; i < nodes.length; i++)
			xml[i] = new SbbResourceAdaptorTypeBindingXML(document, nodes[i], dtd);
		return xml;
	}
	
	public void removeResourceAdaptorTypeBinding(SbbResourceAdaptorTypeBindingXML xml) {
		xml.getRoot().getParentNode().removeChild(xml.getRoot());
	}
	
	/* TODO: activity-context-attribute-alias
	resource-adaptor-type-binding
	resource-adaptor-type-ref
	activity-context-interface-factory-name
	resource-adaptor-entity-binding
	ejb-ref
	*/
	
}