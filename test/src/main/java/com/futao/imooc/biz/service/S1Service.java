package com.futao.imooc.biz.service;

import com.futao.imooc.web.annotation.Autowired;
import com.futao.imooc.web.annotation.Service;

/**
 * @author futao
 * @date 2020/3/4.
 */
@Service
public class S1Service {

    public S1Service(S2Service s2Service) {
        this.s2Service = s2Service;
    }

    @Autowired
    private S2Service s2Service;

    public void s1() {
        System.out.println("s1");
    }
}
