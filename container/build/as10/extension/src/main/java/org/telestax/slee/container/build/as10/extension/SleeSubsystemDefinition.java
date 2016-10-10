package org.telestax.slee.container.build.as10.extension;

import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.registry.ManagementResourceRegistration;

public class SleeSubsystemDefinition extends SimpleResourceDefinition {
    public static final SleeSubsystemDefinition INSTANCE = new SleeSubsystemDefinition();

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
    }
}
