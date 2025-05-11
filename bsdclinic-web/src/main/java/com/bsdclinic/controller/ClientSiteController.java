package com.bsdclinic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientSiteController {
    @GetMapping({"/", ""})
    public String toClientHomePage() {
        return "client/index";
    }
}
