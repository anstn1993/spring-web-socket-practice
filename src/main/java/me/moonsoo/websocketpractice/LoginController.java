package me.moonsoo.websocketpractice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpSession session) {
        session.setAttribute("user", user);
        return "redirect:/";
    }

    @GetMapping("/")
    public String main(HttpSession session) {
        return "index";
    }
}
