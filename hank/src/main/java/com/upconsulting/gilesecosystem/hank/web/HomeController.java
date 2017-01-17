package com.upconsulting.gilesecosystem.hank.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping(value = "/")
    public String home() throws IOException {
//        Process p = Runtime.getRuntime().exec("/usr/local/bin/docker run ocropus ./run-test");
//        String line = "";
//        BufferedReader input =
//                new BufferedReader
//                  (new InputStreamReader(p.getInputStream()));
//              while ((line = input.readLine()) != null) {
//                System.out.println(line);
//              }
//              input.close();
        return "home";
    }
}
