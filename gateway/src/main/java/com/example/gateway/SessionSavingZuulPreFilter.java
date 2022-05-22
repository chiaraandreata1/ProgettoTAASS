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
import java.net.HttpCookie;
import java.util.*;

@Component
public class SessionSavingZuulPreFilter extends ZuulFilter {

    Log log = LogFactory.getLog(SessionSavingZuulPreFilter.class);

    @Autowired
    private SessionRepository repository;

    @Override
    public boolean shouldFilter() {
        return !"http://localhost:8080/ws".equals(RequestContext.getCurrentContext().getRequest().getRequestURL().toString());
//        return true;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();

        HttpSession httpSession = request.getSession();

        Session session = repository.findById(httpSession.getId());
        System.out.println(String.format("\n\n%s\n\n", httpSession.getId()));
//        log.debug(context);
//        log.debug(httpSession);
//        log.debug(session);

//        HttpCookie.parse(context.)
        String cookiesString = context.getRequest().getHeader("Cookie");
//        String sessionID;
//        String clearSessionID;
//        if (cookiesString != null) {
//            List<HttpCookie> cookies = HttpCookie.parse(cookiesString);
//            sessionID = cookies
//                    .stream()
//                    .filter(c -> "SESSION".equalsIgnoreCase(c.getName()))
//                    .map(HttpCookie::getValue)
//                    .findFirst()
//                    .orElse(null);
//            cookies
//                    .stream()
//                    .filter(c -> c.getName().contains("AUTH"))
//                    .forEach(c -> {
//                        System.out.printf("%s = %s\n", c.getName(), c.getValue());
//                    });
//            clearSessionID = new String(Base64.getDecoder().decode(sessionID));
//        } else {
//            sessionID = null;
//            clearSessionID = null;
//        }
//        if (!Objects.equals(clearSessionID, httpSession.getId()))
//            System.out.printf("%s != %s\n", clearSessionID, httpSession.getId());

        StringBuilder cookieBuilder = new StringBuilder();
        if (cookiesString != null) {
            for (String c : cookiesString.split(";")) {
                String[] b = c.trim().split("=");
                if (!b[0].equalsIgnoreCase("SESSION")) {
                    cookieBuilder.append(c).append(";");
                }
            }
        }
        cookieBuilder.append("SESSION=").append(Base64.getEncoder().encodeToString(httpSession.getId().getBytes()));
//        cookieBuilder.append("SESSION=").append(httpSession.getId());
//        cookieBuilder.append(";HOP=ATAZKA");
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
