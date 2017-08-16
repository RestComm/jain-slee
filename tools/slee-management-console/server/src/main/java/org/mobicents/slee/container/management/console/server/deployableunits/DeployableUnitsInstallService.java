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

package org.mobicents.slee.container.management.console.server.deployableunits;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.mobicents.slee.container.management.console.client.deployableunits.DeployableUnitsService;
import org.mobicents.slee.container.management.console.server.ManagementConsole;
import org.mobicents.slee.container.management.console.server.mbeans.SleeMBeanConnection;

/**
 * @author Stefano Zappaterra
 * 
 */
public class DeployableUnitsInstallService extends HttpServlet {

  private static final long serialVersionUID = 5032346678572052921L;

  private ManagementConsole managementConsole = ManagementConsole.getInstance();
  private SleeMBeanConnection sleeConnection = managementConsole.getSleeConnection();

  public DeployableUnitsInstallService() {
    super();
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/plain");
    PrintWriter out = response.getWriter();
    try {
      DiskFileItemFactory factory = new DiskFileItemFactory();
      ServletFileUpload upload = new ServletFileUpload(factory);

      List items = upload.parseRequest(request);
      Iterator iter = items.iterator();

      while (iter.hasNext()) {
        FileItem item = (FileItem) iter.next();

        if (!item.isFormField()) {
          String fileName = new File(item.getName()).getName();

          File uploadedFile = new File(System.getProperty("jboss.server.temp.dir") + File.separator + fileName);
          item.write(uploadedFile);

          sleeConnection.getSleeManagementMBeanUtils().getDeploymentMBeanUtils().install(uploadedFile.toURL().toString());
          uploadedFile.delete();
        }
      }
      out.print(DeployableUnitsService.SUCCESS_CODE);
    }
    catch (Exception e) {
      e.printStackTrace();
      out.print(e.getMessage());
    }
  }
}
