package org.example.timeservlets;

import org.example.utils.TimeServletUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JavaxServletWebApplication;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    private TemplateEngine engine;

    @Override
    public void init() {
        engine = new TemplateEngine();

        JavaxServletWebApplication jswa = JavaxServletWebApplication.buildApplication(this.getServletContext());
        WebApplicationTemplateResolver resolver = new WebApplicationTemplateResolver(jswa);

        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);

        engine.addTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HashMap<String, Object> params = new HashMap<>();

        resp.setContentType("text/html; charset=utf-8");
        String timezone = TimeServletUtils.modifyParameter(req.getParameter(TimeServletUtils.QUERY_PARAM));
        if (timezone != null) {
            HttpSession session = req.getSession(true);

            Cookie cookie = new Cookie(TimeServletUtils.COOKIE_TIME_ZONE_PARAM, timezone);
            resp.addCookie(cookie);
        } else {
            timezone = TimeServletUtils.getTimeZoneCookie(req.getCookies());
            params.put(TimeServletUtils.COOKIE_TIME_ZONE_PARAM, timezone);
        }

        String formattedUtc = TimeServletUtils.getUtcDateTime(timezone);
        params.put(TimeServletUtils.TIME_AT_TIME_ZONE_PARAM, formattedUtc);

        Context simpleContext = new Context(
                req.getLocale(),
                params
        );
        engine.process("calctime", simpleContext, resp.getWriter());

        resp.getWriter().close();
    }
}
