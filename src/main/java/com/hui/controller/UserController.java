package com.hui.controller;

import com.hui.entity.User;
import com.hui.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * @author lcv8
 */
@Controller
@RequestMapping("user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("login")
    public String login(User user , HttpSession session){
        User login = userService.login(user);
        if(login != null){
            session.setAttribute("user",login);
            log.info("id---->"+login.getId());
            return "redirect:/file/findAll";
        }
        return "redirect:/index";
    }
}
