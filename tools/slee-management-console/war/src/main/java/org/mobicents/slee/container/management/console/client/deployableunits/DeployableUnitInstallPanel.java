/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.slee.container.management.console.client.deployableunits;

import org.mobicents.slee.container.management.console.client.Logger;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.ControlContainer;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Stefano Zappaterra
 * 
 */
public class DeployableUnitInstallPanel extends FormPanel {

  // protected DeployableUnitsCard deployableUnitsCard;

  private String extractMessage(String result) {

    // Fix:
    // Firefox 2 encapsulates the text inside <pre> tag
    String startPreTag = "<pre>";
    String endPreTag = "</pre>";

    result = result.trim();

    if (result.startsWith(startPreTag) && result.endsWith(endPreTag)) {
      result = result.substring(startPreTag.length(), result.length() - endPreTag.length());
    }

    return result;
  }

  public DeployableUnitInstallPanel(BrowseContainer browseContainer) {
    super();

    setAction("DeployableUnitsInstallService");
    setEncoding(FormPanel.ENCODING_MULTIPART);
    setMethod(FormPanel.METHOD_POST);

    ControlContainer panel = new ControlContainer();
    panel.setWidth("");

    setWidget(panel);

    Label label = new Label("Package file:");

    final FileUpload fileUpload = new FileUpload();
    fileUpload.setName("uploadFormElement");

    final Button submit = new Button("Install", new ClickListener() {
      public void onClick(Widget sender) {
        submit();
      }
    });

    addFormHandler(new FormHandler() {
      public void onSubmitComplete(final FormSubmitCompleteEvent event) {
        String result = event.getResults();

        if (result == null || result.length() == 0) {
          return;
        }

        result = extractMessage(result);

        if (result.indexOf(DeployableUnitsService.SUCCESS_CODE) != -1) {
          Logger.info("Deployable unit installed");
        }
        else {
          Logger.error(result);
        }
      }

      public void onSubmit(FormSubmitEvent event) {
        if (fileUpload.getFilename().trim().length() == 0) {
          Logger.error("Please specify a package file");
          event.setCancelled(true);
        }
      }
    });

    panel.setWidget(0, 0, label);
    panel.setWidget(0, 1, fileUpload);
    panel.setWidget(0, 2, submit);

    browseContainer.add("Install a deployable unit", this);
  }
}
