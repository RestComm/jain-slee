/**
 * Start time:17:07:31 2009-01-31<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.validator;

import java.util.List;
import java.util.Set;

import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.ComponentID;
import javax.slee.EventContext;
import javax.slee.EventTypeID;
import javax.slee.InitialEventSelector;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedComponentException;
import javax.slee.management.LibraryID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.EventTypeComponentImpl;
import org.mobicents.slee.container.component.SbbComponentImpl;
import org.mobicents.slee.container.component.SleeComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.SbbDescriptorFactoryImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.SbbDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.TCUtilityClass;
import org.mobicents.slee.container.component.event.EventTypeComponent;
import org.mobicents.slee.container.component.library.LibraryComponent;
import org.mobicents.slee.container.component.profile.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.ra.ResourceAdaptorComponent;
import org.mobicents.slee.container.component.ratype.ResourceAdaptorTypeComponent;
import org.mobicents.slee.container.component.sbb.EventEntryDescriptor;
import org.mobicents.slee.container.component.sbb.SbbComponent;
import org.mobicents.slee.container.component.service.ServiceComponent;
import org.mobicents.slee.container.component.validator.sbb.abstracts.event.XEvent;

/**
 * Start time:17:07:31 2009-01-31<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SbbComponentValidatorSbbEventsTest extends TCUtilityClass {

	public static final String _SBB_JAR_ONE_11_EVENTS_OK = "xml/validator/sbb/event/sbb-jar-one-SbbConstraintsEventsOk_1_1.xml";
	public static final String _SBB_JAR_ONE_11_EVENTS_LACK_RECEIVER = "xml/validator/sbb/event/sbb-jar-one-SbbConstraintsEventsLackReceiver_1_1.xml";
	public static final String _SBB_JAR_ONE_11_EVENTS_LACK_FIRE = "xml/validator/sbb/event/sbb-jar-one-SbbConstraintsEventsLackFire_1_1.xml";
	public static final String _SBB_JAR_ONE_11_EVENTS_THROW = "xml/validator/sbb/event/sbb-jar-one-SbbConstraintsEventsThrows_1_1.xml";
	public static final String _SBB_JAR_ONE_11_FIRE_SIGNATURE = "xml/validator/sbb/event/sbb-jar-one-SbbConstraintsEventsWrongFireSignature_1_1.xml";
	public static final String _SBB_JAR_ONE_11_FIRE_SIGNATURE2 = "xml/validator/sbb/event/sbb-jar-one-SbbConstraintsEventsWrongFireSignature2_1_1.xml";
	public static final String _SBB_JAR_ONE_11_RECEIVE_SIGNATURE = "xml/validator/sbb/event/sbb-jar-one-SbbConstraintsEventsWrongReceiveSignature_1_1.xml";
	public static final String _SBB_JAR_ONE_11_RECEIVE_SIGNATURE2 = "xml/validator/sbb/event/sbb-jar-one-SbbConstraintsEventsWrongReceiveSignature2_1_1.xml";
	public static final String _SBB_JAR_ONE_11_CONCRETE_FIRE = "xml/validator/sbb/event/sbb-jar-one-SbbConstraintsEventsConcreteFire_1_1.xml";
	public static final String _SBB_JAR_ONE_11_WRONG_IES = "xml/validator/sbb/event/sbb-jar-one-SbbConstraintsEventsWrongInitialEventSelector_1_1.xml";
	public static final String _SBB_JAR_ONE_11_IES_CONSTRAINTS = "xml/validator/sbb/event/sbb-jar-one-SbbConstraintsEventsIESConstraints_1_1.xml";
	public static final String _SBB_JAR_ONE_11_HANDLERS_CONSTRAINTS = "xml/validator/sbb/event/sbb-jar-one-SbbConstraintsEventsHandlerConstraints_1_1.xml";

	public void testSbbOne11EventsOk() throws Exception {

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_EVENTS_OK));
		final SbbDescriptorImpl descriptor = specs.get(0);
		final SbbComponentImpl component = new SbbComponentImpl(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));
		component.setActivityContextInterface(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbActivityContextInterface()));
		SbbComponentValidator validator = new SbbComponentValidator();
		validator.setComponent(component);
		validator.setComponentRepository(new XComponentRepository());
		boolean b = validator.validateEventHandlers(ClassUtils.getAbstractMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getAbstractMethodsFromSuperClasses(component.getAbstractSbbClass()), ClassUtils.getConcreteMethodsFromClass(component
				.getAbstractSbbClass()), ClassUtils.getConcreteMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertTrue("Sbb class has not been validated", b);

	}

	public void testSbbOne11EventsLackFire() throws Exception {
	
		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_EVENTS_LACK_FIRE));
		final SbbDescriptorImpl descriptor = specs.get(0);
		final SbbComponentImpl component = new SbbComponentImpl(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));
		component.setActivityContextInterface(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbActivityContextInterface()));
		SbbComponentValidator validator = new SbbComponentValidator();
		validator.setComponent(component);
		validator.setComponentRepository(new XComponentRepository());
		boolean b = validator.validateEventHandlers(ClassUtils.getAbstractMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getAbstractMethodsFromSuperClasses(component.getAbstractSbbClass()), ClassUtils.getConcreteMethodsFromClass(component
				.getAbstractSbbClass()), ClassUtils.getConcreteMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertFalse("Sbb class has  been validated- it should not - it lacks fire method", b);

	}

	public void testSbbOne11EventsHasThrow() throws Exception {

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_EVENTS_THROW));
		final SbbDescriptorImpl descriptor = specs.get(0);
		final SbbComponentImpl component = new SbbComponentImpl(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));
		component.setActivityContextInterface(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbActivityContextInterface()));
		SbbComponentValidator validator = new SbbComponentValidator();
		validator.setComponent(component);
		validator.setComponentRepository(new XComponentRepository());
		boolean b = validator.validateEventHandlers(ClassUtils.getAbstractMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getAbstractMethodsFromSuperClasses(component.getAbstractSbbClass()), ClassUtils.getConcreteMethodsFromClass(component
				.getAbstractSbbClass()), ClassUtils.getConcreteMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertFalse("Sbb class has  been validated - it should not - handler methods decalres throws", b);

	}

	public void testSbbOne11EventsWrongFireSignature() throws Exception {

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_FIRE_SIGNATURE));
		final SbbDescriptorImpl descriptor = specs.get(0);
		final SbbComponentImpl component = new SbbComponentImpl(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));
		component.setActivityContextInterface(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbActivityContextInterface()));
		SbbComponentValidator validator = new SbbComponentValidator();
		validator.setComponent(component);
		validator.setComponentRepository(new XComponentRepository());
		boolean b = validator.validateEventHandlers(ClassUtils.getAbstractMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getAbstractMethodsFromSuperClasses(component.getAbstractSbbClass()), ClassUtils.getConcreteMethodsFromClass(component
				.getAbstractSbbClass()), ClassUtils.getConcreteMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertFalse("Sbb class has  been validated- it should not - it declares fire method with wrong signature", b);

	}

	public void testSbbOne11EventsWrongFireSignature2() throws Exception {

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_FIRE_SIGNATURE2));
		final SbbDescriptorImpl descriptor = specs.get(0);
		final SbbComponentImpl component = new SbbComponentImpl(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));
		component.setActivityContextInterface(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbActivityContextInterface()));
		SbbComponentValidator validator = new SbbComponentValidator();
		validator.setComponent(component);
		validator.setComponentRepository(new XComponentRepository());
		boolean b = validator.validateEventHandlers(ClassUtils.getAbstractMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getAbstractMethodsFromSuperClasses(component.getAbstractSbbClass()), ClassUtils.getConcreteMethodsFromClass(component
				.getAbstractSbbClass()), ClassUtils.getConcreteMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertFalse("Sbb class has  been validated", b);

	}

	public void testSbbOne11EventsLackReceiver() throws Exception {

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_EVENTS_LACK_RECEIVER));
		final SbbDescriptorImpl descriptor = specs.get(0);
		final SbbComponentImpl component = new SbbComponentImpl(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));
		component.setActivityContextInterface(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbActivityContextInterface()));
		SbbComponentValidator validator = new SbbComponentValidator();
		validator.setComponent(component);
		validator.setComponentRepository(new XComponentRepository());
		boolean b = validator.validateEventHandlers(ClassUtils.getAbstractMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getAbstractMethodsFromSuperClasses(component.getAbstractSbbClass()), ClassUtils.getConcreteMethodsFromClass(component
				.getAbstractSbbClass()), ClassUtils.getConcreteMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertFalse("Sbb class has  been validated - it should not - it does not declare receiver method", b);

	}

	public void testSbbOne11EventsWrongReceiveSignature() throws Exception {

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_RECEIVE_SIGNATURE));
		final SbbDescriptorImpl descriptor = specs.get(0);
		final SbbComponentImpl component = new SbbComponentImpl(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));
		component.setActivityContextInterface(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbActivityContextInterface()));
		SbbComponentValidator validator = new SbbComponentValidator();
		validator.setComponent(component);
		validator.setComponentRepository(new XComponentRepository());
		boolean b = validator.validateEventHandlers(ClassUtils.getAbstractMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getAbstractMethodsFromSuperClasses(component.getAbstractSbbClass()), ClassUtils.getConcreteMethodsFromClass(component
				.getAbstractSbbClass()), ClassUtils.getConcreteMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertFalse("Sbb class has  been validated - it should not - it does not declare receiver method", b);

	}

	public void testSbbOne11EventsWrongReceiveSignature2() throws Exception {
	
		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_RECEIVE_SIGNATURE2));
		final SbbDescriptorImpl descriptor = specs.get(0);
		final SbbComponentImpl component = new SbbComponentImpl(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));
		component.setActivityContextInterface(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbActivityContextInterface()));
		SbbComponentValidator validator = new SbbComponentValidator();
		validator.setComponent(component);
		validator.setComponentRepository(new XComponentRepository());
		boolean b = validator.validateEventHandlers(ClassUtils.getAbstractMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getAbstractMethodsFromSuperClasses(component.getAbstractSbbClass()), ClassUtils.getConcreteMethodsFromClass(component
				.getAbstractSbbClass()), ClassUtils.getConcreteMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertFalse("Sbb class has  been validated - it should not - it does not declare receiver method", b);

	}

	public void testSbbOne11EventsConcreteFire() throws Exception {

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_CONCRETE_FIRE));
		final SbbDescriptorImpl descriptor = specs.get(0);
		final SbbComponentImpl component = new SbbComponentImpl(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));
		component.setActivityContextInterface(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbActivityContextInterface()));
		SbbComponentValidator validator = new SbbComponentValidator();
		validator.setComponent(component);
		validator.setComponentRepository(new XComponentRepository());
		boolean b = validator.validateEventHandlers(ClassUtils.getAbstractMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getAbstractMethodsFromSuperClasses(component.getAbstractSbbClass()), ClassUtils.getConcreteMethodsFromClass(component
				.getAbstractSbbClass()), ClassUtils.getConcreteMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertFalse("Sbb class has  been validated - it should not - it declares concrete impl of fire method", b);

	}

	public void testSbbOne11EventsWrongIES() throws Exception {
		// This test is just to see if it will fail, IES is checekd lower
	
		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_WRONG_IES));
		final SbbDescriptorImpl descriptor = specs.get(0);
		final SbbComponentImpl component = new SbbComponentImpl(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));
		component.setActivityContextInterface(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbActivityContextInterface()));
		SbbComponentValidator validator = new SbbComponentValidator();
		validator.setComponent(component);
		validator.setComponentRepository(new XComponentRepository());
		boolean b = validator.validateEventHandlers(ClassUtils.getAbstractMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getAbstractMethodsFromSuperClasses(component.getAbstractSbbClass()), ClassUtils.getConcreteMethodsFromClass(component
				.getAbstractSbbClass()), ClassUtils.getConcreteMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertFalse("Sbb class has  been validated - it should not - it declares wrong IES", b);

	}

	// FIXME: maybe move to different place?
	public void testInitialEventSelectorConstraints() throws Exception {
		// This test is just to see if it will fail, IES is checekd lower

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_IES_CONSTRAINTS));
		final SbbDescriptorImpl descriptor = specs.get(0);
		final SbbComponentImpl component = new SbbComponentImpl(descriptor);

		SbbComponentValidator validator = new SbbComponentValidator();
		validator.setComponent(component);
		validator.setComponentRepository(new XComponentRepository());

		// quicker way to validate :)
		EventEntryDescriptor event = descriptor.getEventEntries().values().iterator().next();
		component.setAbstractSbbClass(AbstractIES.class);
		boolean b = validator.validateInitialEventSelector(event, ClassUtils.getConcreteMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getConcreteMethodsFromSuperClasses(component.getAbstractSbbClass()));
		assertFalse("Initial Event Selector can not be abstract", b);

		component.setAbstractSbbClass(FinalIES.class);
		b = validator.validateInitialEventSelector(event, ClassUtils.getConcreteMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getConcreteMethodsFromSuperClasses(component.getAbstractSbbClass()));
		assertFalse("Initial Event Selector can not be final", b);

		component.setAbstractSbbClass(NotPublicIES.class);
		b = validator.validateInitialEventSelector(event, ClassUtils.getConcreteMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getConcreteMethodsFromSuperClasses(component.getAbstractSbbClass()));
		assertFalse("Initial Event Selector can must be public", b);

		component.setAbstractSbbClass(StaticIES.class);
		b = validator.validateInitialEventSelector(event, ClassUtils.getConcreteMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getConcreteMethodsFromSuperClasses(component.getAbstractSbbClass()));
		assertFalse("Initial Event Selector can not be static", b);

		component.setAbstractSbbClass(ThrowsIES.class);
		b = validator.validateInitialEventSelector(event, ClassUtils.getConcreteMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getConcreteMethodsFromSuperClasses(component.getAbstractSbbClass()));
		assertFalse("Initial Event Selector can not have throws clause", b);

		component.setAbstractSbbClass(ReturnTypeIES.class);
		b = validator.validateInitialEventSelector(event, ClassUtils.getConcreteMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getConcreteMethodsFromSuperClasses(component.getAbstractSbbClass()));
		assertFalse("Initial Event Selector  must have javax.slee.InitialEventSelector as return type", b);

	}

	public void testEventHandlersConstraints() throws Exception {
		// This test is just to see if it will fail, IES is checekd lower

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_HANDLERS_CONSTRAINTS));
		final SbbDescriptorImpl descriptor = specs.get(0);
		final SbbComponentImpl component = new SbbComponentImpl(descriptor);

		SbbComponentValidator validator = new SbbComponentValidator();
		validator.setComponent(component);
		validator.setComponentRepository(new XComponentRepository());

		// quicker way to validate :)
		EventEntryDescriptor event = descriptor.getEventEntries().values().iterator().next();
		String receiveMethodName = "on" + event.getEventName();
		String fireMethodName = "fire" + event.getEventName();

		component.setAbstractSbbClass(EventHandlersNotPublic.class);

		// ech this fails ;[
		// boolean b =
		// validator.validateFireMethodSignature(EventHandlersNotPublic.class.getMethod(fireMethodName,
		// XEvent.class,ActivityContextInterface.class,Address.class), "" );
		// assertFalse("Fire method must be public", b);
		// b=validator.validateReceiveMethodSignature(EventHandlersNotPublic.class.getMethod(receiveMethodName,
		// XEvent.class,ActivityContextInterface.class,EventContext.class), "");
		// assertFalse("Receive method must be public", b);

		boolean b = true;
		// b =
		// validator.validateFireMethodSignature(EventHandlersNotPublic.class.getMethod(fireMethodName,
		// XEvent.class,ActivityContextInterface.class,Address.class), "" );
		// assertFalse("Fire method must be public", b);
		b = validator.validateReceiveMethodSignature(EventHandlersStatic.class.getMethod(receiveMethodName, XEvent.class,
				ActivityContextInterface.class, EventContext.class), "");
		assertFalse("Receive method must not be static", b);

		b = validator.validateReceiveMethodSignature(EventHandlersFinal.class.getMethod(receiveMethodName, XEvent.class,
				ActivityContextInterface.class, EventContext.class), "");
		assertFalse("Receive method must not be final", b);

		b = validator.validateFireMethodSignature(EventHandlersThrows.class.getMethod(fireMethodName, XEvent.class, ActivityContextInterface.class,
				Address.class), "");
		assertFalse("Fire method must not declare throws", b);
		b = validator.validateReceiveMethodSignature(EventHandlersThrows.class.getMethod(receiveMethodName, XEvent.class,
				ActivityContextInterface.class, EventContext.class), "");
		assertFalse("Receive method must not declare throws", b);

		b = validator.validateFireMethodSignature(EventHandlersReturn.class.getMethod(fireMethodName, XEvent.class, ActivityContextInterface.class,
				Address.class), "");
		assertFalse("Fire method must not declare return type", b);
		b = validator.validateReceiveMethodSignature(EventHandlersReturn.class.getMethod(receiveMethodName, XEvent.class,
				ActivityContextInterface.class, EventContext.class), "");
		assertFalse("Receive method must not declare return type", b);

	}

	private static class XComponentRepository implements ComponentRepository {
		public SbbComponent getComponentByID(SbbID id) {

			return null;
		}

		public EventTypeComponent getComponentByID(EventTypeID id) {
			EventTypeComponentImpl event = new EventTypeComponentImpl(null);
			event.setEventTypeClass(XEvent.class);
			return event;
		}

		public ProfileSpecificationComponent getComponentByID(ProfileSpecificationID id) {
			// TODO Auto-generated method stub
			return null;
		}

		public LibraryComponent getComponentByID(LibraryID id) {
			// TODO Auto-generated method stub
			return null;
		}

		public ResourceAdaptorComponent getComponentByID(ResourceAdaptorID id) {
			// TODO Auto-generated method stub
			return null;
		}

		public ResourceAdaptorTypeComponent getComponentByID(ResourceAdaptorTypeID id) {
			// TODO Auto-generated method stub
			return null;
		}

		public ServiceComponent getComponentByID(ServiceID id) {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean isInstalled(ComponentID componentID) {
			// TODO Auto-generated method stub
			return false;
		}

		/* (non-Javadoc)
		 * @see org.mobicents.slee.core.component.ComponentRepository#getEventComponentIDs()
		 */
		public Set<EventTypeID> getEventComponentIDs() {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see org.mobicents.slee.core.component.ComponentRepository#getLibraryIDs()
		 */
		public Set<LibraryID> getLibraryIDs() {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see org.mobicents.slee.core.component.ComponentRepository#getProfileSpecificationIDs()
		 */
		public Set<ProfileSpecificationID> getProfileSpecificationIDs() {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see org.mobicents.slee.core.component.ComponentRepository#getReferringComponents(org.mobicents.slee.core.component.SleeComponent)
		 */
		public Set<SleeComponent> getReferringComponents(
				SleeComponent component) {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see org.mobicents.slee.core.component.ComponentRepository#getResourceAdaptorIDs()
		 */
		public Set<ResourceAdaptorID> getResourceAdaptorIDs() {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see org.mobicents.slee.core.component.ComponentRepository#getResourceAdaptorTypeIDs()
		 */
		public Set<ResourceAdaptorTypeID> getResourceAdaptorTypeIDs() {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see org.mobicents.slee.core.component.ComponentRepository#getSbbIDs()
		 */
		public Set<SbbID> getSbbIDs() {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see org.mobicents.slee.core.component.ComponentRepository#getServiceIDs()
		 */
		public Set<ServiceID> getServiceIDs() {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see org.mobicents.slee.core.component.ComponentRepository#getReferringComponents(javax.slee.ComponentID)
		 */
		public ComponentID[] getReferringComponents(ComponentID componentID)
				throws NullPointerException, UnrecognizedComponentException {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see org.mobicents.slee.core.component.ComponentRepository#putComponent(org.mobicents.slee.core.component.event.EventTypeComponent)
		 */
		public boolean putComponent(EventTypeComponent component) {
			// TODO Auto-generated method stub
			return false;
		}

		/* (non-Javadoc)
		 * @see org.mobicents.slee.core.component.ComponentRepository#putComponent(org.mobicents.slee.core.component.library.LibraryComponent)
		 */
		public boolean putComponent(LibraryComponent component) {
			// TODO Auto-generated method stub
			return false;
		}

		/* (non-Javadoc)
		 * @see org.mobicents.slee.core.component.ComponentRepository#putComponent(org.mobicents.slee.core.component.profile.ProfileSpecificationComponent)
		 */
		public boolean putComponent(ProfileSpecificationComponent component) {
			// TODO Auto-generated method stub
			return false;
		}

		/* (non-Javadoc)
		 * @see org.mobicents.slee.core.component.ComponentRepository#putComponent(org.mobicents.slee.core.component.ra.ResourceAdaptorComponent)
		 */
		public boolean putComponent(ResourceAdaptorComponent component) {
			// TODO Auto-generated method stub
			return false;
		}

		/* (non-Javadoc)
		 * @see org.mobicents.slee.core.component.ComponentRepository#putComponent(org.mobicents.slee.core.component.ratype.ResourceAdaptorTypeComponent)
		 */
		public boolean putComponent(ResourceAdaptorTypeComponent component) {
			// TODO Auto-generated method stub
			return false;
		}

		/* (non-Javadoc)
		 * @see org.mobicents.slee.core.component.ComponentRepository#putComponent(org.mobicents.slee.core.component.sbb.SbbComponent)
		 */
		public boolean putComponent(SbbComponent component) {
			// TODO Auto-generated method stub
			return false;
		}

		/* (non-Javadoc)
		 * @see org.mobicents.slee.core.component.ComponentRepository#putComponent(org.mobicents.slee.core.component.service.ServiceComponent)
		 */
		public boolean putComponent(ServiceComponent component) {
			// TODO Auto-generated method stub
			return false;
		}

		/* (non-Javadoc)
		 * @see org.mobicents.slee.core.component.ComponentRepository#removeComponent(javax.slee.EventTypeID)
		 */
		public void removeComponent(EventTypeID componentID) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see org.mobicents.slee.core.component.ComponentRepository#removeComponent(javax.slee.management.LibraryID)
		 */
		public void removeComponent(LibraryID componentID) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see org.mobicents.slee.core.component.ComponentRepository#removeComponent(javax.slee.profile.ProfileSpecificationID)
		 */
		public void removeComponent(ProfileSpecificationID componentID) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see org.mobicents.slee.core.component.ComponentRepository#removeComponent(javax.slee.resource.ResourceAdaptorID)
		 */
		public void removeComponent(ResourceAdaptorID componentID) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see org.mobicents.slee.core.component.ComponentRepository#removeComponent(javax.slee.resource.ResourceAdaptorTypeID)
		 */
		public void removeComponent(ResourceAdaptorTypeID componentID) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see org.mobicents.slee.core.component.ComponentRepository#removeComponent(javax.slee.SbbID)
		 */
		public void removeComponent(SbbID componentID) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see org.mobicents.slee.core.component.ComponentRepository#removeComponent(javax.slee.ServiceID)
		 */
		public void removeComponent(ServiceID componentID) {
			// TODO Auto-generated method stub
			
		}
	}

}

abstract class AbstractIES {
	public abstract InitialEventSelector myInitialEventSelector(InitialEventSelector ies);
}

abstract class FinalIES {
	final public InitialEventSelector myInitialEventSelector(InitialEventSelector ies) {
		return null;
	}
}

abstract class NotPublicIES {
	protected InitialEventSelector myInitialEventSelector(InitialEventSelector ies) {
		return null;
	}
}

abstract class StaticIES {
	public static InitialEventSelector myInitialEventSelector(InitialEventSelector ies) {
		return null;
	}
}

abstract class ThrowsIES {
	public InitialEventSelector myInitialEventSelector(InitialEventSelector ies) throws Error {
		return null;
	}
}

abstract class ReturnTypeIES {
	public void myInitialEventSelector(InitialEventSelector ies) throws Error {
		return;
	}

}

abstract class EventHandlersNotPublic {
	void onCustomEventFive(XEvent event, ActivityContextInterface aci, EventContext ctx) {

	}

	abstract void fireCustomEventFive(XEvent event, ActivityContextInterface aci, Address address);
}

abstract class EventHandlersStatic {
	public static void onCustomEventFive(XEvent event, ActivityContextInterface aci, EventContext ctx) {

	}

	public abstract void fireCustomEventFive(XEvent event, ActivityContextInterface aci, Address address);
}

abstract class EventHandlersThrows {
	public void onCustomEventFive(XEvent event, ActivityContextInterface aci, EventContext ctx) throws Error {

	}

	public abstract void fireCustomEventFive(XEvent event, ActivityContextInterface aci, Address address) throws Error;
}

abstract class EventHandlersReturn {
	public int onCustomEventFive(XEvent event, ActivityContextInterface aci, EventContext ctx) {
		return 0;
	}

	public abstract int fireCustomEventFive(XEvent event, ActivityContextInterface aci, Address address);
}

abstract class EventHandlersFinal {
	public final void onCustomEventFive(XEvent event, ActivityContextInterface aci, EventContext ctx) {

	}

	public abstract void fireCustomEventFive(XEvent event, ActivityContextInterface aci, Address address);
}