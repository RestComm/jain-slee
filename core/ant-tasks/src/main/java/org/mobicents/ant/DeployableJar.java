package org.mobicents.ant;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.ZipFileSet;
import org.mobicents.util.XMLParser;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

//
// The DeployableJar task automates the construction of a deployable unit jar that
// may subsequently deployed in a SLEE. Nested elements of the DeployableJar are the
// consituent elements in the deployable unit. DeployableJar generates a appropriate
// deployable-unit.xml descriptor.
//
// The metainfbase attribute is optional. It contains a path that is preprended to
// the location of all deployment descriptors in the deployable unit - if present the value
// of this attribute is passed to the nested elements.
//
// The extxml attribute is optional. It contains the name of an oc service extension
// deployment descriptor. The name is relative to metainfbase, or the current directory
// if metainfbase is not defined. The file is added as META-INF/oc-service.xml into
// the deployable unit.
//
// <deployableunit destfile="pathname.jar" metainfbase="foo/bar" servicexml="my-service.xml">
//   <component file="foo.jar">
//   <component refid="bar">
//   <sbbjar ...>
//   <eventjar ...>
//   <profilespecjar ...>
//   <resourceadaptortypejar ...>
//   <resourceadaptorjar ...>
//   <fileset ...>
// </deployableunit>
//
// Here is an example from the jcc call forwarding example app:
//
// <deployablejar destfile="${jars}/call-forwarding.jar" metainfbase="${src}/com/opencloud/slee/services/callforwarding/META-INF">
//   <profilespecjar destfile="${jars}/profile.jar" classpath="${classes}/jcc-callforwarding" />
//   <sbbjar destfile="${jars}/sbb.jar" classpath="${classes}/jcc-callforwarding" />
// </deployablejar>

public class DeployableJar extends org.apache.tools.ant.taskdefs.Jar {
    public DeployableJar() {
        super();
        archiveType = "deployable-unit-jar";
        emptyBehavior = "create";

	this.serviceXmlStr = "service.xml";
    }

    public void setMetainfbase(String metainfbase) {
        this.metainfbase = new File(metainfbase);
    }

    public void setServicexml(String serviceXmlStr) {
        this.serviceXmlStr = serviceXmlStr;
    }

    public final void setExtxml(String extXmlStr) {
        this.extXmlStr = extXmlStr;
    }

