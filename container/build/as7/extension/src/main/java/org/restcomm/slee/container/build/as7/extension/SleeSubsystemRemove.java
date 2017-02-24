package org.restcomm.slee.container.build.as7.extension;

import org.jboss.as.controller.AbstractRemoveStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.dmr.ModelNode;

/**
 * Handler responsible for removing the subsystem resource from the model.
 *
 * @author Eduardo Martins
 *
 */
class SleeSubsystemRemove extends AbstractRemoveStepHandler {

    static final SleeSubsystemRemove INSTANCE = new SleeSubsystemRemove();

    private SleeSubsystemRemove() {
    }

    @Override
    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model) throws OperationFailedException {
        //Remove any services installed by the corresponding add handler here
        //context.removeService(ServiceName.of("some", "name"));
    }


}
