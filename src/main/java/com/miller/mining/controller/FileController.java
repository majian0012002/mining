package com.miller.mining.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value="file")
public class FileController {

    @RequestMapping("download")
    public String downloadFile(HttpServletRequest request, HttpServletResponse response) {

        return null;
    }
}
