package com.futao.imooc.web.annotation.handler;

import com.futao.imooc.web.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author futao
 * @date 2020/3/2.
 */
@Slf4j
public class BeanFactory {


    /**
     * 容器，存放初始化好的Bean
     */
    private static final Map<Class<?>, Object> BEAN_CACHE = new HashMap<>();

    /**
     * 从容器中获取Bean
     *
     * @param clazz
     * @return
     */
    public static Object getBean(Class<?> clazz) {
        return BEAN_CACHE.get(clazz);
    }

    public static void initBean(Set<Class<?>> classes) {
        Set<Class<?>> classSet = BeanFactory.filterClassNeed2Bean(classes);
        while (classSet.size() > 0) {
            HashSet<Class<?>> operationSet = new HashSet<>(classSet);
            //开始之前剩余的class数量
            int currentSurplusSize = operationSet.size();
            out:
            for (Class<?> aClass : operationSet) {
                try {
                    Object instance = aClass.newInstance();
                    Field[] fields = aClass.getDeclaredFields();
                    in:
                    for (Field field : fields) {
                        if (field.isAnnotationPresent(Autowired.class)) {
                            Object bean = BeanFactory.getBean(field.getType());
                            if (bean == null) {
                                continue out;
                            } else {
                                field.setAccessible(true);
                                field.set(instance, bean);
                            }
                        }
                    }
                    log.info("加载Bean[{}]", aClass.getName());
                    BEAN_CACHE.put(aClass, instance);
                    classSet.remove(aClass);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (classSet.size() == currentSurplusSize) {
                throw new RuntimeException("发生了循环依赖");
            }
        }
    }

    private static Set<Class<?>> filterClassNeed2Bean(Set<Class<?>> classes) {
        return classes.stream().filter(it ->
                it.isAnnotationPresent(Component.class)
                        ||
                        it.isAnnotationPresent(Controller.class)
                        ||
                        it.isAnnotationPresent(Repository.class)
                        ||
                        it.isAnnotationPresent(Service.class)
                        ||
                        it.isAnnotationPresent(Bean.class)
        ).collect(Collectors.toSet());
    }

}
