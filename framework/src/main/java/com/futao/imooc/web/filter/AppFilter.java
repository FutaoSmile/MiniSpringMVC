package com.futao.imooc.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author futao
 * @date 2020/2/29.
 */
public class AppFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(AppFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("init...");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.debug("filter...");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        log.debug("destroy...");
    }
}
