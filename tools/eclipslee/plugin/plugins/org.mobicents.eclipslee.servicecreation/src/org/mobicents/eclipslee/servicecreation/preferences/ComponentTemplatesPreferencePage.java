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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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

  @Override
  public void init(IWorkbench workbench) {
    // Initialize the preference store we wish to use
    setPreferenceStore(ServiceCreationPlugin.getDefault().getPreferenceStore());
    setDescription("In this page the available component templates can be defined.");
  }

  @Override
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

    new Label(composite, SWT.NONE);
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


    componentsTable.addSelectionListener(new SelectionListener() {
      @Override
      public void widgetSelected(SelectionEvent arg0) {
        removeButton.setEnabled(true);
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent arg0) {
      }
    });

    //    shell.open();
    //    while(!shell.isDisposed()) {
    //      if(!shell.getDisplay().readAndDispatch()) shell.getDisplay().sleep();
    //    }
    //    shell.getDisplay().dispose();

    //    Group enablersGroup = new Group(composite, SWT.SHADOW_ETCHED_IN | SWT.FULL_SELECTION);
    //    enablersGroup.setText("Add Component");

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
      @Override
      public void widgetSelected(SelectionEvent arg0) {
        setErrorMessage(null);
        if(!checkComponentAlreadyExists()) {
          checkEnableAddButton();
        }
        else {
          addButton.setEnabled(false);
          setErrorMessage("This component is already present in the templates list");
        }
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent arg0) {
        // TODO Auto-generated method stub
      }
    });

    ModifyListener textListener = new ModifyListener() {
      @Override
      public void modifyText(ModifyEvent arg0) {
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

      @Override
      public void widgetSelected(SelectionEvent arg0) {
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

      @Override
      public void widgetDefaultSelected(SelectionEvent arg0) {
        // TODO Auto-generated method stub
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

  @Override
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
            //              @Override
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
