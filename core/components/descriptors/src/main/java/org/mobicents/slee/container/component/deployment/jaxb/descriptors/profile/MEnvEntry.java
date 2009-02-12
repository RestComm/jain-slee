package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

/**
 * Start time:17:22:14 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MEnvEntry {

  private org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.EnvEntry envEntry11;

  private String description;

  private String envEntryName;
  private String envEntryType;
  private String envEntryValue;

  public MEnvEntry(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.EnvEntry envEntry11)
  {
    this.envEntry11 = envEntry11;

    this.description = envEntry11.getDescription() == null ? null : envEntry11.getDescription().getvalue();

    this.envEntryName = envEntry11.getEnvEntryName().getvalue();
    this.envEntryType = envEntry11.getEnvEntryType().getvalue();
    this.envEntryValue = envEntry11.getEnvEntryValue() == null ? null : envEntry11.getEnvEntryValue().getvalue();
  }

  public String getDescription() {
    return description;
  }

  public String getEnvEntryName() {
    return envEntryName;
  }

  public String getEnvEntryValue() {
    return envEntryValue;
  }

  public String getEnvEntryType() {
    return envEntryType;
  }

}
