package com.github.trecloux.yeoman;

import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingRequest;
import org.mockito.ArgumentCaptor;

import java.io.File;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

public class YeomanMojoTest extends AbstractMojoTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void test_should_run_all_commands() throws Exception {
        MavenProject project = getMavenProject("src/test/resources/test-mojo-pom.xml");

        YeomanMojo yeomanMojo = (YeomanMojo) lookupConfiguredMojo(project, "build");

        YeomanMojo spy = spy(yeomanMojo);
        ArgumentCaptor<String> commandsCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(spy).executeCommand(commandsCaptor.capture());

        spy.execute();

        assertThat(commandsCaptor.getAllValues()).containsExactly(
                "node --version",
                "npm --version",
                "npm install",
                "bower --version",
                "bower install --no-color",
                "grunt --version",
                "grunt test --no-color",
                "grunt build --no-color"
        );
    }

    private MavenProject getMavenProject(String pomPath) throws Exception {
        File pom = new File(pomPath);
        MavenExecutionRequest request = new DefaultMavenExecutionRequest();
        request.setPom(pom);
        ProjectBuildingRequest configuration = request.getProjectBuildingRequest();
        return lookup( ProjectBuilder.class ).build( pom, configuration ).getProject();
    }
}
