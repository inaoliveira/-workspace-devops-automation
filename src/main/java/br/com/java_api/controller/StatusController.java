package br.com.java_api.controller;

import br.com.java_api.service.SystemInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatusController {

    @Autowired
    private SystemInfoService systemInfoService;

    @GetMapping("/status")
    public String statusPage(Model model) {
        long uptime = systemInfoService.getUptime();
        model.addAttribute("uptime", uptime);
        model.addAttribute("status", "UP");
        return "status";
    }
}