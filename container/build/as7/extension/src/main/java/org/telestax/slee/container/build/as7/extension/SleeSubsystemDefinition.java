package org.telestax.slee.container.build.as7.extension;

import org.jboss.as.controller.ReloadRequiredWriteAttributeHandler;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.logging.Logger;

public class SleeSubsystemDefinition extends SimpleResourceDefinition {
    public static final SleeSubsystemDefinition INSTANCE = new SleeSubsystemDefinition();

    protected static final SimpleAttributeDefinition RMI_ADDRESS =
            new SimpleAttributeDefinitionBuilder(SleeExtension.RMI_ADDRESS, ModelType.STRING, false)
                    .setAllowExpression(true)
                    .setXmlName(SleeExtension.RMI_ADDRESS)
                    .setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES)
                    .setDefaultValue(new ModelNode("localhost"))
                    .build();
    protected static final SimpleAttributeDefinition RMI_PORT =
            new SimpleAttributeDefinitionBuilder(SleeExtension.RMI_PORT, ModelType.INT, false)
                    .setAllowExpression(true)
                    .setXmlName(SleeExtension.RMI_PORT)
                    .setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES)
                    .setDefaultValue(new ModelNode(5555))
                    .build();

    private SleeSubsystemDefinition() {
        super(SleeExtension.SUBSYSTEM_PATH,
                SleeExtension.getResourceDescriptionResolver(null),
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
        resourceRegistration.registerReadWriteAttribute(RMI_ADDRESS, null,
                new ReloadRequiredWriteAttributeHandler(RMI_ADDRESS));
        resourceRegistration.registerReadWriteAttribute(RMI_PORT, null,
                new ReloadRequiredWriteAttributeHandler(RMI_PORT));
    }
}
