package ru.job4j.cars.util;

import org.springframework.ui.Model;
import ru.job4j.cars.model.User;

import javax.servlet.http.HttpSession;

public class SetUser {
    public static void setUser(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setLogin("Гость");
        }
        model.addAttribute("user", user);
    }
}
