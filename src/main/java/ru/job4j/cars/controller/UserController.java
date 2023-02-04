package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.UserService;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static ru.job4j.cars.util.SetUser.setUser;
import static ru.job4j.cars.util.UserTimeZone.getZones;

@Controller
@ThreadSafe
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @GetMapping("registration")
    public String registration(Model model, HttpSession session) {
        model.addAttribute("timeZones", getZones());
        setUser(model, session);
        return "user/registration";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute User user, HttpSession session) {
        Optional<User> rsl = userService.create(user);
        if (rsl.isEmpty()) {
            session.setAttribute("message",  "Ошибка регистрации!");
            return "redirect:/user/showMessage";
        }
        return "redirect:/user/authorization";
    }

    @GetMapping("/authorization")
    public String authorization(Model model, HttpSession session) {
        setUser(model, session);
        return "user/authorization";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model, HttpSession session) {
        Optional<User> rsl = userService.findByLoginPass(user.getLogin(), user.getPassword());
        if (rsl.isEmpty()) {
            model.addAttribute("error", "Логин или пароль введены неверно!");
            return "user/authorization";
        }
        session.setAttribute("user", rsl.get());
        return "redirect:/post/posts";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/authorization";
    }

    @GetMapping("/showMessage")
    public String showMessage(Model model, HttpSession session) {
        setUser(model, session);
        model.addAttribute("message", session.getAttribute("message"));
        return "error/showMessage";
    }
}
