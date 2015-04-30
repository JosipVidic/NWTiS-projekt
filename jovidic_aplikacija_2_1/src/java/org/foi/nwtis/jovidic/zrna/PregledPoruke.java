package org.foi.nwtis.jovidic.zrna;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.foi.nwtis.jovidic.podaci.Poruka;


/**
 * Klasa koja služi za prikaz jedne poruke.
 *
 * @author jovidic
 */
@ManagedBean
@RequestScoped
public class PregledPoruke {

    private Poruka poruka;
    private String privici = "";

    /**
     * Creates a new instance of PregledPoruke
     */
    public PregledPoruke() {
        PregledSvihPoruka psp = (PregledSvihPoruka) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pregledSvihPoruka");
        poruka = psp.getOdabranaPoruka();
    }

    public Poruka getPoruka() {
        return poruka;
    }

    public void setPoruka(Poruka poruka) {
        this.poruka = poruka;
    }

    public String natrag() {
        return "OK";
    }



}
