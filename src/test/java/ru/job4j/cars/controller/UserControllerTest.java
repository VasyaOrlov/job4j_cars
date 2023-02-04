package ru.job4j.cars.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.UserService;

import javax.servlet.http.HttpSession;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.*;

class UserControllerTest {

    @Test
    public void whenCreate() {
        User user = new User(0, "1", "1", "Africa/Abidjan");
        UserService userService = mock(UserService.class);
        HttpSession session = mock(HttpSession.class);
        UserController userController = new UserController(userService);

        when(userService.create(Mockito.any(User.class))).thenReturn(Optional.of(user));
        String rsl = userController.create(user, session);

        verify(userService).create(user);
        assertThat(rsl).isEqualTo("redirect:/user/authorization");
    }

}