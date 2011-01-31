package org.mobicents.slee.tools.maven.plugins.du.ant;

import java.util.LinkedList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DeployConfig {

  /**
   * <deploy-config>
   *   <ra-entity />
   *   <...>
   * <deploy-config>
   **/
  public static DeployConfig parse(Element element)
  {
    LinkedList<RAEntity> raEntities = new LinkedList<RAEntity>();
    NodeList nodeList = element.getElementsByTagName("ra-entity");

    for(int i = 0; i < nodeList.getLength(); i++) {
      raEntities.add(RAEntity.parse(((Element)nodeList.item(i))));
    }

    return new DeployConfig(raEntities);
  }

  public DeployConfig(LinkedList<RAEntity> raEntities) {
    this.raEntities = raEntities;
  }

  private final LinkedList<RAEntity> raEntities;

  public LinkedList<RAEntity> getRaEntities() {
    return raEntities;
  }

}

