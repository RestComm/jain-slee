package org.mobicents.slee.resource.map;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;

import org.mobicents.protocols.ss7.map.api.MAPDialog;

/**
 * 
 * @author amit bhayani
 * 
 */
public class MAPDialogActivityHandle implements Serializable, ActivityHandle {

	private MAPDialog mapDialog;

	public MAPDialogActivityHandle(MAPDialog mapDialog) {
		this.mapDialog = mapDialog;
	}

	@Override
	public int hashCode() {
		return this.mapDialog.getDialogId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((MAPDialogActivityHandle) obj).mapDialog.getDialogId().equals(this.mapDialog.getDialogId());
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "MAPDialogActivityHandle(id=" + this.mapDialog.getDialogId() + ")";
	}

	public MAPDialog getMAPDialog() {
		return this.mapDialog;
	}

}
