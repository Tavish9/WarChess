package cn.edu.sustech.cs309.security;

import cn.edu.sustech.cs309.service.impl.AccountServiceImpl;
import cn.edu.sustech.cs309.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private AccountServiceImpl accountService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)){
            filterChain.doFilter(request, response);
            return;
        }
        if (!JwtTokenUtil.validateToken(token))
            throw new RuntimeException("Authenticate fail");
        JwtTokenUtil.parseToken(token).getSubject();
        UserDetails userDetails = accountService.loadUserByUsername(JwtTokenUtil.parseToken(token).getSubject());
        Authentication authentication = JwtTokenUtil.getAuthentication(token, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().print("Authenticate successfully");
        response.reset();
        filterChain.doFilter(request, response);
    }
}
