package org.restcomm.slee.container.build.as7.extension;

import org.jboss.as.controller.*;
import org.jboss.as.controller.descriptions.StandardResourceDescriptionResolver;
import org.jboss.as.controller.operations.common.GenericSubsystemDescribeHandler;
import org.jboss.as.controller.parsing.Attribute;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.parsing.ParseUtils;
import org.jboss.as.controller.persistence.SubsystemMarshallingContext;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.OperationEntry;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.logging.Logger;
import org.jboss.staxmapper.XMLElementReader;
import org.jboss.staxmapper.XMLElementWriter;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.util.Collections;
import java.util.List;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DESCRIBE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;
import static org.jboss.as.controller.parsing.ParseUtils.*;
import static org.jboss.as.controller.parsing.ParseUtils.missingRequired;
import static org.jboss.as.controller.parsing.ParseUtils.unexpectedAttribute;

/**
 *
 * @author Eduardo Martins
 *
 */
public class SleeExtension implements Extension {

    private final Logger log = Logger.getLogger(SleeExtension.class);

    /**
     * The name space used for the {@code subsystem} element
     */
    public static final String NAMESPACE = "urn:restcomm:slee-container:3.0";

    /**
     * The name of our subsystem within the model.
     */
    public static final String SUBSYSTEM_NAME = "slee-container";

    /**
     * The parser used for parsing our subsystem
     */
    private final SubsystemParser parser = new SubsystemParser();

    protected static final PathElement SUBSYSTEM_PATH = PathElement.pathElement(SUBSYSTEM, SUBSYSTEM_NAME);
    private static final String RESOURCE_NAME = SleeExtension.class.getPackage().getName() + ".LocalDescriptions";

    static StandardResourceDescriptionResolver getResourceDescriptionResolver(final String keyPrefix) {
        //String prefix = SUBSYSTEM_NAME + (keyPrefix == null ? "" : "." + keyPrefix);
        //return new StandardResourceDescriptionResolver(prefix, RESOURCE_NAME, SleeExtension.class.getClassLoader(), true, false);
        return new StandardResourceDescriptionResolver(keyPrefix, RESOURCE_NAME, SleeExtension.class.getClassLoader(), true, false);
    }

    @Override
    public void initializeParsers(ExtensionParsingContext context) {
        context.setSubsystemXmlMapping(SUBSYSTEM_NAME, NAMESPACE, parser);
    }


    @Override
    public void initialize(ExtensionContext context) {
        final SubsystemRegistration subsystem = context.registerSubsystem(SUBSYSTEM_NAME, 3, 0);
        final ManagementResourceRegistration registration = subsystem.registerSubsystemModel(SleeSubsystemDefinition.INSTANCE);

        final OperationDefinition describeOp = new SimpleOperationDefinitionBuilder(DESCRIBE,
                getResourceDescriptionResolver(null))
                    .setEntryType(OperationEntry.EntryType.PRIVATE)
                    .build();
        registration.registerOperationHandler(describeOp, GenericSubsystemDescribeHandler.INSTANCE, false);

        subsystem.registerXMLElementWriter(parser);

        // here we can register submodels
        final ManagementResourceRegistration mbeans = registration.registerSubModel(SleeMbeanDefinition.INSTANCE);
    }

    private static ModelNode createAddSubsystemOperation() {
        final ModelNode subsystem = new ModelNode();
        subsystem.get(OP).set(ADD);
        subsystem.get(OP_ADDR).add(SUBSYSTEM, SUBSYSTEM_NAME);
        return subsystem;
    }

    /**
     * The subsystem parser, which uses stax to read and write to and from xml
     */
    private static class SubsystemParser implements XMLStreamConstants, XMLElementReader<List<ModelNode>>, XMLElementWriter<SubsystemMarshallingContext> {

