package com.futao.imooc.web.servlet;

import com.alibaba.fastjson.JSON;
import com.futao.imooc.web.annotation.Controller;
import com.futao.imooc.web.annotation.RequestMapping;
import com.futao.imooc.web.annotation.handler.RequestMappingHandler;

/**
 * @author futao
 * @date 2020/3/1.
 */
@Controller
public class SystemController {

    @RequestMapping("/mappingCache")
    public String mappingCache() {
        return JSON.toJSONString(RequestMappingHandler.REQUEST_MAPPING_CACHE, true);
    }
}
