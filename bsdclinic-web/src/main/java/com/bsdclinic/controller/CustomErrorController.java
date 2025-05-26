package com.bsdclinic.controller;

import com.bsdclinic.url.WebUrl;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {
    @RequestMapping(WebUrl.ERROR)
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/error404";
            }
            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "error/error403";
            }
            if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                return "error/error401";
            }
        }
        return "error/error500";
    }
}