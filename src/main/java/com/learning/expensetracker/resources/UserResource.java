package com.learning.expensetracker.resources;

import com.learning.expensetracker.models.User;
import com.learning.expensetracker.services.UserService;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@RestController
@RequestMapping("/api/users")
public class UserResource {
    private static final String EMAIL="email";

    @Autowired
    private UserService userService;

    @PostMapping(path="/register")
    public String registerUser(@RequestBody Map<String,Object> userMap){
        User user=new User();
        user.setPassword((String)userMap.get("password"));
        user.setFirstName((String)userMap.get("firstName"));
        user.setLastName((String)userMap.get("lastName"));
        user.setUserId((String)userMap.get("userId"));
        user.setEmail((String)userMap.get(EMAIL));
        User user1=userService.registerUser(user);
        return user1.toString();
    }

    @PutMapping(path="")
    public String updateCategory(@PathVariable String categoryId) {
        return null;
    }

    @PostMapping(path="/login")
    public Map<String, String> loginUser(@RequestBody Map<String,Object> userMap){
        String email=(String)userMap.get(EMAIL);
        String password=(String)userMap.get("password");
        return generateJWTToken(userService.validateUser(email,password));
    }

    public Map<String,String> generateJWTToken(User user){
        Map<String,String> tokenMap=new HashMap<>();
        try(FileInputStream inputStream=new FileInputStream("./src/main/resources/application.properties");){
            Properties properties=new Properties();
            properties.load(inputStream);

            String key=properties.getProperty("api.secret.key");
            long expiry=Long.parseLong(properties.getProperty("api.token.validity"));
            long timeStamp=System.currentTimeMillis();
            byte[] keyBytes= Decoders.BASE64.decode(key);


            String token= Jwts.builder().signWith(Keys.hmacShaKeyFor(keyBytes)).
                    setIssuedAt(new Date(timeStamp)).
                    setExpiration(new Date(timeStamp+expiry)).
                    claim("userId",user.getUserId()).
                    claim("firstName",user.getFirstName()).
                    claim("lastName",user.getLastName()).
                    claim(EMAIL,user.getEmail()).
                    compact();

            tokenMap.put("token",token);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return tokenMap;
    }

}
