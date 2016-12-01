package org.telestax.slee.container.build.as7.extension;

import org.jboss.as.connector.subsystems.datasources.DataSourceReferenceFactoryService;
import org.jboss.as.controller.AbstractBoottimeAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.jmx.MBeanServerService;
import org.jboss.as.naming.ManagedReferenceFactory;
import org.jboss.as.server.AbstractDeploymentChainStep;
import org.jboss.as.server.DeploymentProcessorTarget;
import org.jboss.as.txn.service.TransactionManagerService;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceTarget;
import org.telestax.slee.container.build.as7.deployment.SleeDeploymentInstallProcessor;
import org.telestax.slee.container.build.as7.deployment.SleeDeploymentParseProcessor;
import org.telestax.slee.container.build.as7.service.SleeContainerService;
import org.telestax.slee.container.build.as7.service.SleeServiceNames;

import javax.management.MBeanServer;
import javax.transaction.TransactionManager;
import java.util.List;

class SleeSubsystemAdd extends AbstractBoottimeAddStepHandler {

    static final SleeSubsystemAdd INSTANCE = new SleeSubsystemAdd();

    private final Logger log = Logger.getLogger(SleeSubsystemAdd.class);

    private SleeSubsystemAdd() {
    }

    /** {@inheritDoc} */
    @Override
    protected void populateModel(ModelNode operation, ModelNode model) throws OperationFailedException {
        log.info("Populating the model");
        //model.setEmptyObject();
        SleeSubsystemDefinition.RMI_ADDRESS.validateAndSet(operation, model);
        SleeSubsystemDefinition.RMI_PORT.validateAndSet(operation, model);
        log.info("Populating the model END");
    }

    /** {@inheritDoc} */
    @Override
    public void performBoottime(OperationContext context, ModelNode operation, ModelNode model,
            ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers)
            throws OperationFailedException {

        // Add deployment processors here
        // Remove this if you don't need to hook into the deployers, or you can add as many as you like
        // see SubDeploymentProcessor for explanation of the phases
        context.addStep(new AbstractDeploymentChainStep() {
            public void execute(DeploymentProcessorTarget processorTarget) {
                processorTarget.addDeploymentProcessor(SleeExtension.SUBSYSTEM_NAME,
                        SleeDeploymentParseProcessor.PHASE,
                        SleeDeploymentParseProcessor.PRIORITY,
                        new SleeDeploymentParseProcessor());
                processorTarget.addDeploymentProcessor(SleeExtension.SUBSYSTEM_NAME,
                        SleeDeploymentInstallProcessor.PHASE,
                        SleeDeploymentInstallProcessor.PRIORITY,
                        new SleeDeploymentInstallProcessor());
            }
        }, OperationContext.Stage.RUNTIME);

        //ModelNode fullModel = Resource.Tools.readModel(context.readResource(PathAddress.EMPTY_ADDRESS));

        final ModelNode rmiAddressModel = SleeSubsystemDefinition.RMI_ADDRESS.resolveModelAttribute(context, model);
        final String rmiAddress = rmiAddressModel.isDefined() ? rmiAddressModel.asString() : null;

        final ModelNode rmiPortModel = SleeSubsystemDefinition.RMI_PORT.resolveModelAttribute(context, model);
        final int rmiPort = rmiPortModel.isDefined() ? rmiPortModel.asInt() : 5555;

        log.info("rmi: "+rmiAddress+":"+rmiPort);


    	// Installs the msc service which builds the SleeContainer instance and its modules
        final ServiceTarget target = context.getServiceTarget();
        final SleeContainerService sleeContainerService = new SleeContainerService(rmiAddress, rmiPort);
        //final SleeContainerService sleeContainerService = new SleeContainerService("127.0.0.1", 5555);
        final ServiceBuilder<?> sleeContainerServiceBuilder = target
                .addService(SleeServiceNames.SLEE_CONTAINER, sleeContainerService)
                //.addDependency(PathManagerService.SERVICE_NAME, PathManager.class, service.getPathManagerInjector())
                .addDependency(MBeanServerService.SERVICE_NAME, MBeanServer.class, sleeContainerService.getMbeanServer())
                .addDependency(TransactionManagerService.SERVICE_NAME, TransactionManager.class, sleeContainerService.getTransactionManager())
                .addDependency(DataSourceReferenceFactoryService.SERVICE_NAME_BASE.append("ExampleDS"),
                        ManagedReferenceFactory.class, sleeContainerService.getManagedReferenceFactory());

        newControllers.add(sleeContainerServiceBuilder.setInitialMode(Mode.ACTIVE).install());
    }
    
}
