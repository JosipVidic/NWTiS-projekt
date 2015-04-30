/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jovidic.slusaci;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.foi.nwtis.jovidic.servlet.ObradaAkcija;
import org.foi.nwtis.jovidic.konfiguracije.Konfiguracija;
import org.foi.nwtis.jovidic.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.jovidic.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.jovidic.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.jovidic.db.PozadinskaDretva;
import org.foi.nwtis.jovidic.serveri.ObradaNaredbi;
import org.foi.nwtis.jovidic.serveri.ServerZaUpravljanje;

/**
 * Slušač životnog ciklusa aplikacije koji pokreće potrebne dretve pri deployu
 * te ih gasi kod undeploya, također pristupa datotekama s postavkama
 *
 * @author jovidic
 */
public class SlusacAplikacije implements ServletContextListener {

    private PozadinskaDretva pd = null;
    private ServletContext context;
    public static Konfiguracija konfig;
    private static BP_Konfiguracija bpkonfig;
    private static ServerZaUpravljanje serverThread = null;
    private static ObradaNaredbi naredbe = null;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.context = sce.getServletContext();
        String path = this.context.getRealPath("WEB-INF") + java.io.File.separator;
        String datoteka = this.context.getInitParameter("konfiguracija");
        String datotekaKonfig = context.getInitParameter("konfig");

        bpkonfig = new BP_Konfiguracija(path + datoteka);

        try {
            konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(path + datotekaKonfig);
        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (bpkonfig == null) {
            System.out.println("Pogreška u konfiguraciji");
            return;
        }

        this.context.setAttribute("BP_Konfig", bpkonfig);
        this.context.setAttribute("konfig", konfig);

        pd = new PozadinskaDretva(bpkonfig, konfig);
        pd.start();
        serverThread = new ServerZaUpravljanje();
        serverThread.start();
        naredbe = new ObradaNaredbi();
        naredbe.start();

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        pd.setKraj(true);
        naredbe.interrupt();
        serverThread.interrupt();

    }

    public static Konfiguracija getKonfig() {
        return konfig;
    }

    public static BP_Konfiguracija getBpkonfig() {
        return bpkonfig;
    }

    public static void setBpkonfig(BP_Konfiguracija aBpkonfig) {
        bpkonfig = aBpkonfig;
    }
}
