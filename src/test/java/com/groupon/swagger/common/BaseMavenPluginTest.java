package com.groupon.swagger.common;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.After;
import org.junit.Before;

import com.swagger.document.mavenplugin.ApiDocumentMojo;

public abstract class BaseMavenPluginTest extends AbstractMojoTestCase {
    protected static final String PLUGIN_CONFIG_PATH = "src/test/resources/plugin/plugin-config.xml";

    protected ApiDocumentMojo mojo;

    @Override
    @Before
    protected void setUp() throws Exception {
        super.setUp();

        final File testPom = new File(getBasedir(), PLUGIN_CONFIG_PATH);
        mojo = (ApiDocumentMojo) lookupMojo("generate", testPom);
    }

    @Override
    @After
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
