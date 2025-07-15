package com.bobhub.controller;

import com.bobhub.domain.User;
import com.bobhub.mapper.UserMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class UserController {

    private final UserMapper userMapper;

    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        List<User> users = userMapper.findAll();
        model.addAttribute("users", users);
        return "userList"; // templates/userList.html
    }
}
