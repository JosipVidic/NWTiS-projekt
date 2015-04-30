/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jovidic.zrna;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.foi.nwtis.jovidic.ejb.sb.JovidicKorisniciFacade;

/**
 * Zrno koje služi za registraciju korisnika.
 * @author jovidic
 */
@ManagedBean
@SessionScoped
public class Registracija {

    @EJB
    private JovidicKorisniciFacade jovidicKorisniciFacade;

    private String korIme = new String();
    private String lozinka = new String();
    private String lozinkaPotvrda = new String();

    /**
     * Creates a new instance of Registracija
     */
    public Registracija() {
    }

    public String getKorIme() {
        return korIme;
    }

    public void setKorIme(String korIme) {
        this.korIme = korIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getLozinkaPotvrda() {
        return lozinkaPotvrda;
    }

    public void setLozinkaPotvrda(String lozinkaPotvrda) {
        this.lozinkaPotvrda = lozinkaPotvrda;
    }

    public String registriraj() {
        if (lozinka == null || lozinkaPotvrda == null || korIme == null || lozinka == "" || lozinkaPotvrda == "" || korIme == "" || lozinka.length() < 5 || korIme.length() < 5) {
            return "NOT";
        } else {
            if (lozinka.equals(lozinkaPotvrda)) {
                jovidicKorisniciFacade.unesiKorisnika(korIme, lozinka);
            } else {
                return "NOT";
            }
        }

        return "OK";

    }

}
