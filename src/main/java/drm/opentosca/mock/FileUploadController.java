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

package drm.opentosca.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class FileUploadController {

    private RestTemplate restTemplate;
    private String DRM_CONTROLLER_URL = "http://localhost:8082/v1/drmcontroller/postlicense";
    private MultipartFile csarfile;

    private static final Logger LOG = getLogger(FileUploadController.class);

    @Autowired
    public FileUploadController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/")
    public String listUploadedFiles() throws IOException {
        LOG.debug("Requested Uploadform");
        return "uploadForm";
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("csarfile") MultipartFile csarfile,
                                   RedirectAttributes redirectAttributes) {
        LOG.debug("Handling FileUpload");

        if (file.isEmpty() || csarfile.isEmpty()) {
            LOG.debug("not all files selected to upload, redirecting");
            redirectAttributes.addFlashAttribute("error", "Bitte CSAR und Lizenz auswählen");
            return "redirect:/";
        }

        this.csarfile = csarfile;

        LOG.debug("Generating POST Request");
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        try {
            map.add("file", new MultipartFileInputStreamResource(file.getInputStream(), file.getOriginalFilename()));
            LOG.debug(String.format("Added file %s to request", file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //setting up the header
        LOG.debug("Generating Request Header");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        LOG.debug(String.format("Header generated with ContentTyp %s", headers.getContentType()));

        //sending license
        try {
            LOG.debug(String.format("POST REQUEST to: %s", DRM_CONTROLLER_URL));
            ResponseEntity<String> response = restTemplate.postForEntity(DRM_CONTROLLER_URL, requestEntity, String.class);
            LOG.debug(String.format("Received Response: %s", response.toString()));
            try {
                LOG.debug("Starting to prettify response for output");
                ObjectMapper mapper = new ObjectMapper();
                Object json = mapper.readValue(response.getBody(), Object.class);
                String output = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
                LOG.debug("Setting Redirect Attributes with: %s", output);
                redirectAttributes.addFlashAttribute("message", output);
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }

        } catch (HttpClientErrorException e) {
            LOG.error(String.format("Exception occured: %s", e));
            redirectAttributes.addFlashAttribute("message",
                    "Error " + e.getStatusCode() + " " +
                            e.getResponseBodyAsString().substring(e.getResponseBodyAsString().indexOf("\"message"), e.getResponseBodyAsString().lastIndexOf(",")));
        }

        return "redirect:/";
    }


    public MultipartFile getCsarFile() {
        return csarfile;
    }

    /**
     * Inner class to support multipart uploads with Request part "file" properly set
     */
    class MultipartFileInputStreamResource extends InputStreamResource {

        private final String filename;

        MultipartFileInputStreamResource(InputStream inputStream, String filename) {
            super(inputStream);
            this.filename = filename;
        }

        @Override
        public String getFilename() {
            return this.filename;
        }

        @Override
        public long contentLength() throws IOException {
            return -1;
        }
    }
}