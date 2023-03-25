package com.learning.expensetracker.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AuthFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;

        String authHeader=request.getHeader("Authorization");
        if(authHeader!=null){
            String[] authHeaderArr=authHeader.split("Bearer");
            if(authHeaderArr.length>1 && authHeaderArr[1]!=null){
                String token=authHeaderArr[1];
                try(FileInputStream inputStream=new FileInputStream("./src/main/resources/application.properties")){
                    Properties properties=new Properties();
                    properties.load(inputStream);
                    String key=properties.getProperty("api.secret.key");
                    byte[] keyBytes= Decoders.BASE64.decode(key);


                    Claims claims= (Claims) Jwts.parserBuilder().setSigningKey(keyBytes).build().parse(token).getBody();
                    request.setAttribute("userId",claims.get("userId").toString());
                }catch (Exception e){
                    response.sendError(HttpServletResponse.SC_FORBIDDEN,"Invalid/expired token");
                }
            }
            else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,"Authorization token must be Bearer");
            }
        }
        else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,"No Authorization token provided");
        }

        filterChain.doFilter(request,response);


    }
}