        /**
         * {@inheritDoc}
         */
        @Override
        public void writeContent(XMLExtendedStreamWriter writer, SubsystemMarshallingContext context) throws XMLStreamException {
            context.startSubsystemElement(SleeExtension.NAMESPACE, false);

            final ModelNode node = context.getModelNode();
            final ModelNode mbean = node.get(SleeMbeanDefinition.MBEAN);

            SleeSubsystemDefinition.CACHE_CONFIG.marshallAsElement(node, writer);

            for (Property mbeanProp : mbean.asPropertyList()) {
                writer.writeStartElement(SleeMbeanDefinition.MBEAN);

                final ModelNode mbeanEntry = mbeanProp.getValue();

                SleeMbeanDefinition.NAME_ATTR.marshallAsAttribute(mbeanEntry, true, writer);

                final ModelNode property = mbeanEntry.get(SleeMbeanPropertyDefinition.PROPERTY);
                if (property != null && property.isDefined()) {
                    for (Property propertyProp : property.asPropertyList()) {
                        writer.writeStartElement(SleeMbeanPropertyDefinition.PROPERTY);

                        final ModelNode propertyEntry = propertyProp.getValue();

                        SleeMbeanPropertyDefinition.NAME_ATTR.marshallAsAttribute(propertyEntry, true, writer);
                        //SleeMbeanPropertyDefinition.TYPE_ATTR.marshallAsAttribute(propertyEntry, true, writer);
                        SleeMbeanPropertyDefinition.VALUE_ATTR.marshallAsAttribute(propertyEntry, true, writer);

                        writer.writeEndElement();
                    }
                }

                writer.writeEndElement();
            }

            writer.writeEndElement();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void readElement(XMLExtendedStreamReader reader, List<ModelNode> list) throws XMLStreamException {
            PathAddress address = PathAddress.pathAddress(PathElement.pathElement(SUBSYSTEM, SleeExtension.SUBSYSTEM_NAME));

            final ModelNode subsystem = new ModelNode();
            subsystem.get(OP).set(ADD);
            subsystem.get(OP_ADDR).set(address.toModelNode());
            list.add(subsystem);

            // mbean elements
            while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
                if (reader.getNamespaceURI().equals(SleeExtension.NAMESPACE)) {
                    final String tagName = reader.getLocalName();
                    if (tagName.equals(SleeSubsystemModel.CACHE_CONFIG)) {
                        final String value = parseCacheConfig(reader);
                        SleeSubsystemDefinition.CACHE_CONFIG.parseAndSetParameter(value, subsystem, reader);
                    } else if (tagName.equals(SleeMbeanDefinition.MBEAN)) {
                        parseMbean(reader, address, list);
                    }
                } else {
                    throw unexpectedElement(reader);
                }
            }
        }

    }

    static void parseMbean(XMLExtendedStreamReader reader, PathAddress parent, List<ModelNode> list)
            throws XMLStreamException {
        String name = null;
        final ModelNode mbean = new ModelNode();

        // MBean Attributes
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            requireNoNamespaceAttribute(reader, i);
            final String attribute = reader.getAttributeLocalName(i);
            final String value = reader.getAttributeValue(i);
            switch (SleeMbeanDefinition.Element.of(attribute)) {
                case NAME: {
                    name = value;
                    SleeMbeanDefinition.NAME_ATTR.parseAndSetParameter(value, mbean, reader);
                    break;
                }
                default: {
                    throw unexpectedAttribute(reader, i);
                }
            }
        }

        //ParseUtils.requireNoContent(reader);

        if (name == null) {
            throw missingRequired(reader, Collections.singleton(Attribute.NAME));
        }

        mbean.get(OP).set(ADD);
        PathAddress address = PathAddress.pathAddress(parent,
                PathElement.pathElement(SleeMbeanDefinition.MBEAN, name));
        mbean.get(OP_ADDR).set(address.toModelNode());
        list.add(mbean);

        // properties elements
        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            if (reader.getNamespaceURI().equals(SleeExtension.NAMESPACE)) {
                final String tagName = reader.getLocalName();
                if (tagName.equals(SleeMbeanPropertyDefinition.PROPERTY)) {
                    parseProperty(reader, address, list);
                }
            } else {
                throw unexpectedElement(reader);
            }
        }
    }

    static void parseProperty(XMLExtendedStreamReader reader, PathAddress parent, List<ModelNode> list)
            throws XMLStreamException {
        String name = null;
        final ModelNode property = new ModelNode();

        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            requireNoNamespaceAttribute(reader, i);
            final String attribute = reader.getAttributeLocalName(i);
            final String value = reader.getAttributeValue(i);
            switch (SleeMbeanPropertyDefinition.Element.of(attribute)) {
                case NAME: {
                    name = value;
                    SleeMbeanPropertyDefinition.NAME_ATTR.parseAndSetParameter(value, property, reader);
                    break;
                }
                //case TYPE: {
                //    SleeMbeanPropertyDefinition.TYPE_ATTR.parseAndSetParameter(value, property, reader);
                //    break;
                //}
                case VALUE: {
                    SleeMbeanPropertyDefinition.VALUE_ATTR.parseAndSetParameter(value, property, reader);
                    break;
                }
                default: {
                    throw unexpectedAttribute(reader, i);
                }
            }
        }

        ParseUtils.requireNoContent(reader);

        if (name == null) {
            throw missingRequired(reader, Collections.singleton(Attribute.NAME));
        }

        property.get(OP).set(ADD);
        PathAddress address = PathAddress.pathAddress(parent,
                PathElement.pathElement(SleeMbeanPropertyDefinition.PROPERTY, name));
        property.get(OP_ADDR).set(address.toModelNode());
        list.add(property);
    }

    static String parseCacheConfig(XMLExtendedStreamReader reader) throws XMLStreamException {
        // we don't expect any attributes for this element.
        ParseUtils.requireNoAttributes(reader);

        final String value = reader.getElementText();

        if (value == null || value.trim().isEmpty()) {
            throw new XMLStreamException(
                    "Invalid value: " + value + " for '" + Element.CACHE_CONFIG.getLocalName() + "' element",
                    reader.getLocation());
        }
        return value.trim();
    }
}
