package org.mobicents.sleetests.sbb.events;
import java.util.Random;
import java.io.Serializable;

public final class NotifyEvent implements Serializable {

	private static final long serialVersionUID = -6406917246325807670L;

	public NotifyEvent() {
		id = new Random().nextLong() ^ System.currentTimeMillis();
	}

	public boolean equals(Object o) {
		if (o == this) return true;
		if (o == null) return false;
		return (o instanceof NotifyEvent) && ((NotifyEvent)o).id == id;
	}
	
	public int hashCode() {
		return (int) id;
	}
	
	public String toString() {
		return "NotifyEvent[" + hashCode() + "]";
	}

	private final long id;
}
