package org.mobicents.slee.container.component.validator.profile.tableinterface;

import javax.slee.SLEEException;
import javax.slee.profile.ProfileTable;


public interface ProfileTableInterfaceNotEnoughParameters extends ProfileTable {

	//<query-parameter name="booleanParameter" type="boolean" />
	//<query-parameter name="booleanWParameter" type="java.lang.Boolean" />
	//<query-parameter name="stringParameter" type="java.lang.String" />
	public java.util.Collection queryFirstQuery(Boolean booleanWParameter,String stringParameter) throws javax.slee.TransactionRequiredLocalException,SLEEException;
//	<query-parameter name="stringParameter" type="java.lang.String" />
//	<query-parameter name="integerWParameter" type="java.lang.Integer" />
	public java.util.Collection querySecondQuery(String stringParameter,Integer integerWParameter) throws javax.slee.TransactionRequiredLocalException,SLEEException;
	
}
