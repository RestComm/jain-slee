package org.restcomm.slee.container.build.as7.extension;

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

    static final AttributeDefinition[] ATTRIBUTES = {
            CACHE_CONFIG
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
