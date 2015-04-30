/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jovidic.zrna;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.foi.nwtis.jovidic.podaci.JmsPoruka;
import org.foi.nwtis.jovidic.sb.JmsSessionBean;

/**
 *  Pomoćna klasa koja služi za pregled JMS poruka
 * @author jovidic
 */
@ManagedBean
@SessionScoped
public class PregledPoruka {

    @EJB
    private JmsSessionBean jmsSessionBean;
    private List<JmsPoruka> listaPoruka = new ArrayList<JmsPoruka>();

    public PregledPoruka() {
    }

    public List<JmsPoruka> getListaPoruka() {
        listaPoruka = JmsSessionBean.getListaPoruka();
        return listaPoruka;
    }

    public void setListaPoruka(List<JmsPoruka> listaPoruka) {
        this.listaPoruka = listaPoruka;
    }

}
