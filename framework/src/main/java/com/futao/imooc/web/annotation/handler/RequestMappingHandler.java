package com.futao.imooc.web.annotation.handler;

import com.futao.imooc.web.annotation.Controller;
import com.futao.imooc.web.annotation.RequestMapping;
import com.futao.imooc.web.annotation.RequestParam;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author futao
 * @date 2020/2/29.
 */
@Slf4j
public class RequestMappingHandler {

    public static final HashMap<String, HandlerMapping> REQUEST_MAPPING_CACHE = new HashMap<>();

    public static void initRequestMapping(Set<Class<?>> classes) {
        log.debug("开始初始化uri映射配置........");
        for (Class<?> aClass : classes) {
            if (aClass.isAnnotationPresent(Controller.class)) {
                for (Method method : aClass.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        String requestUri = method.getAnnotation(RequestMapping.class).value();
                        HandlerMapping handlerMapping = REQUEST_MAPPING_CACHE.get(requestUri);
                        if (handlerMapping == null) {
                            HandlerMapping mapping = new HandlerMapping();
                            mapping.setUri(requestUri);
                            mapping.setClazz(aClass);
                            mapping.setMethod(method);
                            ArrayList<Parameter> parameterList = new ArrayList<>();
                            for (Parameter parameter : method.getParameters()) {
                                if (parameter.isAnnotationPresent(RequestParam.class)) {
//                                    String parameterValue = parameter.getAnnotation(RequestParam.class).value();
                                    parameterList.add(parameter);
                                }
                            }
                            log.debug("Mapped [{}.{}] onto [{}]", aClass.getName(), method.getName(), requestUri);
                            mapping.setParameterList(parameterList);
                            REQUEST_MAPPING_CACHE.put(requestUri, mapping);
                        } else {
                            throw new RuntimeException(handlerMapping.getClazz().getName() + "." + handlerMapping.getMethod().getName() + "与" +
                                    aClass.getName() + "." + method.getName() + "所配置的路径重复，请修改");
                        }
                    }
                }
            }
        }
        log.debug("初始化uri映射配置完成........");
    }


    @Getter
    @Setter
    public static class HandlerMapping {
        private String uri;
        private Class<?> clazz;
        private Method method;
        private List<Parameter> parameterList;
    }
}
