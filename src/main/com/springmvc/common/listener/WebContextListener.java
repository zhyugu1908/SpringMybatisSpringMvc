package com.springmvc.common.listener;

import com.springmvc.common.constants.GlobalConstants;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;

/**
 * Created by zhangyuguang on 2018/10/17.
 */
@WebListener
public class WebContextListener extends ContextLoaderListener {

    public WebApplicationContext  initWebApplicationContext(ServletContext context){
        if (!GlobalConstants.ptrintKeyLoadMessage()){
            return null;
        }

        return super.initWebApplicationContext(context);
    }
}
