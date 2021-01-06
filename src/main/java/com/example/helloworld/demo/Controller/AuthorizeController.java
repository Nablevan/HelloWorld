package com.example.helloworld.demo.Controller;


import com.example.helloworld.demo.Model.User;
import com.example.helloworld.demo.Provider.GithubProvider;
import com.example.helloworld.demo.dto.AccessTokenDTO;
import com.example.helloworld.demo.dto.GithubUser;
import com.example.helloworld.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

//    @Value("${github.client.id}")
    @Value("6a312e03edabef8a3323")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private UserMapper userMapper;


    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code, @RequestParam(name = "state") String state,
                           HttpServletRequest request, HttpServletResponse response) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri(redirectUri);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser != null) {      //登陆成功,写入数据库和session
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            response.addCookie(new Cookie("token", token));
//            request.getSession().setAttribute("user", githubUser);
            System.out.println(githubUser.getName() + "首次登录");
            return "redirect:/";    //redirect应以路径重定向，故不能redirect：index
        } else {                 //登陆失败，显示失败信息
            return "redirect:/";
        }
//        return "index";       //直接return index，地址栏会看到token，故重定向
    }
}
