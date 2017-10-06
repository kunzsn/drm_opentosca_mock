/**
 * Copyright (c) 2017 University of Stuttgart.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * and the Apache License 2.0 which both accompany this distribution,
 * and are available at http://www.eclipse.org/legal/epl-v20.html
 * and http://www.apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Sebastian Kunz - initial implementation
 */

package drm.opentosca.mock.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/v1/drm/opentosca/mock")
public interface DrmOpenToscaResoucreInterface {

    String GET_CSAR = "/getcsar";

    @ApiOperation(value = "returns the CSAR ID from the uploaded CSAR file")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "succesfully returns CSAR ID"),
            @ApiResponse(code = 500, message = "an internal error occured")
    })
    @RequestMapping(value = GET_CSAR, method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    String getCSAR(@ApiParam(name = "csarid", required = true) @RequestParam(name = "csarid") String csarid);

}
