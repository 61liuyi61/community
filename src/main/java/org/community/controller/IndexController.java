package org.community.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.community.Service.QuestionService;
import org.community.dot.QuestionDTO;
import org.community.mapper.QuestionMapper;
import org.community.mapper.UserMapper;
import org.community.model.Question;
import org.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model){
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for(Cookie cookie : cookies){
                if (cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if (user != null){
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }else {
            System.out.println("cookie为空");
        }

        List<QuestionDTO> questionList =questionService.list();
        model.addAttribute("questions", questionList);
        return "index";
    }

}
