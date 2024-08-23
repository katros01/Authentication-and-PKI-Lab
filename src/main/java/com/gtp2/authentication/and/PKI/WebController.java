package com.gtp2.authentication.and.PKI;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/home")
    public String viewHomePage() {
        return "home";
    }

    @GetMapping("/home/admin")
    public String viewAdminPage() {
        return "adminHome";
    }

    @GetMapping("/home/user")
    public String viewUserPage() {
        return "userHome";
    }
}
