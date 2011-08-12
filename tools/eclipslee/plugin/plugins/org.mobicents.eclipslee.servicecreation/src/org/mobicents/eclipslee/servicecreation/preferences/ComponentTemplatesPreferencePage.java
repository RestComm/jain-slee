/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.eclipslee.servicecreation.preferences;

import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.mobicents.eclipslee.servicecreation.ServiceCreationPlugin;
import org.mobicents.eclipslee.servicecreation.preferences.ComponentEntry.ComponentType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * EclipSLEE Component Templates Preference Page class
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ComponentTemplatesPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

  //final HashMap<String, ComponentEntry> enablersComponents = new HashMap<String, ComponentEntry>();
  //final HashMap<String, ComponentEntry> ratypesComponents = new HashMap<String, ComponentEntry>();
  //final HashMap<String, ComponentEntry> librariesComponents = new HashMap<String, ComponentEntry>();

  final ArrayList<ComponentEntry> enablersComponents = new ArrayList<ComponentEntry>();
  final ArrayList<ComponentEntry> ratypesComponents = new ArrayList<ComponentEntry>();
  final ArrayList<ComponentEntry> librariesComponents = new ArrayList<ComponentEntry>();

  Text groupIdText;
  Text artifactIdText;
  Text versionText;
  Text descriptionText;

  Button addButton;
  Combo typeCombo;

  public void init(IWorkbench workbench) {
    // Initialize the preference store we wish to use
    setPreferenceStore(ServiceCreationPlugin.getDefault().getPreferenceStore());
    setDescription("In this page the available component templates can be defined.");
  }

  protected Control createContents(Composite parent) {

    Shell shell = parent.getShell();

    Composite composite = new Composite(parent, SWT.NULL);

    //Create a data that takes up the extra space in the dialog .
    GridData data = new GridData(GridData.FILL_HORIZONTAL);
    data.grabExcessHorizontalSpace = true;
    composite.setLayoutData(data);

    GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    composite.setLayout(layout);     

    final Table componentsTable = new Table(composite, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
    componentsTable.setLinesVisible(false);
    componentsTable.setHeaderVisible(true);

    GridData tableData = new GridData(SWT.FILL, SWT.FILL, true, true);
    tableData.heightHint = 150;
    tableData.horizontalSpan = 2;
    componentsTable.setLayoutData(tableData);

    String[] titles = {"Type", "Group ID", "Artifact Id", "Version", "Description"};
    for (int i=0; i<titles.length; i++) {
      TableColumn column = new TableColumn(componentsTable, SWT.NONE);
      column.setText(titles [i]);
    }

    parseDependenciesFile();

    addComponentsToTable(enablersComponents, componentsTable);
    addComponentsToTable(ratypesComponents, componentsTable);
    addComponentsToTable(librariesComponents, componentsTable);

    for (int i=0; i<titles.length; i++) {
      componentsTable.getColumn(i).pack();
    } 
    componentsTable.pack();

    Label noteLabel = new Label(composite, SWT.NONE);
    noteLabel.setText("Note: Double-click an item to edit it's version.");

    final Button removeButton = new Button(composite, SWT.NONE);
    removeButton.setText("Remove Component");
    removeButton.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, true));
    removeButton.setEnabled(false);

    removeButton.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        removeButton.setEnabled(false);
        TableItem item = componentsTable.getItem(componentsTable.getSelectionIndex());

        ComponentEntry.ComponentType type = ComponentEntry.ComponentType.valueOf(item.getText(0));
        String groupId = item.getText(1);
        String artifactId = item.getText(2);
        String version = item.getText(3);
        String description = item.getText(4);

        ComponentEntry ce = new ComponentEntry(type, groupId, artifactId, version, description);
        
        switch(ce.getType()) {
          case ENABLER:
            enablersComponents.remove(ce);
            break;
          case RATYPE:
            ratypesComponents.remove(ce);
            break;
          case LIBRARY:
            librariesComponents.remove(ce);
            break;
        }
        
        componentsTable.remove(componentsTable.getSelectionIndex());
      }
    });

    componentsTable.addMouseListener(new MouseListener() {
      
      public void mouseUp(MouseEvent me) {
        // NOP
      }
      
      public void mouseDown(MouseEvent me) {
        // NOP
      }
      
      public void mouseDoubleClick(MouseEvent me) {
        Table table = ((Table)me.getSource());
        TableItem item = table.getSelection()[0];

        final TableEditor editor = new TableEditor(table);
        // The editor must have the same size as the cell and must
        // not be any smaller than 50 pixels.
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;
        editor.minimumWidth = 50;
        // editing the second column
        final int EDITABLECOLUMN = 3;
        final String oldValue = item.getText(EDITABLECOLUMN);

        // The control that will be the editor must be a child of the
        // Table
        Text newEditor = new Text(table, SWT.NONE);
        newEditor.setText(item.getText(EDITABLECOLUMN));
        newEditor.addModifyListener(new ModifyListener() {
          public void modifyText(ModifyEvent me) {
            Text text = (Text) editor.getEditor();
            editor.getItem().setText(EDITABLECOLUMN, text.getText());
          }
        });
        newEditor.selectAll();
        newEditor.setFocus();
        editor.setEditor(newEditor, item, EDITABLECOLUMN);
        
        newEditor.addFocusListener(new FocusListener() {
          
          public void focusLost(FocusEvent fe) {
            Text text = (Text) fe.getSource();
            text.dispose();
            
            TableItem item = editor.getItem();

            ComponentEntry.ComponentType type = ComponentEntry.ComponentType.valueOf(item.getText(0));
            String groupId = item.getText(1);
            String artifactId = item.getText(2);
            String version = item.getText(3);
            String description = item.getText(4);

            ComponentEntry old = new ComponentEntry(type, groupId, artifactId, oldValue, description);
            ComponentEntry neew = new ComponentEntry(type, groupId, artifactId, version, description);
            
            switch(old.getType()) {
              case ENABLER:
                enablersComponents.set(enablersComponents.indexOf(old), neew);
                break;
              case RATYPE:
                ratypesComponents.set(ratypesComponents.indexOf(old), neew);
                break;
              case LIBRARY:
                librariesComponents.set(librariesComponents.indexOf(old), neew);
                break;
            }            
          }
          
          public void focusGained(FocusEvent fe) {
            // NOP
          }
        });
      }
    });

    componentsTable.addSelectionListener(new SelectionListener() {
      public void widgetSelected(SelectionEvent se) {
        removeButton.setEnabled(true);
      }

      public void widgetDefaultSelected(SelectionEvent se) {
      }
    });

    // empty line
    new Label(composite, SWT.NONE);
    new Label(composite, SWT.NONE);

    Label addLabel = new Label(composite, SWT.NONE);
    addLabel.setText("Add Component Template:");
    GridData labelData = new GridData(SWT.LEFT, SWT.FILL, true, true);
    labelData.horizontalSpan = 2;
    addLabel.setLayoutData(labelData);

    GridData textData = new GridData(SWT.LEFT, SWT.FILL, true, true);
    textData.widthHint = 222;

    Label typeLabel = new Label(composite, SWT.NONE);
    typeLabel.setText("Component Type *");
    typeCombo = new Combo(composite, SWT.READ_ONLY);
    typeCombo.setItems(new String [] {"Enabler", "Resource Adaptor Type", "Library"});
    typeCombo.select(0);
    typeCombo.setLayoutData(textData);

    Label groupIdLabel = new Label(composite, SWT.NONE);
    groupIdLabel.setText("Group ID *");
    groupIdText = new Text(composite, SWT.SINGLE | SWT.BORDER);
    groupIdText.setLayoutData(textData);
    Label artifactIdLabel = new Label(composite, SWT.NONE);
    artifactIdLabel.setText("Artifact ID *");
    artifactIdText = new Text(composite, SWT.SINGLE | SWT.BORDER);
    artifactIdText.setLayoutData(textData);
    Label versionLabel = new Label(composite, SWT.NONE);
    versionLabel.setText("Version *");
    versionText = new Text(composite, SWT.SINGLE | SWT.BORDER);
    versionText.setLayoutData(textData);
    Label descriptionLabel = new Label(composite, SWT.NONE);
    descriptionLabel.setText("Description");
    descriptionText = new Text(composite, SWT.SINGLE | SWT.BORDER);
    descriptionText.setLayoutData(textData);

    addButton = new Button(composite, SWT.NONE);
    addButton.setText("Add Component");
    addButton.setEnabled(false);

    typeCombo.addSelectionListener(new SelectionListener() {
      public void widgetSelected(SelectionEvent se) {
        setErrorMessage(null);
        if(!checkComponentAlreadyExists()) {
          checkEnableAddButton();
        }
        else {
          addButton.setEnabled(false);
          setErrorMessage("This component is already present in the templates list");
        }
      }

      public void widgetDefaultSelected(SelectionEvent se) {
        // TODO Auto-generated method stub
      }
    });

    ModifyListener textListener = new ModifyListener() {
      public void modifyText(ModifyEvent me) {
        setErrorMessage(null);
        if(!checkComponentAlreadyExists()) {
          checkEnableAddButton();
        }
        else {
          addButton.setEnabled(false);
          setErrorMessage("This component is already present in the templates list");
        }
      }
    };

    groupIdText.addModifyListener(textListener);
    artifactIdText.addModifyListener(textListener);
    versionText.addModifyListener(textListener);

    addButton.addSelectionListener(new SelectionListener() {

      public void widgetSelected(SelectionEvent se) {
        ComponentEntry ce = getCurrentNewComponentEntry();
        TableItem item = new TableItem(componentsTable, SWT.NONE);
        item.setText(0, ce.getType().toString());
        item.setText(1, ce.getGroupId());
        item.setText(2, ce.getArtifactId());
        item.setText(3, ce.getVersion());
        item.setText(4, ce.getDescription() == null ? "" : ce.getDescription());

        groupIdText.setText("");
        artifactIdText.setText("");
        versionText.setText("");
        descriptionText.setText("");

        componentsTable.select(componentsTable.getItemCount()-1);
        componentsTable.showSelection();
        componentsTable.forceFocus();
        
        // Add to proper arraylist
        switch(ce.getType()) {
          case ENABLER:
            enablersComponents.add(ce);
            break;
          case RATYPE:
            ratypesComponents.add(ce);
            break;
          case LIBRARY:
            librariesComponents.add(ce);
            break;
        }
      }

      public void widgetDefaultSelected(SelectionEvent se) {
        // NOP
      }
    });

    shell.pack();

    return parent;
  }

  private void addComponentsToTable(ArrayList<ComponentEntry> components, Table table) {
    for (ComponentEntry comp : components) {
      TableItem item = new TableItem(table, SWT.NONE);
      item.setText(0, comp.getType().toString());
      item.setText(1, comp.getGroupId());
      item.setText(2, comp.getArtifactId());
      item.setText(3, comp.getVersion());
      item.setText(4, comp.getDescription() == null ? "" : comp.getDescription());
    }
  }
  
  private ComponentEntry getCurrentNewComponentEntry() {
    ComponentEntry.ComponentType type = ComponentEntry.ComponentType.fromString(typeCombo.getItem(typeCombo.getSelectionIndex()));

    return new ComponentEntry(type, groupIdText.getText(), artifactIdText.getText(), versionText.getText(), descriptionText.getText());
  }

  private boolean checkComponentAlreadyExists() {
    ComponentEntry ce = getCurrentNewComponentEntry();

    return enablersComponents.contains(ce) || ratypesComponents.contains(ce) || librariesComponents.contains(ce);
  }

  private void checkEnableAddButton() {
    if(!groupIdText.getText().equals("") && !artifactIdText.getText().equals("") && !versionText.getText().equals("") && typeCombo.getSelectionIndex() != -1) {
      addButton.setEnabled(true);
    }
    else {
      addButton.setEnabled(false);
    }
  }

  public boolean performOk() {
    writeDependenciesFile();
    return true;
  }

  private void parseDependenciesFile() {
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      URL url = FileLocator.find(ServiceCreationPlugin.getDefault().getBundle(), new Path("/eclipslee-maven-dependencies.xml"), null);
      URL furl = FileLocator.toFileURL(url);
      Document doc = db.parse(furl.openStream());
      // TODO: Get component
      NodeList compNodeList = doc.getDocumentElement().getElementsByTagName("sbb");
      if(compNodeList.getLength() != 1) {
        // fail
      }
      else {
        NodeList elemChilds = compNodeList.item(0).getChildNodes();

        // Component types
        for(int i = 0; i < elemChilds.getLength(); i++) {
          if(elemChilds.item(i) instanceof Element) {
            Element e = (Element) elemChilds.item(i);

            String componentType = e.getNodeName();

            //            // Add Listener to selection change
            //            componentTemplatesCombo.addModifyListener(new ModifyListener() {
            //              public void modifyText(ModifyEvent event) {
            //                String selected = componentTemplatesCombo.getItem(componentTemplatesCombo.getSelectionIndex());
            //                if(selected.startsWith("-------- ")) {
            //                  componentTemplatesCombo.pack();
            //                }
            //                else {
            //                  String[] values = descToIds.get(componentTemplatesCombo.getItem(componentTemplatesCombo.getSelectionIndex()));
            //                  depGroupId.setText(values[0]);
            //                  depArtifactId.setText(values[1]);
            //                  depVersion.setText(values[2]);
            //                }
            //              }
            //            });

            // Now we get to dependencies
            NodeList dependencies = e.getChildNodes();

            if(dependencies.getLength() == 0) {
              // skip. no need to fail/alert, thought
            }
            else {
              for(int j = 0; j < dependencies.getLength(); j++) {
                if(dependencies.item(j) instanceof Element) {
                  Element depElem = (Element) dependencies.item(j);

                  String depGroupId = depElem.getElementsByTagName("groupId").item(0).getTextContent();
                  String depArtifactId = depElem.getElementsByTagName("artifactId").item(0).getTextContent();
                  String depVersion = depElem.getElementsByTagName("version").item(0).getTextContent();
                  String depDesc = depElem.getElementsByTagName("description").getLength() > 0 ? depElem.getElementsByTagName("description").item(0).getTextContent() : "";

                  //String depKey = depGroupId + " : " + depArtifactId + " : " + depVersion;

                  switch (ComponentType.fromString(componentType)) {
                    case ENABLER:
                      enablersComponents.add(new ComponentEntry(ComponentType.fromString(componentType), depGroupId, depArtifactId, depVersion, depDesc));
                      break;
                    case RATYPE:
                      ratypesComponents.add(new ComponentEntry(ComponentType.fromString(componentType), depGroupId, depArtifactId, depVersion, depDesc));
                      break;
                    case LIBRARY:
                      librariesComponents.add(new ComponentEntry(ComponentType.fromString(componentType), depGroupId, depArtifactId, depVersion, depDesc));
                      break;
                    default:
                      // meh
                  }
                }
              }
            }
          }
        }

      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void writeDependenciesFile() {
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();

      Document doc = db.newDocument();
      Element root = doc.createElement("eclipslee-maven-dependencies");
      doc.appendChild(root);
      Element sbbRoot = doc.createElement("sbb");
      root.appendChild(sbbRoot);

      // Add root enablers entry
      Element sbbEnablersRoot = doc.createElement("enablers");
      sbbEnablersRoot.setAttribute("description", "Enablers");
      sbbRoot.appendChild(sbbEnablersRoot);

      addComponentsToXml(enablersComponents, sbbEnablersRoot);

      // Add root enablers entry
      Element sbbRATypesRoot = doc.createElement("resource-adaptor-types");
      sbbRATypesRoot.setAttribute("description", "Resource Adaptor Types");
      sbbRoot.appendChild(sbbRATypesRoot);

      addComponentsToXml(ratypesComponents, sbbRATypesRoot);

      // Add root enablers entry
      Element sbbLibrariesRoot = doc.createElement("libraries");
      sbbLibrariesRoot.setAttribute("description", "JAIN SLEE Libraries");
      sbbRoot.appendChild(sbbLibrariesRoot);

      addComponentsToXml(librariesComponents, sbbLibrariesRoot);

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      DOMSource source = new DOMSource(doc);

      URL url = FileLocator.find(ServiceCreationPlugin.getDefault().getBundle(), new Path("/eclipslee-maven-dependencies.xml"), null);
      URL furl = FileLocator.toFileURL(url);

      StreamResult result =  new StreamResult(new FileOutputStream(furl.getPath()));
      //      StreamResult result =  new StreamResult(System.out);
      transformer.transform(source, result);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void addComponentsToXml(ArrayList<ComponentEntry> components, Element root) {
    Document doc = root.getOwnerDocument();

    for(ComponentEntry ce : components) {
      Element dependency = doc.createElement("dependency");
      root.appendChild(dependency);
      if(ce.getDescription() != null) {
        Element description = doc.createElement("description");
        description.setTextContent(ce.getDescription());
        dependency.appendChild(description);
      }
      Element groupId = doc.createElement("groupId");
      groupId.setTextContent(ce.getGroupId());
      dependency.appendChild(groupId);

      Element artifactId = doc.createElement("artifactId");
      artifactId.setTextContent(ce.getArtifactId());
      dependency.appendChild(artifactId);

      Element version = doc.createElement("version");
      version.setTextContent(ce.getVersion());
      dependency.appendChild(version);
    }
  }

}
