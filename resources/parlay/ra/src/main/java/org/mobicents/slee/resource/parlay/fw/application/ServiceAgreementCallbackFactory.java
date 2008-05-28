package org.mobicents.slee.resource.parlay.fw.application;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.slee.resource.parlay.fw.FwSessionException;
import org.mobicents.slee.resource.parlay.util.corba.ORBHandler;
import org.mobicents.slee.resource.parlay.util.corba.POAFactory;
import org.mobicents.slee.resource.parlay.util.corba.PolicyFactory;
import org.mobicents.slee.resource.parlay.util.corba.ServantActivationHelper;
import org.omg.PortableServer.POAPackage.AdapterAlreadyExists;
import org.omg.PortableServer.POAPackage.InvalidPolicy;
import org.omg.PortableServer.POAPackage.ObjectAlreadyActive;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;


/**
 *  Please refer to the relevant Parlay documentation
 */
public class ServiceAgreementCallbackFactory {
    private static final Log logger =
        LogFactory.getLog(ServiceAgreementCallbackFactory.class);

    private ORBHandler orbHandler = null;
    
    private transient IpAppServiceAgreementManagementImpl ipAppServiceAgreementManagementImpl = null;
    
    public ServiceAgreementCallbackFactory(
        ORBHandler orbHandler) {
        this.orbHandler = orbHandler;

    }

    private org.omg.PortableServer.POA ipAppServiceAgreementManagementPOA =
        null;

    /**
     *  Destroys the CallbackFactory
     */
    public void destroy() {
		try
		{
			if ( ipAppServiceAgreementManagementPOA != null ) {
				ipAppServiceAgreementManagementPOA.destroy(false, true);
				ipAppServiceAgreementManagementPOA = null;
			}
		}
		catch(Exception e) {
    		if(logger.isDebugEnabled()) {
    		    logger.debug("Failed to destroy ipAppServiceAgreementManagementPOA", e);
            }
		}

        orbHandler = null;
    }

    /**
     * @param saBean
     * @return @throws
     *         FwSessionException
     */
    public org.csapi.fw.fw_application.service_agreement.IpAppServiceAgreementManagement createIpAppServiceAgreementManagement(
            SABeanImpl saBean) throws FwSessionException {

        org.csapi.fw.fw_application.service_agreement.IpAppServiceAgreementManagement ipAppServiceAgreementManagement = null;

        if (ipAppServiceAgreementManagementPOA == null) {

            //Set up the policies required to create thePOA
            org.omg.CORBA.Policy[] policies = PolicyFactory
                    .createTransientPoaPolicies(orbHandler.getRootPOA());

            try {
                
                ipAppServiceAgreementManagementPOA = POAFactory.createPOA(
                        orbHandler.getRootPOA(),
                        "IpAppServiceAgreementManagementPOA_"
                                + saBean.getTSMBean().getFwProperties()
                                        .getInstanceID(), orbHandler.getRootPOA().the_POAManager(),
                                        policies);

                ipAppServiceAgreementManagementImpl = new IpAppServiceAgreementManagementImpl(
                        ipAppServiceAgreementManagementPOA);
                ipAppServiceAgreementManagementImpl.setSABean(saBean);

                org.omg.CORBA.Object activatedCallback = null;

                activatedCallback = ServantActivationHelper
                        .activateServant(
                                ipAppServiceAgreementManagementPOA,
                                ipAppServiceAgreementManagementImpl);

                ipAppServiceAgreementManagement = org.csapi.fw.fw_application.service_agreement.IpAppServiceAgreementManagementHelper
                        .narrow(activatedCallback);
            } catch (ServantAlreadyActive e) {
                throw new FwSessionException(e);
            } catch (ObjectAlreadyActive e) {
                throw new FwSessionException(e);
            } catch (WrongPolicy e) {
                throw new FwSessionException(e);
            } catch (org.omg.CORBA.BAD_PARAM ex) {
                throw new FwSessionException(
                        "Failed To narrow the CORBA Object reference", ex);
            } catch (AdapterAlreadyExists e) {
                throw new FwSessionException(e);
            } catch (InvalidPolicy e) {
                throw new FwSessionException(e);
            }
        }

        return ipAppServiceAgreementManagement;
    }

    /**
     * Method deactivateIpAppServiceAgreementManagement.
     * @throws FwSessionException
     */
    public void deactivateIpAppServiceAgreementManagement()
        throws FwSessionException {
        
        try {
            ServantActivationHelper.deactivateServant(ipAppServiceAgreementManagementImpl);
        } catch (ObjectNotActive e) {
            throw new FwSessionException(e);
        } catch (WrongPolicy e) {
            throw new FwSessionException(e);
        } catch (ServantNotActive e) {
            throw new FwSessionException(e);
        }

    }
}
