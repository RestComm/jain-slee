package org.telestax.slee.container.build.as7.extension;

import org.jboss.as.controller.*;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
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

    protected static final String REMOTE = "remote";
    protected static final String RMI_ADDRESS = "rmi-address";
    protected static final String RMI_PORT = "rmi-port";
    protected static final PathElement REMOTE_PATH = PathElement.pathElement(REMOTE);

    static StandardResourceDescriptionResolver getResourceDescriptionResolver(final String keyPrefix) {
        String prefix = SUBSYSTEM_NAME + (keyPrefix == null ? "" : "." + keyPrefix);
        return new StandardResourceDescriptionResolver(prefix, RESOURCE_NAME, SleeExtension.class.getClassLoader(), true, false);
    }

    @Override
    public void initializeParsers(ExtensionParsingContext context) {
        context.setSubsystemXmlMapping(SUBSYSTEM_NAME, NAMESPACE, parser);
    }


    @Override
    public void initialize(ExtensionContext context) {
        log.info("initialize");
        final SubsystemRegistration subsystem = context.registerSubsystem(SUBSYSTEM_NAME, 3, 0);
        final ManagementResourceRegistration registration = subsystem.registerSubsystemModel(SleeSubsystemDefinition.INSTANCE);

        final OperationDefinition describeOp = new SimpleOperationDefinitionBuilder(DESCRIBE,
                getResourceDescriptionResolver(null))
                    .setEntryType(OperationEntry.EntryType.PRIVATE)
                    .build();
        registration.registerOperationHandler(describeOp, GenericSubsystemDescribeHandler.INSTANCE, false);

        subsystem.registerXMLElementWriter(parser);
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
            System.out.println("writeContent");

            context.startSubsystemElement(SleeExtension.NAMESPACE, false);

            final ModelNode node = context.getModelNode();

            SleeSubsystemDefinition.RMI_ADDRESS.marshallAsAttribute(node, false, writer);
            SleeSubsystemDefinition.RMI_PORT.marshallAsAttribute(node, false, writer);

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


            System.out.println("readElement");

            final ModelNode address = new ModelNode();
            address.add(SUBSYSTEM, SleeExtension.SUBSYSTEM_NAME);
            address.protect();

            final ModelNode subsystem = new ModelNode();
            subsystem.get(OP).set(ADD);
            subsystem.get(OP_ADDR).set(address);

            final int count = reader.getAttributeCount();
            System.out.println("count: "+count);
            for (int i = 0; i < count; i++) {
                requireNoNamespaceAttribute(reader, i);
                final String value = reader.getAttributeValue(i);
                final String name = reader.getAttributeLocalName(i);
                //final Attribute attribute = Attribute.forName(name);
                System.out.println("name: "+name);
                if (name.equals("rmi-address") ||
                        name.equals("rmi-port")) {
                    System.out.println("attribute: "+name);
                    subsystem.get(name).set(value);
                }
            }

            list.add(subsystem);

            /*
            //Read the children
            while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
                if (reader.isStartElement()) {
                    if (reader.getLocalName().equals(SleeSubsystemDefinition.REMOTE)) {
                        System.out.println("readRemoteType");
                        readRemoteType(reader, list, address);
                    }
                }
            }
            */

            while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            }

            System.out.println("list: "+list.toString());

        }

        /*
        private void readRemoteType(final XMLExtendedStreamReader reader,
                                    final List<ModelNode> modelNodes,
                                    final ModelNode parentAddress) throws XMLStreamException {
            final ModelNode node = new ModelNode();
            node.get(OP).set(ADD);
            modelNodes.add(node);

            System.out.println("readRemoteAttributes");
            readRemoteAttributes(reader, node, parentAddress);
            ParseUtils.requireNoContent(reader);
        }

        private void readRemoteAttributes(final XMLExtendedStreamReader reader,
                                          final ModelNode node,
                                          final ModelNode parentAddress) throws XMLStreamException {
            String remoteName = "sleeremote";
            final int count = reader.getAttributeCount();
            for (int i = 0; i < count; i++) {
                final String name = reader.getAttributeLocalName(i);
                final String value = reader.getAttributeValue(i);

                System.out.println("readRemoteAttributes name: "+name+", value: "+value);

                if (name.equals("rmi-address")) {
                    SleeSubsystemDefinition.RMI_ADDRESS.parseAndSetParameter(value, node, reader);
                } else if (name.equals("rmi-port")) {
                    SleeSubsystemDefinition.RMI_PORT.parseAndSetParameter(value, node, reader);
                } else {
                    throw ParseUtils.unexpectedAttribute(reader, i);
                }
            }

            node.get(OP_ADDR).set(parentAddress).add(SleeSubsystemDefinition.REMOTE, remoteName);
        }
        */
    }

}
