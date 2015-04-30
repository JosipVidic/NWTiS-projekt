/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jovidic.podaci;

/**
 * Klasa koja opisuje izgled aplikacijskog dnevnika
 *
 * @author jovidic
 */
public class AppDnevnik {

    private String url;
    private String vrijemeUnosa;
    private String pocetak;
    private String kraj;
    private String vrijemeTrajanja;

    public AppDnevnik() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVrijemeUnosa() {
        return vrijemeUnosa;
    }

    public void setVrijemeUnosa(String vrijemeUnosa) {
        this.vrijemeUnosa = vrijemeUnosa;
    }

    public String getPocetak() {
        return pocetak;
    }

    public void setPocetak(String pocetak) {
        this.pocetak = pocetak;
    }

    public String getKraj() {
        return kraj;
    }

    public void setKraj(String kraj) {
        this.kraj = kraj;
    }

    public String getVrijemeTrajanja() {
        return vrijemeTrajanja;
    }

    public void setVrijemeTrajanja(String vrijemeTrajanja) {
        this.vrijemeTrajanja = vrijemeTrajanja;
    }

    
    
}
