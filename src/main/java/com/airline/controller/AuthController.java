package com.airline.controller;

import com.airline.model.User;
import com.airline.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        // If already logged in, redirect to home
        if (session.getAttribute("currentUser") != null) {
            return "redirect:/";
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                          @RequestParam String password,
                          @RequestParam String fullName,
                          @RequestParam String email,
                          @RequestParam(required = false) String phone,
                          Model model) {
        if (userService.existsByUsername(username)) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }
        if (userService.existsByEmail(email)) {
            model.addAttribute("error", "Email already exists");
            return "register";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        userService.registerUser(user);

        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        @RequestParam(required = false, defaultValue = "false") boolean rememberMe,
                        HttpSession session,
                        HttpServletResponse response,
                        Model model) {
        var userOpt = userService.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            session.setAttribute("currentUser", userOpt.get());
            
            // Remember me cookie (expires in 7 days)
            if (rememberMe) {
                Cookie cookie = new Cookie("rememberMe", username);
                cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
                cookie.setPath("/");
                response.addCookie(cookie);
            }
            
            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        // Invalidate session
        session.invalidate();
        
        // Clear remember me cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("rememberMe")) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }
        
        return "redirect:/";
    }
}
