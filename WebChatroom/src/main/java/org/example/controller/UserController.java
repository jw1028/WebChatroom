package org.example.controller;

import org.example.exception.AppException;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object login(@RequestBody User user, HttpServletRequest req){
        User query = userMapper.selectOne(user);
        if(query == null)
            throw new AppException("用户名或密码错误");
        HttpSession session = req.getSession();
        session.setAttribute("user", query);
        return query;
    }

    /**
     * 页面初始化时，检查是否已登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public Object login(HttpServletRequest req){
        HttpSession session = req.getSession(false);
        if(session != null){
            User get = (User) session.getAttribute("user");
            if(get != null)
                return get;
        }
        throw new AppException("您未登陆");
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public Object logout(HttpSession session){
        session.removeAttribute("user");
        return null;
    }
}
