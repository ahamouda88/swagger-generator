package com.groupon.swagger.generator;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.groupon.swagger.common.BaseMavenPluginTest;
import com.groupon.swagger.test.files.DocumentReaderSwaggerFile1;
import com.groupon.swagger.test.files.DocumentReaderSwaggerFile2;
import com.swagger.document.generator.DocumentGenerator;

import io.swagger.annotations.Api;
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
        Swagger updatedSwagger = setupDocumentReaderFile(DocumentReaderSwaggerFile1.class);

        List<Tag> tags = updatedSwagger.getTags();
        assertEquals("tag1", tags.get(0).getName());
        assertEquals("tag2", tags.get(1).getName());
        assertEquals("jsonCon", updatedSwagger.getConsumes().get(0));
        assertEquals("jsonPro", updatedSwagger.getProduces().get(0));
        assertEquals(2, updatedSwagger.getSchemes().size());
        assertEquals(Scheme.HTTPS, updatedSwagger.getSchemes().get(0));
        assertEquals(Scheme.WS, updatedSwagger.getSchemes().get(1));
    }

    @Test
    public void testMergedSwaggerComponents() {
        setupDocumentReaderFile(DocumentReaderSwaggerFile1.class);
        Swagger updatedSwagger = setupDocumentReaderFile(DocumentReaderSwaggerFile2.class);

        assertEquals(1, updatedSwagger.getConsumes().size());
        assertEquals("jsonCon", updatedSwagger.getConsumes().get(0));
        assertEquals(2, updatedSwagger.getProduces().size());
        assertEquals("jsonPro", updatedSwagger.getProduces().get(0));
        assertEquals("xmlPro", updatedSwagger.getProduces().get(1));
        assertEquals(2, updatedSwagger.getSchemes().size());
        assertEquals(Scheme.HTTPS, updatedSwagger.getSchemes().get(0));
        assertEquals(Scheme.WS, updatedSwagger.getSchemes().get(1));
        assertEquals(Sets.newHashSet("rest_auth"), updatedSwagger.getSecurity().get(0).getRequirements().keySet());
        assertEquals("write:scope", updatedSwagger.getSecurity().get(0).getRequirements().get("rest_auth").get(0));
        assertEquals("read:scope", updatedSwagger.getSecurity().get(0).getRequirements().get("rest_auth").get(1));
    }

    private Swagger setupDocumentReaderFile(Class<?> clazz) {
//        Api api = clazz.getAnnotation(Api.class);
//        documentReader.setTags(api);
//        documentReader.setConsumes(api);
//        documentReader.setProduces(api);
//        documentReader.setScheme(api);
//        documentReader.setSecurityRequirements(api);

        return documentReader.getSwagger();
    }
}
