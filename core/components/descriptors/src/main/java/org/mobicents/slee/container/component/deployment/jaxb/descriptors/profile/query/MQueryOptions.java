package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query;

/**
 * Start time:11:20:06 2009-01-22<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MQueryOptions {

  private boolean readOnly;
  private long maxMatches;

  public MQueryOptions(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.QueryOptions queryOptions11)
  {
    this.readOnly = Boolean.parseBoolean(queryOptions11.getReadOnly());
    this.maxMatches = queryOptions11.getMaxMatches() == null ? Long.MAX_VALUE : Long.parseLong(queryOptions11.getMaxMatches());
  }

  public long getMaxMatches() {
    return maxMatches;
  }

  public boolean isReadOnly() {
    return readOnly;
  }

}
