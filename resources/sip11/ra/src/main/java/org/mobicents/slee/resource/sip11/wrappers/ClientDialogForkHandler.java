/**
 * 
 */
package org.mobicents.slee.resource.sip11.wrappers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.mobicents.slee.resource.sip11.DialogWithoutIdActivityHandle;
import org.mobicents.slee.resource.sip11.SipResourceAdaptor;

/**
 * @author martins
 * 
 */
public class ClientDialogForkHandler implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DialogWithoutIdActivityHandle master;

	private DialogWithoutIdActivityHandle forkWinner;

	private transient volatile ConcurrentHashMap<String, DialogWithoutIdActivityHandle> forks;

	private AtomicBoolean closed = new AtomicBoolean(false);

	/**
	 * 
	 */
	public ClientDialogForkHandler() {
		this.master = null;
	}

	/**
	 * 
	 */
	public ClientDialogForkHandler(DialogWithoutIdActivityHandle master) {
		this.master = master;
	}

	/**
	 * @return the master
	 */
	public DialogWithoutIdActivityHandle getMaster() {
		return master;
	}

	/**
	 * @return the forkWinner
	 */
	public DialogWithoutIdActivityHandle getForkWinner() {
		return forkWinner;
	}

	public void addFork(SipResourceAdaptor ra,
			DialogWithoutIdActivityHandle fork) {
		if (master != null) {
			throw new IllegalStateException(
					"non initial dialogs can't add forks");
		}
		if (!closed.get()) {
			// forking not closed, add fork
			if (forks == null) {
				synchronized (this) {
					// lazy init with volatile
					if (forks == null) {
						forks = new ConcurrentHashMap<String, DialogWithoutIdActivityHandle>(
								3);
					}
				}
			}
			forks.put(fork.getRemoteTag(), fork);
		} else {
			// late fork add, terminate it
			terminateFork(ra, fork);
		}
	}

	public DialogWithoutIdActivityHandle getFork(String remoteTag) {
		return forks != null ? forks.get(remoteTag) : null;
	}

	/**
	 * Indicates that the fork with the specified remote tag was confirmed. The
	 * related dialog must now be the "owner" of the wrapped dialog and the
	 * original dialog must be deleted.
	 * 
	 * @param ra
	 * @param original
	 * @param forkRemoteTag
	 */
	public void forkConfirmed(SipResourceAdaptor ra,
			ClientDialogWrapper original, ClientDialogWrapper fork) {
		if (master != null) {
			throw new IllegalStateException(
					"non initial dialogs can't be used to confirme a fork");
		}
		if (closed.compareAndSet(false, true)) {
			if (forks != null) {
				// remove the winner
				forkWinner = forks.remove(fork.data.getLocalRemoteTag());
				// terminate losers
				for (DialogWithoutIdActivityHandle loser : forks.values()) {
					terminateFork(ra, loser);
				}
				forks = null;
			}
			original.wrappedDialog = null;
			original.delete();
			// close forking handler on fork and link wrapped dialog to forked dialog
			fork.data.getForkHandler().closed.set(true);
			fork.wrappedDialog.setApplicationData(fork);
		}		
	}

	/**
	 * Indicates the forking ended, either by the original dialog get confirmed
	 * or by error. All forks will be deleted.
	 * 
	 * @param ra
	 */
	public void terminate(SipResourceAdaptor ra) {
		if (master != null) {
			throw new IllegalStateException(
					"non initial dialogs can't terminate forking");
		}
		if (closed.compareAndSet(false, true)) {
			if (forks != null) {
				// terminate all
				for (DialogWithoutIdActivityHandle fork : forks.values()) {
					terminateFork(ra, fork);
				}
				forks = null;
			}
		}
	}

	public boolean isForking() {
		return forks != null || !closed.get();
	}

	private void terminateFork(SipResourceAdaptor ra,
			DialogWithoutIdActivityHandle fork) {
		final ClientDialogWrapper dw = (ClientDialogWrapper) ra
				.getActivity(fork);
		if (dw != null) {
			dw.setWrappedDialog(null);
			dw.delete();
		}
	}

	// SERIALIZATION

	private DialogWithoutIdActivityHandle[] EMPTY_HANDLE_ARRAY = {};

	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();

		final DialogWithoutIdActivityHandle[] forksArray = (forks == null) ? null
				: forks.values().toArray(EMPTY_HANDLE_ARRAY);
		stream.writeObject(forksArray);
	}

	private void readObject(ObjectInputStream stream) throws IOException,
			ClassNotFoundException {

		stream.defaultReadObject();

		final DialogWithoutIdActivityHandle[] forksArray = (DialogWithoutIdActivityHandle[]) stream
				.readObject();
		if (forksArray != null && forksArray.length > 0) {
			forks = new ConcurrentHashMap<String, DialogWithoutIdActivityHandle>(
					forksArray.length);
			for (DialogWithoutIdActivityHandle fork : forksArray) {
				forks.put(fork.getRemoteTag(), fork);
			}
		}
	}

}
