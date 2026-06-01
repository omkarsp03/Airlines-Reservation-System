package com.airline.controller;

import com.airline.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model, 
                      @SessionAttribute(name = "currentUser", required = false) Object currentUser,
                      HttpServletRequest request,
                      HttpSession session) {
        // Check for rememberMe cookie if not already logged in
        if (currentUser == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("rememberMe")) {
                        String username = cookie.getValue();
                        var userOpt = userService.findByUsername(username);
                        if (userOpt.isPresent()) {
                            session.setAttribute("currentUser", userOpt.get());
                            currentUser = userOpt.get();
                        }
                    }
                }
            }
        }
        
        model.addAttribute("currentUser", currentUser);
        return "index";
    }
}
