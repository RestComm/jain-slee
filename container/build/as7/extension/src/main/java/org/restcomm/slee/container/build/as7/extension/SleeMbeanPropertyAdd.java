package org.restcomm.slee.container.build.as7.extension;

import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.dmr.ModelNode;

import static org.restcomm.slee.container.build.as7.extension.SleeMbeanPropertyDefinition.PROPERTY_ATTRIBUTES;

class SleeMbeanPropertyAdd extends AbstractAddStepHandler {

    public static final SleeMbeanPropertyAdd INSTANCE = new SleeMbeanPropertyAdd();

    private SleeMbeanPropertyAdd() {
    }

    @Override
    protected void populateModel(final ModelNode operation, final ModelNode model) throws OperationFailedException {
        SleeMbeanPropertyDefinition.NAME_ATTR.validateAndSet(operation, model);
        for (SimpleAttributeDefinition def : PROPERTY_ATTRIBUTES) {
            def.validateAndSet(operation, model);
        }
    }
}