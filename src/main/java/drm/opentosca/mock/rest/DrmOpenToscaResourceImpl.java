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

import drm.opentosca.mock.service.GenerateCsarResponse;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import static org.slf4j.LoggerFactory.getLogger;

@Api(value = "DRM OpenTOSCA Upload Mock", description = "Mock Endpoint")
@RestController
public class DrmOpenToscaResourceImpl implements DrmOpenToscaResoucreInterface {

    private static final Logger LOG = getLogger(DrmOpenToscaResourceImpl.class);

    private GenerateCsarResponse generateCsarResponse;

    @Autowired
    public DrmOpenToscaResourceImpl(GenerateCsarResponse generateCsarResponse) {
        this.generateCsarResponse = generateCsarResponse;
    }

    @Override
    public String getCSAR(String csarid) {
        LOG.debug("received GET Request");
        return generateCsarResponse.returnCSAR(csarid);
    }

}
