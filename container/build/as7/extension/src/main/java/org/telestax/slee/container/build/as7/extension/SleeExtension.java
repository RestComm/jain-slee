package org.telestax.slee.container.build.as7.extension;

import org.jboss.as.controller.*;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.descriptions.StandardResourceDescriptionResolver;
import org.jboss.as.controller.operations.common.GenericSubsystemDescribeHandler;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.controller.parsing.Attribute;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.parsing.Namespace;
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

import javax.slee.SLEEException;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.util.EnumSet;
import java.util.List;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DESCRIBE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;
import static org.jboss.as.controller.parsing.ParseUtils.requireNoNamespaceAttribute;

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
    public static final String NAMESPACE = "urn:telestax:slee-container:3.0";

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
    }

    /*
    private static ModelNode createAddSubsystemOperation() {
        final ModelNode subsystem = new ModelNode();
        subsystem.get(OP).set(ADD);
        subsystem.get(OP_ADDR).add(SUBSYSTEM, SUBSYSTEM_NAME);
        return subsystem;
    }
    */

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

            final ModelNode sleeSubsystem = context.getModelNode();
            SleeSubsystemDefinition.REMOTE_RMI_ADDRESS.marshallAsElement(sleeSubsystem, writer);
            SleeSubsystemDefinition.REMOTE_RMI_PORT.marshallAsElement(sleeSubsystem, writer);
            SleeSubsystemDefinition.PROFILES_PERSIST_PROFILES.marshallAsElement(sleeSubsystem, writer);
            SleeSubsystemDefinition.PROFILES_CLUSTERED_PROFILES.marshallAsElement(sleeSubsystem, writer);
            SleeSubsystemDefinition.PROFILES_HIBERNATE_DATASOURCE.marshallAsElement(sleeSubsystem, writer);
            SleeSubsystemDefinition.PROFILES_HIBERNATE_DIALECT.marshallAsElement(sleeSubsystem, writer);

            writer.writeEndElement();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void readElement(XMLExtendedStreamReader reader, List<ModelNode> list) throws XMLStreamException {
            // Require no content
            //ParseUtils.requireNoContent(reader);
            //list.add(createAddSubsystemOperation());
            ParseUtils.requireNoAttributes(reader);

            final ModelNode address = new ModelNode();
            address.add(SUBSYSTEM, SleeExtension.SUBSYSTEM_NAME);
            address.protect();

            final ModelNode subsystem = new ModelNode();
            subsystem.get(OP).set(ADD);
            subsystem.get(OP_ADDR).set(address);
            list.add(subsystem);

            // elements
            final EnumSet<Element> encountered = EnumSet.noneOf(Element.class);
            while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
                final Element element = Element.forName(reader.getLocalName());
                if (!encountered.add(element)) {
                    throw ParseUtils.unexpectedElement(reader);
                }
                switch (element) {
                    case REMOTE_RMI_ADDRESS: {
                        final String value = parseRemoteRmiAddress(reader);
                        SleeSubsystemDefinition.REMOTE_RMI_ADDRESS.parseAndSetParameter(value, subsystem, reader);
                        break;
                    }
                    case REMOTE_RMI_PORT: {
                        final String value = parseRemoteRmiPort(reader);
                        SleeSubsystemDefinition.REMOTE_RMI_PORT.parseAndSetParameter(value, subsystem, reader);
                        break;
                    }
                    case PROFILES_PERSIST_PROFILES: {
                        final String value = parseProfilesPersistProfiles(reader);
                        SleeSubsystemDefinition.PROFILES_PERSIST_PROFILES.parseAndSetParameter(value, subsystem, reader);
                        break;
                    }
                    case PROFILES_CLUSTERED_PROFILES: {
                        final String value = parseProfilesClusteredProfiles(reader);
                        SleeSubsystemDefinition.PROFILES_CLUSTERED_PROFILES.parseAndSetParameter(value, subsystem, reader);
                        break;
                    }
                    case PROFILES_HIBERNATE_DATASOURCE: {
                        final String value = parseProfilesHibernateDatasource(reader);
                        SleeSubsystemDefinition.PROFILES_HIBERNATE_DATASOURCE.parseAndSetParameter(value, subsystem, reader);
                        break;
                    }
                    case PROFILES_HIBERNATE_DIALECT: {
                        final String value = parseProfilesHibernateDialect(reader);
                        SleeSubsystemDefinition.PROFILES_HIBERNATE_DIALECT.parseAndSetParameter(value, subsystem, reader);
                        break;
                    }
                    default: {
                        throw ParseUtils.unexpectedElement(reader);
                    }
                }
            }
        }

    }

    static String parseRemoteRmiAddress(XMLExtendedStreamReader reader) throws XMLStreamException {
        // we don't expect any attributes for this element.
        ParseUtils.requireNoAttributes(reader);

        final String value = reader.getElementText();
        if (value == null || value.trim().isEmpty()) {
            throw new XMLStreamException(
                    "Invalid value: " + value + " for '" + Element.REMOTE_RMI_ADDRESS.getLocalName() + "' element",
                    reader.getLocation());
        }
        return value.trim();
    }

    static String parseRemoteRmiPort(XMLExtendedStreamReader reader) throws XMLStreamException {
        // we don't expect any attributes for this element.
        ParseUtils.requireNoAttributes(reader);

        final String value = reader.getElementText();
        if (value == null || value.trim().isEmpty()) {
            throw new XMLStreamException(
                    "Invalid value: " + value + " for '" + Element.REMOTE_RMI_PORT.getLocalName() + "' element",
                    reader.getLocation());
        }
        return value.trim();
    }

    static String parseProfilesPersistProfiles(XMLExtendedStreamReader reader) throws XMLStreamException {
        // we don't expect any attributes for this element.
        ParseUtils.requireNoAttributes(reader);

        final String value = reader.getElementText();
        if (value == null || value.trim().isEmpty()) {
            throw new XMLStreamException(
                    "Invalid value: " + value + " for '" + Element.PROFILES_PERSIST_PROFILES.getLocalName() + "' element",
                    reader.getLocation());
        }
        return value.trim();
    }

    static String parseProfilesClusteredProfiles(XMLExtendedStreamReader reader) throws XMLStreamException {
        // we don't expect any attributes for this element.
        ParseUtils.requireNoAttributes(reader);

        final String value = reader.getElementText();
        if (value == null || value.trim().isEmpty()) {
            throw new XMLStreamException(
                    "Invalid value: " + value + " for '" + Element.PROFILES_CLUSTERED_PROFILES.getLocalName() + "' element",
                    reader.getLocation());
        }
        return value.trim();
    }

    static String parseProfilesHibernateDatasource(XMLExtendedStreamReader reader) throws XMLStreamException {
        // we don't expect any attributes for this element.
        ParseUtils.requireNoAttributes(reader);

        final String value = reader.getElementText();
        if (value == null || value.trim().isEmpty()) {
            throw new XMLStreamException(
                    "Invalid value: " + value + " for '" + Element.PROFILES_HIBERNATE_DATASOURCE.getLocalName() + "' element",
                    reader.getLocation());
        }
        return value.trim();
    }

    static String parseProfilesHibernateDialect(XMLExtendedStreamReader reader) throws XMLStreamException {
        // we don't expect any attributes for this element.
        ParseUtils.requireNoAttributes(reader);

        final String value = reader.getElementText();
        if (value == null || value.trim().isEmpty()) {
            throw new XMLStreamException(
                    "Invalid value: " + value + " for '" + Element.PROFILES_HIBERNATE_DIALECT.getLocalName() + "' element",
                    reader.getLocation());
        }
        return value.trim();
    }

}
