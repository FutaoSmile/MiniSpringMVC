package com.futao.imooc.web.annotation;

import java.lang.annotation.*;

/**
 * @author futao
 * @date 2020/2/28.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RequestParam {
    String value();
}
