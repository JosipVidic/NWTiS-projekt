/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jovidic.slusaci;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.nwtis.jovidic.konfiguracije.Konfiguracija;
import org.foi.nwtis.jovidic.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.jovidic.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.jovidic.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.jovidic.mail.MailDretva;

/**
 * Slušač životnog ciklusa aplikacije u kojem se dohvaćaju potrebni podaci za
 * rad sustava.
 *
 *
 * @author jovidic
 */
@WebListener()
public class SlusacAplikacije implements ServletContextListener {

    private ServletContext context;
    public static Konfiguracija konfig;
    private static BP_Konfiguracija bpkonfig;
    private static MailDretva md;

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

        md = new MailDretva(konfig);
        md.start();
    }

    /**
     *
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("DESTROY");

        md.interrupt();
    }

    public static Konfiguracija getKonfig() {
        return konfig;
    }

    public static BP_Konfiguracija getBpkonfig() {
        return bpkonfig;
    }

}
