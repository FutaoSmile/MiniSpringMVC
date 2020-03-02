package com.futao.imooc.web.server;

import com.futao.imooc.web.annotation.handler.BeanFactory;
import com.futao.imooc.web.annotation.handler.RequestMappingHandler;
import com.futao.imooc.web.filter.AppFilter;
import com.futao.imooc.web.servlet.DispatcherServlet;
import com.futao.imooc.web.util.ClassUtil;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import java.util.Set;

/**
 * @author futao
 * @date 2020/2/29.
 */
public class TomcatServer {

    public void startServer(String pkg) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(7000);
        tomcat.setHostname("localhost");


        DispatcherServlet dispatcherServlet = new DispatcherServlet();

        //一个context相当于一个应用webapp
        Context context = tomcat.addContext("", null);
        Tomcat.addServlet(context, "dispatcherServlet", dispatcherServlet).setAsyncSupported(true);
        context.addServletMappingDecoded("/", "dispatcherServlet");

        AppFilter appFilter = new AppFilter();
        FilterDef filterDef = new FilterDef();
        filterDef.setFilter(appFilter);
        filterDef.setDisplayName("APP拦截器");
        filterDef.setFilterName("appFilter");
        context.addFilterDef(filterDef);

        FilterMap filterMap = new FilterMap();
        filterMap.setFilterName("appFilter");
        filterMap.addURLPattern("/*");
        context.addFilterMap(filterMap);


        tomcat.init();
        tomcat.start();

        Set<Class<?>> classes = ClassUtil.extractPackageClass(pkg);

        RequestMappingHandler.initRequestMapping(classes);

        BeanFactory.initBean(classes);

        tomcat.getServer().await();

    }
}
