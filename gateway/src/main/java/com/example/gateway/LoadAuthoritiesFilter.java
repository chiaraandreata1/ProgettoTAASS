package com.example.gateway;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Collection;

class SimplePrincipal implements AuthenticatedPrincipal, Serializable {

    private final String username;

    public SimplePrincipal(String username) {
        this.username = username;
    }

    @Override
    public String getName() {
        return username;
    }
}

@Component
public class LoadAuthoritiesFilter extends ZuulFilter {

    @Autowired
    private SessionRepository repository;

    @Autowired
    private HttpSession httpSession;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        System.out.printf("\n\n%s\n\n", httpSession.getId());

        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) httpSession.getAttribute("authorities");
        System.out.println(RequestContextHolder.currentRequestAttributes().getSessionId());
        String userName = (String) httpSession.getAttribute("userName");


        if (authorities != null && userName != null) {
            AuthenticatedPrincipal principal = new SimplePrincipal(userName);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
        } else {
            System.err.println("Missing");
        }

        return null;
    }
}