    public void execute() throws BuildException {

        processServiceXml();

        // Execute subtasks as necessary.
        for (Iterator i = tasks.iterator(); i.hasNext(); ) {
            Task task = (Task)i.next();
            if (task instanceof SleeJar) ((SleeJar)task).setMetaInfBase(metainfbase);
            task.perform();
        }

        if (deployableunitxml == null) {
            // Build our deployable-unit.xml.

            try {
                // ... build a DOM tree and serialize it out.
                DocumentBuilder builder = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder();
                DOMImplementation domImpl = builder.getDOMImplementation();
                DocumentType docType = domImpl.createDocumentType("deployable-unit", "-//Sun Microsystems, Inc.//DTD JAIN SLEE Deployable Unit 1.0//EN", "http://java.sun.com/dtd/slee-deployable-unit_1_0.dtd");
                Document doc = domImpl.createDocument(null, docType.getName(), docType);
                Element root = doc.getDocumentElement();

                // Add <jar> elements
                for (Iterator i = components.iterator(); i.hasNext(); ) {
                    Component component = (Component)i.next();
                    File componentFile = component.getComponentFile(getProject());

                    // Add a fileset for this component.
                    ZipFileSet fs = new ZipFileSet();
                    fs.setFile(componentFile);
                    fs.setFullpath(componentFile.getName());
                    super.addFileset(fs);

                    XMLParser.createTextElement(root, "jar", componentFile.getName());
                }

                if (servicexml != null) {
		    for (int i = 0; i < servicexml.length; i++) {
			File serviceXmlFile = (File) servicexml[i];
			XMLParser.createTextElement(root, "service-xml", serviceXmlFile.getName());
		    }

                    // Add <service-xml> to the output doc.
		    // @@                    XMLParser.createTextElement(root, "service-xml", "service-jar.xml");
                }

                // Create the deployable-unit.xml.
                deployableunitxml = File.createTempFile("deployable-unit", ".xml");
                deployableunitxml.deleteOnExit();

                FileOutputStream duDDOut = new FileOutputStream(deployableunitxml);
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer identityTransformer = factory.newTransformer();

                identityTransformer.setOutputProperty(OutputKeys.METHOD, "xml");
                identityTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
                identityTransformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, docType.getPublicId());
                identityTransformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, docType.getSystemId());
                identityTransformer.transform(new DOMSource(doc), new StreamResult(duDDOut));

                duDDOut.close();

                // Add a new fileset for deployable-unit.xml.
                ZipFileSet fs = new ZipFileSet();
                fs.setFile(deployableunitxml);
                fs.setFullpath("META-INF/deployable-unit.xml");
                super.addFileset(fs);
            } catch (Exception e) {
                throw new BuildException(e);
            }
        }

        super.execute();
    }

    //  servicexml="..."
    private void processServiceXml() {
	StringTokenizer tok = new StringTokenizer(serviceXmlStr);

	File [] files = new File[tok.countTokens()];
	int i = 0;
	while (tok.hasMoreElements()) {
            files[i++] = (null == metainfbase) ? new File(tok.nextToken()) : new File(metainfbase, tok.nextToken());
	}

        setServicexml(files);
    }

    private void setServicexml(File [] servicexml) {
        this.servicexml = servicexml;

	for (int i = 0; i < servicexml.length; i++) {
	    if (!servicexml[i].exists()) {
		throw new BuildException("Service deployment descriptor: "
					 + servicexml[i]
					 + " does not exist.");
	    }
	}

	for (int i = 0; i < servicexml.length; i++) {
	    ZipFileSet fs = new ZipFileSet();
	    fs.setFile(servicexml[i]);
	    fs.setFullpath(servicexml[i].getName());
	    super.addFileset(fs);
	}


        // look for oc-service.xml extension dd
        if (extXmlStr != null) {
            File extXml = (metainfbase == null) ? new File(extXmlStr) : new File(metainfbase, extXmlStr);
	    if (!extXml.exists()) {
		throw new BuildException("Service extension deployment descriptor: "
					 + extXml
					 + " does not exist.");
	    }
            ZipFileSet fs = new ZipFileSet();
            fs.setFile(extXml);
            fs.setFullpath("META-INF/oc-service.xml");
            super.addFileset(fs);
        }
    }

    // Nested <component ...>
    public Component createComponent() {
        SimpleComponent newComponent = new SimpleComponent();
        components.add(newComponent);
        return newComponent;
    }

    // Nested <sbbjar ...>
    public SbbJar createSbbjar() {
        SbbJar sbbTask = new SbbJar();
        sbbTask.setGeneratename(true);
        components.add(sbbTask);
        tasks.add(sbbTask);
        return sbbTask;
    }

    public EventJar createEventJar() {
	EventJar eventTask = new EventJar();
	eventTask.setGeneratename(true);
        components.add(eventTask);
        tasks.add(eventTask);
        return eventTask;
    }

    public ProfileSpecJar createProfileSpecJar() {
	ProfileSpecJar profileSpecTask = new ProfileSpecJar();
	profileSpecTask.setGeneratename(true);
        components.add(profileSpecTask);
        tasks.add(profileSpecTask);
        return profileSpecTask;
    }

    public ResourceAdaptorTypeJar createResourceAdaptorTypeJar() {
	ResourceAdaptorTypeJar raTypeTask = new ResourceAdaptorTypeJar();
	raTypeTask.setGeneratename(true);
	components.add(raTypeTask);
	tasks.add(raTypeTask);
	return raTypeTask;
    }

    public ResourceAdaptorJar createResourceAdaptorJar() {
	ResourceAdaptorJar raTask = new ResourceAdaptorJar();
	raTask.setGeneratename(true);
	components.add(raTask);
	tasks.add(raTask);
	return raTask;
    }

    protected void cleanUp() {
        // Delete temporary deployable-unit.xml
        if (deployableunitxml != null) {
            deployableunitxml.delete();
            deployableunitxml = null;
        }
    }

    // SimpleComponent:
    //   <component file="location"/>
    //   <component refid="id"/>
    public static final class SimpleComponent implements Component {
        public SimpleComponent() {}

        public File getComponentFile(Project project) throws BuildException {
            if (file != null)
                return file;

            if (ref != null) {
                try {
                    Component referenced = (Component)ref.getReferencedObject(project);
                    return referenced.getComponentFile(project);
                } catch (ClassCastException cce) {
                    throw new BuildException("Referenced task is not a Component task");
                }
            }

            throw new BuildException("One of ref or file attribute must be specified");
        }

        public void setFile(File file) { this.file = file; }
        public void setRefid(Reference ref) { this.ref = ref; }

        private File file;
        private Reference ref;
    }

    private File metainfbase = null;
    private String serviceXmlStr;
    private String extXmlStr;
    private File deployableunitxml;
    private File [] servicexml;
    private final LinkedList components = new LinkedList();
    private final LinkedList tasks = new LinkedList();
}
