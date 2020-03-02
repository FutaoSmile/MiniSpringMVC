package com.futao.imooc.biz.controller;

import com.futao.imooc.biz.service.SalaryService;
import com.futao.imooc.web.annotation.Autowired;
import com.futao.imooc.web.annotation.Controller;
import com.futao.imooc.web.annotation.RequestMapping;
import com.futao.imooc.web.annotation.RequestParam;
import com.futao.imooc.web.servlet.SystemController;
import lombok.extern.slf4j.Slf4j;

/**
 * @author futao
 * @date 2020/2/29.
 */
@Slf4j
@Controller
public class SalaryController {

    @Autowired
    private SystemController systemController;

    @Autowired
    private SalaryService salaryService;

    @RequestMapping("/getSalary.json")
    public int getSalary(
            @RequestParam("name")
                    String name,
            @RequestParam("experience")
                    String experience) {

        log.info("{},{}", name, experience);
        return salaryService.calculateSalary();
    }

    @RequestMapping("/getSalary1.json")
    public int getSalary1(
            @RequestParam("name")
                    String name,
            @RequestParam("experience")
                    int experience) {
        return 15000;
    }
}
