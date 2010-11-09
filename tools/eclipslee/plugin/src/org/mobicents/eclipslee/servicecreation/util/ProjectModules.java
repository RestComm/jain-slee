package org.mobicents.eclipslee.servicecreation.util;

import java.util.ArrayList;

public class ProjectModules {

  public static final String[] AVAILABLE_MODULES = {"events", "sbb", "profile-spec", "ratype", "ra", "library", "du"};
  
  ArrayList<String> modules;

  private boolean events = false;
  private boolean sbb = false;
  private boolean profileSpec = false;
  private boolean raType = false;
  private boolean ra = false;
  private boolean library = false;
  private boolean deployableUnit = true;

  private int count = 0;

  public ProjectModules(ArrayList<String> modules) {
    this.modules = modules;
    this.count = 0;

    for(String module : modules) {
      if(module.equals("events")) {
        events = true;
        count++;
      }
      else if(module.equals("sbb")) {
        sbb = true;
        count++;
      }
      else if(module.equals("profile-spec")) {
        profileSpec = true;
        count++;
      }
      else if(module.equals("ratype")) {
        raType = true;
        count++;
      }
      else if(module.equals("ra")) {
        ra = true;
        count++;
      }
      else if(module.equals("library")) {
        library = true;
        count++;
      }
      else if(module.equals("du")) {
        deployableUnit = true;
        count++;
      }
      else {
        // Whatever this is, is wrong.. ignore.
      }
    }
  }

  public boolean hasEvents() {
    return events;
  }

  public boolean hasSBB() {
    return sbb;
  }

  public boolean hasProfileSpec() {
    return profileSpec;
  }

  public boolean hasRAType() {
    return raType;
  }

  public boolean hasRA() {
    return ra;
  }

  public boolean hasLibrary() {
    return library;
  }

  public boolean hasDeployableUnit() {
    return deployableUnit;
  }
  
  public ArrayList<String> getModules() {
    return modules;
  }
  
  public int getModuleCount() {
    return count;
  }

}
