package org.foi.nwtis.jovidic.podaci;

import java.io.Serializable;

/**
 *
 *
 * @author jovidic
 */
public class JmsPoruka implements Serializable {
     private static final long serialVersionUID = 1L;
    private String pocetak;
    private String kraj;
    private int svePoruke;
    private int ispravnePoruke;
    private int neispravnePoruke;
    private int preuzetePoruke;


    public JmsPoruka(String pocetak, String kraj, int svePoruke, int ispravnePoruke, int neispravnePoruke, int preuzetePoruke) {
        this.pocetak = pocetak;
        this.kraj = kraj;
        this.svePoruke = svePoruke;
        this.ispravnePoruke = ispravnePoruke;
        this.neispravnePoruke = neispravnePoruke;
        this.preuzetePoruke = preuzetePoruke;
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


    public int getSvePoruke() {
        return svePoruke;
    }

    public void setSvePoruke(int svePoruke) {
        this.svePoruke = svePoruke;
    }

    public int getIspravnePoruke() {
        return ispravnePoruke;
    }

    public void setIspravnePoruke(int ispravnePoruke) {
        this.ispravnePoruke = ispravnePoruke;
    }

    public int getNeispravnePoruke() {
        return neispravnePoruke;
    }

    public void setNeispravnePoruke(int neispravnePoruke) {
        this.neispravnePoruke = neispravnePoruke;
    }

    public int getPreuzetePoruke() {
        return preuzetePoruke;
    }

    public void setPreuzetePoruke(int preuzetePoruke) {
        this.preuzetePoruke = preuzetePoruke;
    }

}
