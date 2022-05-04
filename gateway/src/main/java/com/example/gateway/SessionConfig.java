package com.example.gateway;

import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

@EnableJdbcHttpSession
public class SessionConfig extends AbstractHttpSessionApplicationInitializer {
}
