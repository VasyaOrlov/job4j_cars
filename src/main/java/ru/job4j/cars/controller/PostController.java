package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.model.*;
import ru.job4j.cars.service.PostService;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static ru.job4j.cars.util.SetUser.setUser;

@Controller
@ThreadSafe
@AllArgsConstructor
@RequestMapping("/post")
public class PostController {
    private PostService postService;

    @GetMapping("/posts")
    public String posts(Model model, HttpSession session) {
        setUser(model, session);
        model.addAttribute("posts", postService.findAll());
        model.addAttribute("header", "Все объявления");
        return "post/posts";
    }

    @GetMapping("/add")
    public String add(Model model, HttpSession session) {
        setUser(model, session);
        return "post/add";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Post post,
                         @RequestParam("carName") String carName,
                         @RequestParam("engineName") String engineName,
                         @RequestParam("driverName") String driverName,
                         @RequestParam("price") long price,
                         @RequestParam("file") MultipartFile file,
                         HttpSession session) throws IOException {
        User user = (User) session.getAttribute("user");

        Engine engine = new Engine(0, engineName);

        Driver driver = new Driver(0, driverName);
        Set<Driver> drivers = new HashSet<>();
        drivers.add(driver);

        Car car = new Car(0, carName, engine, drivers);

        LocalDateTime created = LocalDateTime.now();

        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setCreated(created);
        priceHistory.setAfter(price);
        List<PriceHistory> priceHistories = new ArrayList<>();
        priceHistories.add(priceHistory);

        post.setPriceHistoryList(priceHistories);
        post.setCreated(created);
        post.setCar(car);
        post.setUser(user);
        post.setPhoto(file.getBytes());
        Optional<Post> rsl = postService.add(post);
        if (rsl.isEmpty()) {
            session.setAttribute("message", "Ошибка при добавлении объявления");
            return "redirect:/post/showMessage";
        }
        return "redirect:/post/posts";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable int id, Model model, HttpSession session) {
        setUser(model, session);
        return "post/view";
    }

    @GetMapping("/getPhoto/{id}")
    public ResponseEntity<Resource> getPhoto(@PathVariable("id") Integer id) {
        Optional<Post> opPost = postService.findById(id);
        if (opPost.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        byte[] photo = opPost.get().getPhoto();
        return ResponseEntity.ok()
                .headers(new HttpHeaders())
                .contentLength(photo.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(photo));
    }

    @GetMapping("/showMessage")
    public String showMessage(Model model, HttpSession session) {
        setUser(model, session);
        model.addAttribute("message", session.getAttribute("message"));
        return "error/showMessage";
    }
}
