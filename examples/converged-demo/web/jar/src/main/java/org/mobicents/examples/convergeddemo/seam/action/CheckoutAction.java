/*
 * JBoss, Home of Professional Open Source
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.mobicents.examples.convergeddemo.seam.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.naming.*;
import javax.slee.*;
import javax.slee.connection.*;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.bpm.CreateProcess;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.faces.FacesMessages;

import org.mobicents.examples.convergeddemo.seam.model.Customer;
import org.mobicents.examples.convergeddemo.seam.model.Inventory;
import org.mobicents.examples.convergeddemo.seam.model.OrderLine;
import org.mobicents.examples.convergeddemo.seam.model.Order;
import org.mobicents.examples.convergeddemo.seam.model.Product;
import org.mobicents.slee.service.events.CustomEvent;

@Stateful
@Name("checkout")
public class CheckoutAction implements Checkout, Serializable {
	private static final long serialVersionUID = -4651884454184474207L;

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	EntityManager em;

	@In(value = "currentUser", required = false)
	Customer customer;

	@In(create = true)
	ShoppingCart cart;

	@Out(scope = ScopeType.CONVERSATION, required = false)
	Order currentOrder;

	@Out(scope = ScopeType.CONVERSATION, required = false)
	Order completedOrder;

	@Out(scope = ScopeType.BUSINESS_PROCESS, required = false)
	long orderId;
	
	@Out(scope = ScopeType.BUSINESS_PROCESS, required = false)
	BigDecimal amount = BigDecimal.ZERO;

	@Out(value = "userName", scope = ScopeType.BUSINESS_PROCESS, required = false)
	String userName;

	@Out(value = "customerfullname", scope = ScopeType.BUSINESS_PROCESS, required = false)
	String customerFullName;

	@Out(value = "cutomerphone", scope = ScopeType.BUSINESS_PROCESS, required = false)
	String customerPhone;

	@Begin(nested = true, pageflow = "checkout")
	public void createOrder() {
		currentOrder = new Order();

		for (OrderLine line : cart.getCart()) {
			currentOrder.addProduct(em.find(Product.class, line.getProduct()
					.getProductId()), line.getQuantity());
		}

		currentOrder.calculateTotals();
		cart.resetCart();
	}

	@End
	@CreateProcess(definition = "OrderManagement", processKey = "#{completedOrder.orderId}")
	@Restrict("#{identity.loggedIn}")
	public void submitOrder() {
		try {
			completedOrder = purchase(customer, currentOrder);

			orderId = completedOrder.getOrderId();
			amount = completedOrder.getNetAmount();
			userName = completedOrder.getCustomer().getUserName();

			customerFullName = completedOrder.getCustomer().getFirstName()
					+ " " + completedOrder.getCustomer().getLastName();
			customerPhone = completedOrder.getCustomer().getPhone();

			fireEvent(orderId, amount, customerFullName, customerPhone, userName);

		} catch (InsufficientQuantityException e) {
			for (Product product : e.getProducts()) {
				Contexts.getEventContext().set("prod", product);
				FacesMessages.instance().addFromResourceBundle(
						"checkoutInsufficientQuantity");
			}
		}
	}

	private Order purchase(Customer customer, Order order)
			throws InsufficientQuantityException {
		order.setCustomer(customer);
		order.setOrderDate(new Date());

		List<Product> errorProducts = new ArrayList<Product>();
		for (OrderLine line : order.getOrderLines()) {
			Inventory inv = line.getProduct().getInventory();
			if (!inv.order(line.getQuantity())) {
				errorProducts.add(line.getProduct());
			}
		}

		if (errorProducts.size() > 0) {
			throw new InsufficientQuantityException(errorProducts);
		}

		order.calculateTotals();
		em.persist(order);

		return order;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void fireEvent(final long orderId,final  BigDecimal ammount,
			final String customerFullName,final  String customerPhone,final  String userName) {

		Thread t = new Thread(new Runnable() {
			
			public void run() {
				try {

					InitialContext ic = new InitialContext();

					SleeConnectionFactory factory = (SleeConnectionFactory) ic
							.lookup("java:/MobicentsConnectionFactory");

					SleeConnection conn1 = null;
					conn1 = factory.getConnection();

					ExternalActivityHandle handle = conn1.createActivityHandle();

					EventTypeID requestType = conn1.getEventTypeID(
							"org.mobicents.slee.service.dvddemo.ORDER_PLACED",
							"org.mobicents", "1.0");
					CustomEvent customEvent = new CustomEvent(orderId, ammount,
							customerFullName, customerPhone, userName);

					conn1.fireEvent(customEvent, requestType, handle, null);
					conn1.close();

				} catch (Exception e) {

					e.printStackTrace();

				}
				
			}
		});
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Remove
	public void destroy() {
	}

}
