/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jovidic.slusaci;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Web application lifecycle listener.
 *
 *
 * @author jovidic
 */
@WebListener()
public class SlusacAplikacije implements ServletContextListener {

    private ServletContext context;


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.context = sce.getServletContext();

    }

    /**
     *
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("DESTROY 3");

    }


}
