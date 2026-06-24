package com.example.flame;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/categories")
    public String categories() {
        return "forward:/categories.html";
    }
}
