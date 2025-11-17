package k23cnt3.tvtday7.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "TEST CONTROLLER IS WORKING!";
    }

    @GetMapping("/")
    @ResponseBody
    public String home() {
        return "HOME PAGE - Application is running!";
    }

    // Thêm endpoint test template
    @GetMapping("/test-template")
    public String testTemplate() {
        System.out.println("=== TESTING TEMPLATE RENDERING ===");
        return "tvtcategory-list";
    }
}