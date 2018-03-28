package com.groupon.swagger.test.files;

import java.io.InputStream;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "/pet", authorizations = {
                @Authorization(value = "petstore_auth",
                                scopes = {
                                                @AuthorizationScope(scope = "write:pets",
                                                                description = "modify pets in your account"),
                                                @AuthorizationScope(scope = "read:pets",
                                                                description = "read your pets")
                                })
}, tags = "pet")
public class PetResource {

    @ApiOperation(value = "Find pet by ID", notes = "Returns a single pet", response = Pet.class,
                    authorizations = @Authorization(value = "api_key"), httpMethod = "GET")
    @ApiResponses(value = {
                    @ApiResponse(code = 400, message = "Invalid ID supplied"),
                    @ApiResponse(code = 404, message = "Pet not found")
    })
    public void getPetById(@ApiParam(value = "ID of pet to return") Long petId) {}

    @ApiOperation(value = "Deletes a pet", httpMethod = "DELETE")
    @ApiResponses(value = {
                    @ApiResponse(code = 400, message = "Invalid ID supplied"),
                    @ApiResponse(code = 404, message = "Pet not found")
    })
    public void deletePet(@ApiParam() String apiKey,
                          @ApiParam(value = "Pet id to delete", required = true) Long petId) {}

    @ApiOperation(value = "uploads an image", response = ApiResponse.class, httpMethod = "POST")
    public void uploadFile(@ApiParam(value = "ID of pet to update",
                    required = true) Long petId,
                           @ApiParam(value = "Additional data to pass to server") @FormDataParam("additionalMetadata") String testString,
                           @ApiParam(value = "file to upload") @FormDataParam("file") InputStream inputStream,
                           @ApiParam(value = "file detail") @FormDataParam("file") FormDataContentDisposition fileDetail) {

    }

    @ApiOperation(value = "Update an existing pet", httpMethod = "PUT")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid ID supplied"),
                    @ApiResponse(code = 404, message = "Pet not found"),
                    @ApiResponse(code = 405, message = "Validation exception")})
    public void updatePet(@ApiParam(value = "Pet object that needs to be added to the store",
                    required = true) Pet pet) {}

    /* Pet Test Model */
    public class Pet {
        public String name;
        public String type;
        public long   id;
    }

}
