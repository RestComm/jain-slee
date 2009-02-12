package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query;

/**
 * 
 * MQueryExpressionType.java
 *
 * <br>Project:  mobicents
 * <br>3:27:19 AM Feb 12, 2009 
 * <br>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 */
public enum MQueryExpressionType {

  Compare(1), LongestPrefixMatch(2), HasPrefix(3), RangeMatch(4), And(5), Or(6), Not(7);

  private int code = -1;

  MQueryExpressionType(int val)
  {
    this.code=val;
  }

  public int getCode()
  {
    return code;
  }

}
