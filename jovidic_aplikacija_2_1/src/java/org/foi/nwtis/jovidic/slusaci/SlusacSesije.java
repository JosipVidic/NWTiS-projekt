/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jovidic.slusaci;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 * Slušač sesije u kojemu se dodaju atributi u sesiju.
 *
 * @author jovidic
 */
public class SlusacSesije implements HttpSessionAttributeListener {

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        if (event.getName().compareTo("user") == 0) {
            String user = (String) event.getValue();
            List<String> allUsers = (List<String>) event.getSession().getServletContext().getAttribute("users");
            if (allUsers == null) {
                allUsers = new ArrayList<String>();
            }
            allUsers.add(user);
            event.getSession().getServletContext().setAttribute("users", allUsers);
        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        if (event.getName().compareTo("user") == 0) {
            String user = (String) event.getValue();
            List<String> allUsers = (List<String>) event.getSession().getServletContext().getAttribute("users");
            allUsers.remove(user);
            event.getSession().getServletContext().setAttribute("users", allUsers);
        }
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
