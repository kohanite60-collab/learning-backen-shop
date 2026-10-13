package org.example.asimpleshop.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.example.asimpleshop.pojo.Result;
import org.example.asimpleshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;

    @GetMapping("/userLogin")
    @ResponseBody
    public Result userLogin(String name,String pwd){


        // 1获取subject对象
        Subject subject = SecurityUtils.getSubject();
        // 2封装请求数据到token
        AuthenticationToken token = new UsernamePasswordToken(name,pwd);
        // 3调用login方法进行登录认证
        try{
            subject.login(token);
            return Result.success("登录成功");

        }catch(AuthenticationException e){
            e.printStackTrace();

            return Result.error("登录失败");
        }
}


    @PostMapping("/userRegister")
    @ResponseBody
    public Result userRegister(String name,String pwd){

        boolean ok= userService.register(name,pwd);
        if (ok ){
            return Result.success("注册成功");
        }else{
            return Result.error("注册失败");
        }


    }
















}
