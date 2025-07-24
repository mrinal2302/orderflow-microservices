package com.order.flow.api_gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/check")
    public String check() {
        System.out.println("github checking working or not");
        return "working properly";
    }
}
