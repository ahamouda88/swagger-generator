package com.swagger.document.utils;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

import org.springframework.core.annotation.AnnotationUtils;

import io.reactivex.Observable;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.models.Contact;
import io.swagger.models.Info;
import io.swagger.models.License;

public class SwaggerMainUtils {

    public static Info getInfoFromAnnotation() {
        Info resultInfo = new Info();
        getSwaggerDefinitionObs().map(swaggerDefinition -> getInfoFromAnnotation(swaggerDefinition.info()))
                                 .onErrorResumeNext(Observable.empty())
                                 .subscribe(info -> resultInfo.mergeWith(info));
        return resultInfo;
    }

    public static String getBasePathFromAnnotation() {
        return getSwaggerDefinitionObs().map(swaggerDefinition -> swaggerDefinition.basePath())
                                        .blockingFirst(null);
    }

    public static String getHostFromAnnotation() {
        return getSwaggerDefinitionObs().map(swaggerDefinition -> swaggerDefinition.host())
                                        .blockingFirst(null);
    }

    private static Info getInfoFromAnnotation(io.swagger.annotations.Info infoAnnotation) {
        return new Info().title(infoAnnotation.title())
                         .description(emptyToNull(infoAnnotation.description()))
                         .version(infoAnnotation.version())
                         .termsOfService(emptyToNull(infoAnnotation.termsOfService()))
                         .license(getLicenseFromAnnotation(infoAnnotation.license()))
                         .contact(getContactFromAnnotation(infoAnnotation.contact()));
    }

    private static Contact getContactFromAnnotation(io.swagger.annotations.Contact contactAnnotation) {
        Contact contact = new Contact().name(emptyToNull(contactAnnotation.name()))
                                       .email(emptyToNull(contactAnnotation.email()))
                                       .url(emptyToNull(contactAnnotation.url()));
        if (contact.getName() == null && contact.getEmail() == null && contact.getUrl() == null) {
            contact = null;
        }
        return contact;
    }

    private static License getLicenseFromAnnotation(io.swagger.annotations.License licenseAnnotation) {
        License license = new License().name(emptyToNull(licenseAnnotation.name()))
                                       .url(emptyToNull(licenseAnnotation.url()));
        if (license.getName() == null && license.getUrl() == null) {
            license = null;
        }
        return license;
    }

    private static Observable<SwaggerDefinition> getSwaggerDefinitionObs() {
        return Observable.fromIterable(ReflectionUtils.getValidClasses(SwaggerDefinition.class))
                         .map(clazz -> AnnotationUtils.findAnnotation(clazz,
                                                                      SwaggerDefinition.class));
    }

    private static String emptyToNull(String str) {
        return defaultIfBlank(str, null);
    }
}
