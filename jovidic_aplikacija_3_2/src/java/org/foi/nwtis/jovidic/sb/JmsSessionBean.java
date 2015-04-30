/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jovidic.sb;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import org.foi.nwtis.jovidic.podaci.JmsPoruka;

/**
 *  Pomocna klasa za dodavanje poruka u listu.
 * @author jovidic
 */
@Stateless
public class JmsSessionBean {

    private static List<JmsPoruka> listaPoruka = new ArrayList<JmsPoruka>();
    private static JmsPoruka poruka;

    public static List<JmsPoruka> getListaPoruka() {
        return listaPoruka;
    }

    public static void setListaPoruka(List<JmsPoruka> listaPoruka) {
        JmsSessionBean.listaPoruka = listaPoruka;
    }

    public static JmsPoruka getPoruka() {
        return poruka;
    }

    public static void setPoruka(JmsPoruka poruka) {
        listaPoruka.add(poruka);
        JmsSessionBean.poruka = poruka;
    }


    public static void dodajPoruku(JmsPoruka poruka) {
        listaPoruka.add(poruka);
        System.out.println("DODAO SAM PORUKU");

    }

}
