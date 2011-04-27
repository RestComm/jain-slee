/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package net.java.slee.resource.sip;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.ResponseEvent;
import javax.sip.message.Response;

/**
 * The event that signals the arrival of a response that forks the dialog sent
 * in the request.
 * 
 */
public class DialogForkedEvent extends ResponseEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the new dialog that is the result of the forking
	 */
	private final Dialog forkedDialog;

	/**
	 * 
	 * @param source
	 * @param clientTransaction
	 * @param originalDialog
	 * @param forkedDialog
	 * @param response
	 */
	public DialogForkedEvent(Object source,
			ClientTransaction clientTransaction, Dialog originalDialog,
			Dialog forkedDialog, Response response) {
		super(source, clientTransaction, originalDialog, response);
		this.forkedDialog = forkedDialog;
	}

	/**
	 * Retrieves the new dialog that is the result of the forking.
	 * 
	 * @return
	 */
	public Dialog getForkedDialog() {
		return this.forkedDialog;
	}

}
