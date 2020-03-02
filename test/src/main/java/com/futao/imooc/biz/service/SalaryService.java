package com.futao.imooc.biz.service;

import com.futao.imooc.web.annotation.Service;

import java.util.Random;

/**
 * @author futao
 * @date 2020/3/2.
 */
@Service
public class SalaryService {

    public int calculateSalary() {
        return new Random().nextInt(10000);
    }
}
