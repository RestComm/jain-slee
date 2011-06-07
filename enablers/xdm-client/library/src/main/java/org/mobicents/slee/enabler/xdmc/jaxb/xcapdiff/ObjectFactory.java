/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.enabler.xdmc.jaxb.xcapdiff;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.mobicents.slee.xdm.server.subscription.xcapdiff package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DocumentTypeRemove_QNAME = new QName("urn:ietf:params:xml:ns:xcap-diff", "remove");
    private final static QName _DocumentTypeReplace_QNAME = new QName("urn:ietf:params:xml:ns:xcap-diff", "replace");
    private final static QName _DocumentTypeAdd_QNAME = new QName("urn:ietf:params:xml:ns:xcap-diff", "add");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.mobicents.slee.xdm.server.subscription.xcapdiff
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DocumentType.Remove }
     * 
     */
    public DocumentType.Remove createDocumentTypeRemove() {
        return new DocumentType.Remove();
    }

    /**
     * Create an instance of {@link AttributeType }
     * 
     */
    public AttributeType createAttributeType() {
        return new AttributeType();
    }

    /**
     * Create an instance of {@link org.mobicents.slee.enabler.xdmc.jaxb.xcapdiff.Remove }
     * 
     */
    public org.mobicents.slee.enabler.xdmc.jaxb.xcapdiff.Remove createRemove() {
        return new org.mobicents.slee.enabler.xdmc.jaxb.xcapdiff.Remove();
    }

    /**
     * Create an instance of {@link org.mobicents.slee.enabler.xdmc.jaxb.xcapdiff.Add }
     * 
     */
    public org.mobicents.slee.enabler.xdmc.jaxb.xcapdiff.Add createAdd() {
        return new org.mobicents.slee.enabler.xdmc.jaxb.xcapdiff.Add();
    }

    /**
     * Create an instance of {@link EmptyType }
     * 
     */
    public EmptyType createEmptyType() {
        return new EmptyType();
    }

    /**
     * Create an instance of {@link DocumentType.Replace }
     * 
     */
    public DocumentType.Replace createDocumentTypeReplace() {
        return new DocumentType.Replace();
    }

    /**
     * Create an instance of {@link DocumentType.Add }
     * 
     */
    public DocumentType.Add createDocumentTypeAdd() {
        return new DocumentType.Add();
    }

    /**
     * Create an instance of {@link XcapDiff }
     * 
     */
    public XcapDiff createXcapDiff() {
        return new XcapDiff();
    }

    /**
     * Create an instance of {@link DocumentType }
     * 
     */
    public DocumentType createDocumentType() {
        return new DocumentType();
    }

    /**
     * Create an instance of {@link org.mobicents.slee.enabler.xdmc.jaxb.xcapdiff.Replace }
     * 
     */
    public org.mobicents.slee.enabler.xdmc.jaxb.xcapdiff.Replace createReplace() {
        return new org.mobicents.slee.enabler.xdmc.jaxb.xcapdiff.Replace();
    }

    /**
     * Create an instance of {@link ElementType }
     * 
     */
    public ElementType createElementType() {
        return new ElementType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DocumentType.Remove }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:ietf:params:xml:ns:xcap-diff", name = "remove", scope = DocumentType.class)
    public JAXBElement<DocumentType.Remove> createDocumentTypeRemove(DocumentType.Remove value) {
        return new JAXBElement<DocumentType.Remove>(_DocumentTypeRemove_QNAME, DocumentType.Remove.class, DocumentType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DocumentType.Replace }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:ietf:params:xml:ns:xcap-diff", name = "replace", scope = DocumentType.class)
    public JAXBElement<DocumentType.Replace> createDocumentTypeReplace(DocumentType.Replace value) {
        return new JAXBElement<DocumentType.Replace>(_DocumentTypeReplace_QNAME, DocumentType.Replace.class, DocumentType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DocumentType.Add }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:ietf:params:xml:ns:xcap-diff", name = "add", scope = DocumentType.class)
    public JAXBElement<DocumentType.Add> createDocumentTypeAdd(DocumentType.Add value) {
        return new JAXBElement<DocumentType.Add>(_DocumentTypeAdd_QNAME, DocumentType.Add.class, DocumentType.class, value);
    }

}
