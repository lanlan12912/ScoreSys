package com.yelanlan.scoremanagersystem.Controller;

import com.yelanlan.scoremanagersystem.Repository_Impl.*;
import com.yelanlan.scoremanagersystem.Service_Impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @Autowired
    UserService userService;

    @RequestMapping("/say")
    public User sayHello(@RequestBody Map<String,Object> map){
        String userNumber = map.get("number").toString();
        return userService.findUserByNumber(userNumber);
    }
}
