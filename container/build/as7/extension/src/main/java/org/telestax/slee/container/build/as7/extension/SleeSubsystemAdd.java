package org.telestax.slee.container.build.as7.extension;

import org.jboss.as.connector.subsystems.datasources.DataSourceReferenceFactoryService;
import org.jboss.as.controller.*;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.controller.services.path.PathManager;
import org.jboss.as.controller.services.path.PathManagerService;
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
        for (AttributeDefinition ad : SleeSubsystemDefinition.ATTRIBUTES) {
            ad.validateAndSet(operation, model);
        }
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

        ModelNode fullModel = Resource.Tools.readModel(context.readResource(PathAddress.EMPTY_ADDRESS));

        final ModelNode cacheConfigModel = SleeSubsystemDefinition.CACHE_CONFIG.resolveModelAttribute(context, model);
        final String cacheConfig = cacheConfigModel.isDefined() ? cacheConfigModel.asString() : null;

    	// Installs the msc service which builds the SleeContainer instance and its modules
        final ServiceTarget target = context.getServiceTarget();
        final SleeContainerService sleeContainerService = new SleeContainerService(fullModel, cacheConfig);

        final ServiceBuilder<?> sleeContainerServiceBuilder = target
                .addService(SleeServiceNames.SLEE_CONTAINER, sleeContainerService)
                .addDependency(PathManagerService.SERVICE_NAME, PathManager.class, sleeContainerService.getPathManagerInjector())
                .addDependency(MBeanServerService.SERVICE_NAME, MBeanServer.class, sleeContainerService.getMbeanServer())
                .addDependency(TransactionManagerService.SERVICE_NAME, TransactionManager.class, sleeContainerService.getTransactionManager())
                .addDependency(DataSourceReferenceFactoryService.SERVICE_NAME_BASE.append("ExampleDS"),
                        ManagedReferenceFactory.class, sleeContainerService.getManagedReferenceFactory());

        newControllers.add(sleeContainerServiceBuilder.setInitialMode(Mode.ACTIVE).install());
    }
    
}
