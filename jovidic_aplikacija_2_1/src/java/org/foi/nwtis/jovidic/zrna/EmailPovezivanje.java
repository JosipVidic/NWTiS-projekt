/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jovidic.zrna;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.foi.nwtis.jovidic.ejb.eb.JovidicKorisnici;
import org.foi.nwtis.jovidic.ejb.sb.JovidicKorisniciFacade;
import org.foi.nwtis.jovidic.konfiguracije.Konfiguracija;
import org.foi.nwtis.jovidic.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.jovidic.slusaci.SlusacAplikacije;

/**
 * Klasa sadrži podatke za povezivanje na mail server.
 *
 * @author jovidic
 */
@ManagedBean(name = "emailPovezivanje")//za dohvaćanje 
@SessionScoped
public class EmailPovezivanje implements Serializable {

    @EJB
    private JovidicKorisniciFacade jovidicKorisniciFacade;
    /**
     * Creates a new instance of EmailPovezivanje
     */
    private Konfiguracija konfig = SlusacAplikacije.getKonfig();
    private String korisnickoIme;
    private String lozinka;
    private String emailPosluzitelj;

    public EmailPovezivanje() {
       this.korisnickoIme = konfig.dajPostavku("korisnickoIme");
        this.lozinka = konfig.dajPostavku("korisnickaLozinka");
        this.emailPosluzitelj = konfig.dajPostavku("emailPosluzitelj");

    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getEmailPosluzitelj() {
        return emailPosluzitelj;
    }

    public void setEmailPosluzitelj(String emailPosluzitelj) {
        this.emailPosluzitelj = emailPosluzitelj;
    }

    public String citajPoruke() {
        List<JovidicKorisnici> listUsers = jovidicKorisniciFacade.provjeriKorisnikaEmail(korisnickoIme, lozinka);
        boolean check = false;
        for (JovidicKorisnici d : listUsers) {
            if (d.getEmail().equals(korisnickoIme) || d.getLozinka().equals(lozinka)) {
                check = true;
            } else {
                check = false;
            }

        }

        if (check) {
            return "citaj";
        } else {
            return "NOT_OK";

        }

    }
}
