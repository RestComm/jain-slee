package org.telestax.slee.container.build.as7.extension;

import org.jboss.as.controller.*;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

public class SleeSubsystemDefinition extends SimpleResourceDefinition {

    protected static final SimpleAttributeDefinition CACHE_CONFIG =
            new SimpleAttributeDefinitionBuilder(SleeSubsystemModel.CACHE_CONFIG, ModelType.STRING, false)
                    .setAllowExpression(true)
                    .setXmlName(SleeSubsystemModel.CACHE_CONFIG)
                    .setDefaultValue(new ModelNode(""))
                    .build();

    protected static final SimpleAttributeDefinition REMOTE_RMI_ADDRESS =
            new SimpleAttributeDefinitionBuilder(SleeSubsystemModel.REMOTE_RMI_ADDRESS, ModelType.STRING, false)
                    .setAllowExpression(true)
                    .setXmlName(SleeSubsystemModel.REMOTE_RMI_ADDRESS)
                    .setDefaultValue(new ModelNode("localhost"))
                    .build();

    protected static final SimpleAttributeDefinition REMOTE_RMI_PORT =
            new SimpleAttributeDefinitionBuilder(SleeSubsystemModel.REMOTE_RMI_PORT, ModelType.INT, false)
                    .setAllowExpression(true)
                    .setXmlName(SleeSubsystemModel.REMOTE_RMI_PORT)
                    .setDefaultValue(new ModelNode(5555))
                    .build();

    protected static final SimpleAttributeDefinition PROFILES_PERSIST_PROFILES =
            new SimpleAttributeDefinitionBuilder(SleeSubsystemModel.PROFILES_PERSIST_PROFILES, ModelType.BOOLEAN, false)
                    .setAllowExpression(true)
                    .setXmlName(SleeSubsystemModel.PROFILES_PERSIST_PROFILES)
                    .setDefaultValue(new ModelNode(true))
                    .build();

    protected static final SimpleAttributeDefinition PROFILES_CLUSTERED_PROFILES =
            new SimpleAttributeDefinitionBuilder(SleeSubsystemModel.PROFILES_CLUSTERED_PROFILES, ModelType.BOOLEAN, false)
                    .setAllowExpression(true)
                    .setXmlName(SleeSubsystemModel.PROFILES_CLUSTERED_PROFILES)
                    .setDefaultValue(new ModelNode(false))
                    .build();

    protected static final SimpleAttributeDefinition PROFILES_HIBERNATE_DATASOURCE =
            new SimpleAttributeDefinitionBuilder(SleeSubsystemModel.PROFILES_HIBERNATE_DATASOURCE, ModelType.STRING, false)
                    .setAllowExpression(true)
                    .setXmlName(SleeSubsystemModel.PROFILES_HIBERNATE_DATASOURCE)
                    .setDefaultValue(new ModelNode(""))
                    .build();

    protected static final SimpleAttributeDefinition PROFILES_HIBERNATE_DIALECT =
            new SimpleAttributeDefinitionBuilder(SleeSubsystemModel.PROFILES_HIBERNATE_DIALECT, ModelType.STRING, false)
                    .setAllowExpression(true)
                    .setXmlName(SleeSubsystemModel.PROFILES_HIBERNATE_DIALECT)
                    .setDefaultValue(new ModelNode(""))
                    .build();

    static final AttributeDefinition[] ATTRIBUTES = {
            CACHE_CONFIG,
            REMOTE_RMI_ADDRESS,
            REMOTE_RMI_PORT,
            PROFILES_PERSIST_PROFILES,
            PROFILES_CLUSTERED_PROFILES,
            PROFILES_HIBERNATE_DATASOURCE,
            PROFILES_HIBERNATE_DIALECT
    };

    public static final SleeSubsystemDefinition INSTANCE = new SleeSubsystemDefinition();

    private SleeSubsystemDefinition() {
        super(SleeExtension.SUBSYSTEM_PATH,
                SleeExtension.getResourceDescriptionResolver(SleeExtension.SUBSYSTEM_NAME),
                //We always need to add an 'add' operation
                SleeSubsystemAdd.INSTANCE,
                //Every resource that is added, normally needs a remove operation
                SleeSubsystemRemove.INSTANCE);
    }

    @Override
    public void registerOperations(ManagementResourceRegistration resourceRegistration) {
        super.registerOperations(resourceRegistration);
        //you can register aditional operations here
    }

    @Override
    public void registerAttributes(ManagementResourceRegistration resourceRegistration) {
        //you can register attributes here
        for (AttributeDefinition ad : SleeSubsystemDefinition.ATTRIBUTES) {
            resourceRegistration.registerReadWriteAttribute(ad, null,
                    new ReloadRequiredWriteAttributeHandler(ad));
        }
    }
}
