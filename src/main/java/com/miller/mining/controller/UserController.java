package com.miller.mining.controller;

import com.miller.mining.vo.RequestVo;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("user")
public class UserController {

    @RequestMapping(value = "register")
    public void register(final HttpServletRequest request,
                         final RequestVo requestVo,
                         final ModelMap map) {

    }
}
