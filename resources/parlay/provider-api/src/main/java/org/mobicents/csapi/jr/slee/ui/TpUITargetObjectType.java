package org.mobicents.csapi.jr.slee.ui;

/**
 *  Defines the type of object where User Interaction should be performed upon.
 *
 *
 */
public final class TpUITargetObjectType implements java.io.Serializable {

    private int _value;
    private static int _size = 3;
    private static TpUITargetObjectType[] _array = new TpUITargetObjectType[_size];

    /**
     *  User-interaction will be performed on a complete Call.
     */
    public final static int _P_UI_TARGET_OBJECT_CALL = 0;

    /**
     *  Target object is a Generic Call Contol Service (GCCS) Call.
     */
    public final static TpUITargetObjectType P_UI_TARGET_OBJECT_CALL = new TpUITargetObjectType(_P_UI_TARGET_OBJECT_CALL);

    /**
     *  User-interaction will be performed on a complete Multi-party Call.
     */
    public final static int _P_UI_TARGET_OBJECT_MULTI_PARTY_CALL = 1;

    /**
     *  Target object is a MultiParty Call Contol Service (MPCCS) Call.
     */
    public final static TpUITargetObjectType P_UI_TARGET_OBJECT_MULTI_PARTY_CALL =
        new TpUITargetObjectType(_P_UI_TARGET_OBJECT_MULTI_PARTY_CALL);

    /**
     *  User-interaction will be performed on a single Call Leg.
     */
    public final static int _P_UI_TARGET_OBJECT_CALL_LEG = 2;

    /**
     *  Target object is a MultiParty Call Contol Service (MPCCS) Call Leg.
     */
    public final static TpUITargetObjectType P_UI_TARGET_OBJECT_CALL_LEG =
        new TpUITargetObjectType(_P_UI_TARGET_OBJECT_CALL_LEG);


    /**
     *  Private constructor used internally to construct static objects
     *
     *@param  value  Please refer to the relevant Parlay documentation
     */
    private TpUITargetObjectType(int value) {
        this._value = value;
		TpUITargetObjectType._array[this._value] = this;
    }

    /**
     *  Number representing enum type
     *
     *@return    int number representing type of object, this is defined within
     *      the class
     */
    public int getValue() {
        return _value;
    }

    /**
     *  toString method, overrides the toString() method in class
     *  java.lang.Object
     *
     *@return    String The stringified version of the TpUITargetObjectType object
     */
    public String toString() {
        switch (_value) {
            case _P_UI_TARGET_OBJECT_CALL :
                return "P_UI_TARGET_OBJECT_CALL";
            case _P_UI_TARGET_OBJECT_MULTI_PARTY_CALL :
                return "P_UI_TARGET_OBJECT_MULTI_PARTY_CALL";
            case _P_UI_TARGET_OBJECT_CALL_LEG :
                return "P_UI_TARGET_OBJECT_CALL_LEG";
            default :
                return "ERROR";
        }
    }

    /**
     *  Accepts values in the range of 0 to 3
     *
     *@param  value                                    number representing type
     *      of object, this is defined within the class
     *@return                                          The object value
     *@throws  org.mobicents.csapi.jr.slee.InvalidEnumValueException  thrown if invalid value
     *      is passed into method
     */
    public static TpUITargetObjectType getObject(int value) throws org.mobicents.csapi.jr.slee.InvalidEnumValueException {
        if (value >= 0 && value < _size) {
            return _array[value];
        } else {
            throw new org.mobicents.csapi.jr.slee.InvalidEnumValueException();
        }
    }

}
// TpUITargetObjectType
