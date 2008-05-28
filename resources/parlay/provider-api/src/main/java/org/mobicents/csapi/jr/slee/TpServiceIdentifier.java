
package org.mobicents.csapi.jr.slee;

import java.io.Serializable;


/**
 * Unamebiguously identifies a service instance. All service idnetifiers must provide 
 * an implementation of equals() and hashCode() and support serialization.
 * 
 */
public class TpServiceIdentifier implements Serializable {

/**
* Constructor
*/
public TpServiceIdentifier(int serviceID) {
   this.serviceID = serviceID;
}

/**
 * Retuen the multiPartyCallID
 * @return int
 */
public int getServiceID() {
  return serviceID;
}  



/**
 * @see java.lang.Object#equals(Object)
 *
 */
public boolean equals(Object o) {
 if(!(o instanceof TpServiceIdentifier)) { 
       return false;
 }
 TpServiceIdentifier inst = (TpServiceIdentifier) o; 

  if(this.serviceID != inst.serviceID) {
        return false;
   }
  return true;

}

/*
 * @see java.lang.Object#hashCode()
 */
public int hashCode() {
   return serviceID;
}

/*
 * @see java.lang.Object#clone()
 */
 public Object clone() {
  TpServiceIdentifier inst = new TpServiceIdentifier(
     this.serviceID);
   return inst;   

 }



/*
 *
 */
 public String toString() {
   StringBuffer result = new StringBuffer(
    "TpServiceIdentifier:: serviceID = ");
   result.append(serviceID);
   result.append(";");
   return result.toString(); 
 
 }

 //VARIABLES
 //..................................................
 
 private int serviceID;


}//TpServiceIdentifier
