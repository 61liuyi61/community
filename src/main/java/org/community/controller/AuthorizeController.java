package org.community.controller;

import org.community.dot.AcceseTokenDTO;
import org.community.dot.GithubUser;
import org.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.url}")
    private String redirectUrl;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code,
                           @RequestParam(name = "state")String state){
        AcceseTokenDTO accessToDTO = new AcceseTokenDTO();
        accessToDTO.setClient_id(clientId);
        accessToDTO.setClient_secret(clientSecret);
        accessToDTO.setCode(code);
        accessToDTO.setRedirect_uri(redirectUrl);
        accessToDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessToDTO);
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println(user.getName());
        return "index";
    }

}


