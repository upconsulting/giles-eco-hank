package com.upconsulting.gilesecosystem.hank.web;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping(value = "/")
    public String home() throws IOException {
        return "home";
    }
}
