/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.nwtis.jovidic.podaci;

import java.io.Serializable;

/**
 *  Klasa koja definira izgled dnevnika
 * @author jovidic
 */
public class Dnevnik implements Serializable{
    private String komanda;
    private String korisnik;
    private String vrijeme;

    public Dnevnik(String komanda, String korisnik, String vrijeme) {
        this.komanda = komanda;
        this.korisnik = korisnik;
        this.vrijeme = vrijeme;
    }
    
    

    public String getKomanda() {
        return komanda;
    }

    public void setKomanda(String komanda) {
        this.komanda = komanda;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(String vrijeme) {
        this.vrijeme = vrijeme;
    }
}
