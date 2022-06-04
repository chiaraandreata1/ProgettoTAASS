package com.example.gateway;

import com.example.gateway.rabbithole.UserRabbitClient;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserRabbitClient userRabbitClient;

    @Autowired
    private SessionRepository sessionRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain) throws ServletException, IOException {

        Session session = sessionRepository.findById(RequestContextHolder.getRequestAttributes().getSessionId());

        if (session == null) {
            session = sessionRepository.createSession();
            RequestContext.getCurrentContext().addZuulRequestHeader("ZuulSession", session.getId());
            httpServletResponse.addHeader("ZuulSession", session.getId());
        }

        if (!Objects.equals("http://localhost:8080/ws", httpServletRequest.getRequestURL().toString())) {
            String jwt = getJwtFromRequest(httpServletRequest);
            if (jwt != null) {
                System.out.println(jwt);

                try {

//                    SecurityContext securityContext = new HttpSessionSecurityContextRepository().loadContext(new HttpRequestResponseHolder(httpServletRequest, httpServletResponse));
                    UsernamePasswordAuthenticationToken token = userRabbitClient.validateToken(jwt);
                    SecurityContext preContext = SecurityContextHolder.getContext();
                    preContext.setAuthentication(token);
                    session.setAttribute("TOKEN", token);



                } catch (ResponseStatusException ex) {
                    httpServletResponse.sendError(ex.getStatus().value(), ex.getMessage());
                }

            } else {
                logger.info(httpServletRequest.getRequestURL());
//                SecurityContextHolder.clearContext();
            }
        }

        sessionRepository.save(session);

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
