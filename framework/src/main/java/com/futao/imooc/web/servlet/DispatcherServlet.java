package com.futao.imooc.web.servlet;

import com.futao.imooc.web.annotation.RequestParam;
import com.futao.imooc.web.annotation.handler.BeanFactory;
import com.futao.imooc.web.annotation.handler.RequestMappingHandler;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author futao
 * @date 2020/2/29.
 */
@Slf4j
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HashMap<String, RequestMappingHandler.HandlerMapping> requestMappingCache = RequestMappingHandler.REQUEST_MAPPING_CACHE;
        String servletPath = req.getServletPath();
        RequestMappingHandler.HandlerMapping handlerMapping = requestMappingCache.get(servletPath);
        ServletOutputStream os = resp.getOutputStream();
        Map<String, String[]> parameterMap = req.getParameterMap();
        if (handlerMapping != null) {
            try {
//                Object instance = handlerMapping.getClazz().newInstance();
                Object instance = BeanFactory.getBean(handlerMapping.getClazz());
                Method method = handlerMapping.getMethod();
                List<Parameter> parameterList = handlerMapping.getParameterList();
                Object[] params = new Object[parameterList.size()];
                for (int i = 0; i < parameterList.size(); i++) {
                    Parameter parameter = parameterList.get(i);
                    String requestParameterName = parameter.getAnnotation(RequestParam.class).value();
                    String[] requestParam = parameterMap.get(requestParameterName);
                    if (requestParam != null) {
                        // TODO: 2020/2/29 参数赋值，类型转换问题
                        Class<?> type = parameter.getType();
                        params[i] = requestParam[0];
                    } else {
                        params[i] = null;
                    }
                }
                log.info("param:[{}]", com.alibaba.fastjson.JSON.toJSONString(params));
                os.write(method.invoke(instance, params).toString().getBytes(StandardCharsets.UTF_8));
                os.flush();
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

        } else {
            os.write("no handler found to response...".getBytes(StandardCharsets.UTF_8));
            os.flush();
        }
    }
}
