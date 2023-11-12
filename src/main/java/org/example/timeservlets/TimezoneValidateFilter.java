package org.example.timeservlets;

import org.example.utils.TimeServletUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(value = "/time")
public class TimezoneValidateFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (TimeServletUtils.validRequest(req, TimeServletUtils.QUERY_PARAM)) {
            chain.doFilter(req, res);
        } else {
            res.setStatus(400);
            res.setContentType("text/html; charset=utf-8");
            res.getWriter().write("<h1>Invalid timezone</h1>");
            res.getWriter().close();
        }
    }
}
