package com.groupon.swagger.generator;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.groupon.swagger.common.BaseMavenPluginTest;
import com.swagger.document.generator.DocumentGenerator;

import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;

public class DocumentGeneratorTest extends BaseMavenPluginTest {

    private DocumentGenerator documentReader;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        documentReader = new DocumentGenerator(mojo.getApiSources().get(0));
    }

    @Test
    public void testSwaggerComponents() {
        Swagger updatedSwagger = documentReader.getSwagger();

        assertEquals("tag1", updatedSwagger.getBasePath());
        List<Tag> tags = updatedSwagger.getTags();
        assertEquals("tag1", tags.get(0).getName());
        assertEquals("tag2", tags.get(1).getName());
        assertEquals("jsonCon", updatedSwagger.getConsumes().get(0));
        assertEquals("jsonPro", updatedSwagger.getProduces().get(0));
        assertEquals(2, updatedSwagger.getSchemes().size());
        assertEquals(Scheme.HTTPS, updatedSwagger.getSchemes().get(0));
        assertEquals(Scheme.WS, updatedSwagger.getSchemes().get(1));
    }

//    @Test
//    public void testMergedSwaggerComponents() {
//        Swagger updatedSwagger = documentReader.getSwagger();
//
//        assertEquals(1, updatedSwagger.getConsumes().size());
//        assertEquals("jsonCon", updatedSwagger.getConsumes().get(0));
//        assertEquals(2, updatedSwagger.getProduces().size());
//        assertEquals("jsonPro", updatedSwagger.getProduces().get(0));
//        assertEquals("xmlPro", updatedSwagger.getProduces().get(1));
//        assertEquals(2, updatedSwagger.getSchemes().size());
//        assertEquals(Scheme.HTTPS, updatedSwagger.getSchemes().get(0));
//        assertEquals(Scheme.WS, updatedSwagger.getSchemes().get(1));
//        assertEquals(Sets.newHashSet("rest_auth"), updatedSwagger.getSecurity().get(0).getRequirements().keySet());
//        assertEquals("write:scope", updatedSwagger.getSecurity().get(0).getRequirements().get("rest_auth").get(0));
//        assertEquals("read:scope", updatedSwagger.getSecurity().get(0).getRequirements().get("rest_auth").get(1));
//    }
}
