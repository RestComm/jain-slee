package org.mobicents.slee.container.component.validator.test;

public abstract class SbbSuperClass  {

	
	// ------ LIFE CYCLE
	// Method descriptor #6 ()V
	public void sbbPassivate() {
	}

	// Method descriptor #6 ()V
	public void sbbLoad() {
	}

	// Method descriptor #6 ()V
	public void sbbStore() {
	}

	// Method descriptor #6 ()V
	public void sbbRemove() {
	}

	// Method descriptor #17
	// (Ljava/lang/Exception;Ljava/lang/Object;Ljavax/slee/ActivityContextInterface;)V
	public void sbbExceptionThrown(java.lang.Exception arg0,
			java.lang.Object arg1, javax.slee.ActivityContextInterface arg2) {

	}

	// Method descriptor #19 (Ljavax/slee/RolledBackContext;)V
	public void sbbRolledBack(javax.slee.RolledBackContext arg0) {

	}

	
	// ------ SBB LO
	
	
	
	public void doSomeMagicInSuperInterface(int i, String s)
	{
		
	}
	
	
	
	// ------ CMP
	
	public abstract void setSuperCMP(int i);
	public abstract int getSuperCMP();
	public abstract void setSharedCMP(String t);
	
}
