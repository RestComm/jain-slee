package org.mobicents.slee.resource.persistence.ra;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.mobicents.slee.resource.persistence.ratype.PersistenceResourceAdaptorSbbInterface;
import org.mobicents.slee.resource.persistence.ratype.SbbEntityManager;

public class PersistenceResourceAdaptorSbbInterfaceImpl implements
		PersistenceResourceAdaptorSbbInterface {

	private ActivityManipulation am = null;

	private HashMap<String,EntityManagerFactory> emfsHeldHere=new HashMap<String,EntityManagerFactory>();

	private  boolean hasBeenClosed=false;

	public PersistenceResourceAdaptorSbbInterfaceImpl(ActivityManipulation am) {

		this.am = am;

	}

	public void close() {
		
		if(this.hasBeenClosed)
			return;
		for(String s : this.emfsHeldHere.keySet())
		{
			//we have to inform
			this.am.unsubscribeForEntityManagerFactoryForPU(s);
		}
		
		
		this.hasBeenClosed=true;
		
	}

	public boolean hasBeenClosed()
	{
		return this.hasBeenClosed;
	}
	public EntityManager createEntityManager(Map emfProperties, String puName){
		if(this.hasBeenClosed)
			throw new RuntimeException("Wrong state RASbbInterface has been closed.");
		return prepareManager(emfProperties,null, puName);
	}

	public EntityManager createEntityManager(Map emfProperties,Map emProps, String puName) {
		if(this.hasBeenClosed)
			throw new RuntimeException("Wrong state RASbbInterface has been closed.");
		return prepareManager(emfProperties ,emProps, puName);
	}

	public boolean isOpen(String puName) {
		// we should always return true;
		//only false in error condition and when emf doesnt exist
		EntityManagerFactory t_emf=null;
		if(emfsHeldHere.containsKey(puName))
		{
			
			t_emf=(EntityManagerFactory) emfsHeldHere.get(puName);
			//this shold always be true;
			return t_emf.isOpen();
		}
		else
		{
			
			return this.am.isEMFOpen(puName);
			
			
		}
		
	}

	// HELPER METHODS

	private SbbEntityManager prepareManager(Map emfProperties,Map emProperties, String puName) {

		EntityManagerFactory t_emf=null;
		EntityManager em = null;
		
		t_emf=this.am.subscribeForEntityManagerFactoryForPU(puName,emfProperties);
		if(t_emf==null)
			return null;
		
		this.emfsHeldHere.put(puName, t_emf);
		
		if (emProperties != null)
		{
	
			em = t_emf.createEntityManager(emProperties);
		}
		else
		{
			em = t_emf.createEntityManager();
		}
		SbbEntityManagerImpl sbe = new SbbEntityManagerImpl(em, am);
		am.addActivity(sbe);
		return sbe;
	}
	
	
	public void finalize()
	{
		
		this.close();
		
	}
}
