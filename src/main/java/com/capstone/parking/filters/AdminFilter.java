package com.capstone.parking.filters;

import com.capstone.parking.constants.ApaConstant;
import com.capstone.parking.constants.ApaRole;
import com.capstone.parking.entity.UserEntity;
import com.capstone.parking.repository.UserRepository;
import com.capstone.parking.service.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String token = (String) request.getHeader("Authorization");
        try {
            if (token != null && !token.equals("null")) {
                UserEntity userEntity = TokenAuthenticationService.getUserFromToken(token);
                if (userEntity != null) {
                    if (userEntity.getRoleByRoleId().getName().equals(ApaRole.ROLE_SUPERADMMIN)) {
                        request.setAttribute("USER_INFO", userEntity);
                        filterChain.doFilter(request, response);
                    } else {
                        response.setStatus(HttpStatus.FORBIDDEN.value());
                        response.getWriter().write(ApaConstant.ACCESS_DENIED_MESSAGE);
                    }
                } else {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.getWriter().write(ApaConstant.ACCESS_DENIED_MESSAGE);
                }
            } else {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().write(ApaConstant.ACCESS_DENIED_MESSAGE);
            }
        } catch (Exception e) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write(ApaConstant.ACCESS_DENIED_MESSAGE);
        }

    }

    @Override
    public void destroy() {

    }
}
