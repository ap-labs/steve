package de.rwth.idsg.steve.custom.controller;

import de.rwth.idsg.steve.custom.dto.SendParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/external")
public class ExternalController {

    private static final Logger log = LoggerFactory.getLogger(ExternalController.class);

    @PostMapping("/send-params")
    public SendParams.Response sendParams(@RequestBody SendParams.Request request) {
        return null;
    }
}
