/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jovidic.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.jovidic.konfiguracije.Konfiguracija;
import org.foi.nwtis.jovidic.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.jovidic.rest.klijenti.WeatherBugKlijent;
import org.foi.nwtis.jovidic.podaci.Adresa;
import org.foi.nwtis.jovidic.podaci.WeatherData;
import org.foi.nwtis.jovidic.slusaci.SlusacAplikacije;

/**
 * Klasa u kojoj je pozadinska dretva koja za zadani interval prikuplja sve
 * adrese iz baze podataka te sprema njihove meteo podatke u jovidic_meteo
 * tablicu u bazi podataka.
 *
 * @author jovidic
 */
public class PozadinskaDretva extends Thread {

    private BP_Konfiguracija bpkonfig;
    private String interval;
    private static boolean kraj = false;
    private static boolean pauza = false;
    private static Connection veza;

    public String sKey;
    public String cKey;

    public PozadinskaDretva(BP_Konfiguracija bpkonfig, Konfiguracija konfig) {

        this.bpkonfig = bpkonfig;
        this.sKey = konfig.dajPostavku("sKey");
        this.cKey = konfig.dajPostavku("cKey");
        this.interval = konfig.dajPostavku("interval");
    }

    public static boolean isPauza() {
        return pauza;
    }

    public static void setPauza(boolean pauza) {
        PozadinskaDretva.pauza = pauza;
    }

    @Override
    public void run() {
        while (kraj == false) {
            if (pauza == false) {

                List<Adresa> adrese = DBHelper.vratiSveAdrese(bpkonfig);

                WeatherBugKlijent wbk = new WeatherBugKlijent(cKey, sKey);

                for (Adresa adresa : adrese) {

                    System.out.println(adresa.getAdresa());
                    WeatherData wd = wbk.getRealTimeWeather(adresa.getGeoloc().getLatitude(), adresa.getGeoloc().getLongitude());
                    String adresaProvjera = adresa.getAdresa();
                    if (DBHelper.provjeriAdresuMeteo(adresaProvjera,veza) == true) {
                        DBHelper.spremiMeteoPodatke(adresa, wd, bpkonfig,veza);

                    } else if (DBHelper.provjeriAdresuMeteo(adresaProvjera,veza) == false) {

                        DBHelper.spremiMeteoPodatkePrviPut(adresa, wd, bpkonfig,veza);
                    }

                }

                try {

                    Thread.sleep(Integer.parseInt(interval) * 1000 * 60);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PozadinskaDretva.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("PAUZA JE");
                try {
                    Thread.sleep(Integer.parseInt(interval) * 1000 * 60);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PozadinskaDretva.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        try {
            veza.close();
        } catch (SQLException ex) {
            Logger.getLogger(PozadinskaDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public synchronized void start() {

        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    public static boolean isKraj() {
        return kraj;
    }

    public static void setKraj(boolean kraj) {
        PozadinskaDretva.kraj = kraj;
    }

}
