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

package drm.opentosca.mock.service;

import drm.opentosca.mock.FileUploadController;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class GenerateCsarResponse {

    private static final Logger LOG = getLogger(GenerateCsarResponse.class);

    private FileUploadController fileUploadController;

    @Autowired
    public GenerateCsarResponse(FileUploadController fileUploadController) {
        this.fileUploadController = fileUploadController;
    }

    public String returnCSAR(String csarid) {
        LOG.debug("Started Response Generator");
        LOG.debug("Picking up the stored CSAR");
        MultipartFile csarFile = fileUploadController.getCsarFile();
        if (csarid.equals(csarFile.getOriginalFilename().substring(0, csarFile.getOriginalFilename().indexOf(".")))) {
            LOG.debug("Checked CSAR ID from CSAR with Requestparam - success");
            return csarFile.getOriginalFilename().substring(0, csarFile.getOriginalFilename().indexOf("."));
        }
        LOG.debug("Stored CSAR and Requestparam do not match, no file is returned");
        return null;
    }

}
