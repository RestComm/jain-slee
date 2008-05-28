
package org.mobicents.csapi.jr.slee.ui;

import java.io.Serializable;
 
import org.mobicents.csapi.jr.slee.ui.CallUITarget;
import org.mobicents.csapi.jr.slee.ui.CallLegUITarget;
import org.mobicents.csapi.jr.slee.ui.MultiPartyCallUITarget;

/**
 * This class is used to pass a reference to a ongoing call or call leg to the
 *  User Interaction Service. Call related user interaction may be performed on
 *  the call.
 */
public class TpUITargetObject implements Serializable {

    
	private TpUITargetObjectType _discriminator = null;
	private java.lang.Object _object;
    

    public TpUITargetObject() { 
    
    }

    /**
     * Returns the callIdentifier
     * @return callIdentifier
     */
    public CallUITarget getCallUITarget()throws org.mobicents.csapi.jr.slee.InvalidUnionAccessorException { 
		if (_discriminator != ((TpUITargetObjectType) TpUITargetObjectType.P_UI_TARGET_OBJECT_CALL)) {
				   throw new org.mobicents.csapi.jr.slee.InvalidUnionAccessorException();
			   }
			   return ((CallUITarget) _object);
    }


   /**
     * Returns the multiPartyCallIdentifier
     * @return multiPartyCallIdentifier
     */
    public MultiPartyCallUITarget getMultiPartyCallUITarget() throws org.mobicents.csapi.jr.slee.InvalidUnionAccessorException{ 
		if (_discriminator != ((TpUITargetObjectType) TpUITargetObjectType.P_UI_TARGET_OBJECT_MULTI_PARTY_CALL)) {
			throw new org.mobicents.csapi.jr.slee.InvalidUnionAccessorException();
		}
		return ((MultiPartyCallUITarget) _object);
    }


     /**
     * Returns the callLegIdentifier
     * @return callLegIdentifier
     */
    public CallLegUITarget getCallLegUITarget()throws org.mobicents.csapi.jr.slee.InvalidUnionAccessorException { 

		if (_discriminator != ((TpUITargetObjectType) TpUITargetObjectType.P_UI_TARGET_OBJECT_CALL_LEG)) {
				  throw new org.mobicents.csapi.jr.slee.InvalidUnionAccessorException();
			  }
			  return ((CallLegUITarget) _object);
    }
	
	/**
	 *  Set the GCCS Call.
	 *
	 *@param  value  a <code>org.mobicents.csapi.jr.slee.cc.gccs.Call</code> value
	 */
	public void setCallUITarget(CallUITarget value) {
		_discriminator = (TpUITargetObjectType) TpUITargetObjectType.P_UI_TARGET_OBJECT_CALL;
		_object = value;
	}


	/**
	 *  Set the MPCC Call.
	 *
	 *@param  value  a <code>org.mobicents.csapi.jr.slee.cc.mpccs.MultiPartyCall</code> value
	 */
	public void setMultiPartyCallUITarget(MultiPartyCallUITarget value) {
		_discriminator = (TpUITargetObjectType) TpUITargetObjectType.P_UI_TARGET_OBJECT_MULTI_PARTY_CALL;
		_object = value;
	}


	/**
	 *  Set the MPCC Call Leg. 
	 *
	 *@param  value  a org.mobicents.csapi.jr.slee.cc.mpccs.CallLeg</code> value
	 */
	public void setCallLegUITarget(CallLegUITarget value) {
		_discriminator = (TpUITargetObjectType) TpUITargetObjectType.P_UI_TARGET_OBJECT_CALL_LEG;
		_object = value;
	}

	
	/**
		*  Get the type of object to which UI is connected.
		*
		*@return                                                 a <code>
		*      UITargetObjectType</code> value
		*@exception  org.mobicents.csapi.jr.slee.InvalidUnionAccessorException  Please refer to
		*      the relevant Parlay documentation
		*/
	   public TpUITargetObjectType getDiscriminator() throws org.mobicents.csapi.jr.slee.InvalidUnionAccessorException {
		   if (_discriminator == null) {
			   throw new org.mobicents.csapi.jr.slee.InvalidUnionAccessorException();
		   }
		   return _discriminator;
	   }







} // TpUITargetObject

