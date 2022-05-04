package com.example.gateway;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.Map;

@Component
public class SessionSavingZuulPreFilter extends ZuulFilter {

    Log log = LogFactory.getLog(SessionSavingZuulPreFilter.class);

    @Autowired
    private SessionRepository repository;

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();

        HttpSession httpSession = context.getRequest().getSession();

        Session session = repository.findById(httpSession.getId());
        log.debug(context);
        log.debug(httpSession);
        log.debug(session);

        String cookie = context.getRequest().getHeader("Cookie");
        StringBuilder cookieBuilder = new StringBuilder();
        if (cookie != null) {
            for (String c : cookie.split(";")) {
                String[] b = c.trim().split("=");
                if (!b[0].equalsIgnoreCase("SESSION")) {
                    cookieBuilder.append(c).append(";");
                }
            }
        }
        cookieBuilder.append("SESSION=").append(httpSession.getId());
        context.addZuulRequestHeader("Cookie", cookieBuilder.toString());

        return null;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }
}
