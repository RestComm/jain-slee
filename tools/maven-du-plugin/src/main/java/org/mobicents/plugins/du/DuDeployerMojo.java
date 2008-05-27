package org.mobicents.plugins.du;

import java.io.File;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.deployer.ArtifactDeployer;
import org.apache.maven.artifact.deployer.ArtifactDeploymentException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

/**
 * Deploy deployable-unit jar into a remote repository.
 * 
 * @author La Porta
 * @goal deploy
 * @phase deploy
 */
public class DuDeployerMojo
    extends AbstractMojo
{

    private static final String SCORE = new String( "-" );

    private static final String JAR_PREFIX = new String( ".jar" );

    /**
     * The maven project.
     * 
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;

    /**
     * Directory containing the generated JAR.
     * 
     * @parameter expression="${project.build.directory}"
     * @required
     * @readonly
     */
    protected File targetDirectory;

    /**
     * Used to look up Artifacts in the remote repository.
     * 
     * @parameter expression="${component.org.apache.maven.artifact.factory.ArtifactFactory}"
     * @required
     * @readonly
     */
    protected org.apache.maven.artifact.factory.ArtifactFactory factory;

    /**
     * Used to look up Artifacts in the remote repository.
     * 
     * @parameter expression="${component.org.apache.maven.artifact.resolver.ArtifactResolver}"
     * @required
     * @readonly
     */
    protected org.apache.maven.artifact.resolver.ArtifactResolver resolver;

    /**
     * @parameter expression="${project.distributionManagementArtifactRepository}"
     * @readonly
     */
    private org.apache.maven.artifact.repository.ArtifactRepository deploymentRepository;

    /**
     * @parameter expression="${component.org.apache.maven.artifact.deployer.ArtifactDeployer}"
     * @required
     * @readonly
     */
    private ArtifactDeployer deployer;

    /**
     * Default artifact handler.
     * 
     * @parameter expression="${component.org.apache.maven.artifact.handler.ArtifactHandler}"
     * @readonly
     * @required
     */
    org.apache.maven.artifact.handler.ArtifactHandler artifactHandler;

    /**
     * Perform deploy of du jar on remote repository
     */
    private void performDeploy()
    {

        Artifact artifact =
            new DefaultArtifact( project.getGroupId(), project.getArtifactId(),
                                 VersionRange.createFromVersion( project.getVersion() ), Artifact.SCOPE_RUNTIME, "jar",
                                 "", artifactHandler );

        String duJarName = project.getArtifactId() + SCORE + project.getVersion();
        File duFile = new File( targetDirectory, duJarName + JAR_PREFIX );

        try
        {
            deployer.deploy( duFile, artifact, deploymentRepository, deploymentRepository );

        }
        catch ( ArtifactDeploymentException e )
        {

            getLog().error( "Cannot deploy artifact " + artifact, e );
        }
    }

    /**
     * Deploy the DU.
     * 
     * @todo Add license files in META-INF directory.
     */
    public void execute()
        throws MojoExecutionException
    {
        performDeploy();
    }

    public File getTargetDirectory()
    {
        return this.targetDirectory;
    }

}
