package com.swagger.document.reader.utils;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import io.swagger.annotations.Api;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import io.swagger.models.SecurityRequirement;
import io.swagger.models.Tag;
import io.swagger.models.properties.Property;

public interface MainComponentUtils {

//    static Map<String, Property> getResponseHeaders(ResponseHeader[] headers) {
//        if (isEmpty(headers)) return emptyMap();
//
//        Map<String, Property> responseHeaders = null;
//        for (ResponseHeader header : headers) {
//            if (header.name().isEmpty()) {
//                continue;
//            }
//            if (responseHeaders == null) {
//                responseHeaders = new HashMap<String, Property>();
//            }
//            Class<?> cls = header.response();
//
//            if (!cls.equals(Void.class) && !cls.equals(void.class)) {
//                Property property = ModelConverters.getInstance().readAsProperty(cls);
//                if (property != null) {
//                    Property responseProperty;
//
//                    if (header.responseContainer().equalsIgnoreCase("list")) {
//                        responseProperty = new ArrayProperty(property);
//                    } else if (header.responseContainer().equalsIgnoreCase("map")) {
//                        responseProperty = new MapProperty(property);
//                    } else {
//                        responseProperty = property;
//                    }
//                    responseProperty.setDescription(header.description());
//                    responseHeaders.put(header.name(), responseProperty);
//                }
//            }
//        }
//        return responseHeaders;
//    }
//    
    static Set<Map<String, Object>> getCustomExtensions(Extension[] extensions) {
        if (isEmpty(extensions)) return emptySet();
        
        Set<Map<String, Object>> resultSet = new HashSet<>();
        for (Extension extension : extensions) {
            if (extension == null) {
                continue;
            }
            Map<String, Object> extensionProperties = new HashMap<>();
            for (ExtensionProperty extensionProperty : extension.properties()) {
                String name = extensionProperty.name();
                if (!name.isEmpty()) {
                    extensionProperties.put(name, extensionProperty.value());
                }
            }
            if (!extension.name().isEmpty()) {
                Map<String, Object> wrapper = new HashMap<>();
                wrapper.put(extension.name(), extensionProperties);
                resultSet.add(wrapper);
            } else {
                resultSet.add(extensionProperties);
            }
        }
        return resultSet;
    }

    static List<SecurityRequirement> getSecurityRequirements(Api api) {
        if (api == null || isEmpty(api.authorizations())) return newArrayList();

        return asList(api.authorizations()).stream()
                                           .filter(a -> !a.value().isEmpty())
                                           .map(a -> {
                                               List<String> scopes = asList(a.scopes()).stream()
                                                                                       .filter(s -> !s.scope().isEmpty())
                                                                                       .map(s -> s.scope())
                                                                                       .collect(Collectors.toList());
                                               return new SecurityRequirement().requirement(a.value(), scopes);
                                           })
                                           .collect(Collectors.toList());
    }

    @SuppressWarnings("deprecation")
    static Set<Tag> getTags(Api api) {
        if(api == null || isEmpty(api.tags())) return Sets.newHashSet();

        Set<Tag> tagSet = asList(api.tags()).stream()
                                            .filter(t -> !t.isEmpty())
                                            .map(t -> new Tag().name(t))
                                            .collect(Collectors.toSet());
        if (tagSet.isEmpty()) {
            String tagString = api.value().replace("/", "");
            if (!tagString.isEmpty()) {
                Tag tag = new Tag().name(tagString).description(api.description());
                tagSet.add(tag);
            }
        }
        return tagSet;
    }
}
