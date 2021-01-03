package com.example.helloworld.demo.Controller;


import com.alibaba.fastjson.JSON;
import com.example.helloworld.demo.Provider.GithubProvider;
import com.example.helloworld.demo.dto.AccessTokenDTO;
import com.example.helloworld.demo.dto.GithubUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code, @RequestParam(name = "state")String state, HttpServletRequest request){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri(redirectUri);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser user = githubProvider.getUser(accessToken);
        if (user != null){      //登陆成功
            request.getSession().setAttribute("uesr", user);
            System.out.println(user.getName());
            return "redirect:/";    //redirect应以路径重定向，故不能redirect：index
        }else {                 //登陆失败，显示失败信息
            return "redirect:/";
        }
//        return "index";       //直接return index，地址栏会看到token，故重定向
    }
}
