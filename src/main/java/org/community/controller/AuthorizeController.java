package org.community.controller;

import org.community.dot.AcceseTokenDTO;
import org.community.dot.GithubUser;
import org.community.mapper.UserMapper;
import org.community.model.User;
import org.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private UserMapper userMapper;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.url}")
    private String redirectUrl;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code,
                           @RequestParam(name = "state")String state,
                           HttpServletRequest request){
        AcceseTokenDTO accessToDTO = new AcceseTokenDTO();
        accessToDTO.setClient_id(clientId);
        accessToDTO.setClient_secret(clientSecret);
        accessToDTO.setCode(code);
        accessToDTO.setRedirect_uri(redirectUrl);
        accessToDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessToDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if(githubUser != null){
            User user = new User();
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setToken(UUID.randomUUID().toString());
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            //登录成功，写cookie和session
            request.getSession().setAttribute("user", githubUser);
            return "redirect:/";
        }else {
            //登录失败，重新登录
            return "redirect:/";
        }

    }

}


