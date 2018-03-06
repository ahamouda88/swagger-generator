package com.groupon.swagger.test.files;

import io.swagger.annotations.Api;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;


@Api(consumes = "jsonCon", produces = "xmlPro", protocols = "ws",
                authorizations = {@Authorization(value = "rest_auth",
                                scopes = {@AuthorizationScope(scope = "write:scope", description = "Write permission"),
                                          @AuthorizationScope(scope = "read:scope", description = "Read permission")
                                })})
public class DocumentReaderSwaggerFile2 {

}
