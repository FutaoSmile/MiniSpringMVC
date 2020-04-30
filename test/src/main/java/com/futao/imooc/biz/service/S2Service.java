package com.futao.imooc.biz.service;

import com.futao.imooc.web.annotation.Autowired;
import com.futao.imooc.web.annotation.Service;

/**
 * @author futao
 * @date 2020/3/4.
 */
@Service
public class S2Service {

    @Autowired
    private S1Service s1Service;
}
