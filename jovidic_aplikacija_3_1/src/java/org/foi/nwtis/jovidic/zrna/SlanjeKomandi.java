/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jovidic.zrna;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Zrno koje služi za odabir vrste komandi za slanje.
 *
 * @author jovidic
 */
@ManagedBean
@SessionScoped
public class SlanjeKomandi {

    @EJB
    private org.foi.nwtis.jovidic.sb.SlanjeKomandi slanjeKomandi;

    private boolean responseFlag = false;
    private boolean getAdresaFlag = false;
    private boolean getAdresaLoginFlag = false;
    private boolean addUserFlag = false;
    private boolean addAdresaFlag = false;
    private boolean pauseFlag = false;
    private boolean startFlag = false;
    private boolean stopFlag = false;
    private boolean neispravnaFlag = false;
    private boolean testFlag = false;
    private Map<String, String> komande;
    private String odabranaKomanda = null;
    private String response = null;
   private String request;

    private String addUser = "USER xxxx; PASSWD xxxx; ADD xxxx; NEWPASSWD xxxx;";
    private String getAdresa = "USER xxxx; GET xxxx;";
    private String getAdresawLogin = "USER xxxx; PASSWD xxxx; GET xxxx;";
    private String addAdresa = "USER xxxx; PASSWD xxxx; ADD xxxx;";
    private String neispravna = "PROIZVOLJNA KOMANDA";
    private String pause = "USER xxxx; PASSWD xxxx; PAUSE;";
    private String start = "USER xxxx; PASSWD xxxx; START;";
    private String stop = "USER xxxx; PASSWD xxxx; STOP;";
    private String test = "USER xxxx; PASSWD xxxx; TEST xxxx;";

    private String korisnikAddUser;
    private String lozinkaAddUser;
    private String noviAddUser;
    private String novaAddUser;
    private String korisnik;
    private String adresa;

    public SlanjeKomandi() {
    }

    public void odaberiKomandu() {
        responseFlag = false;
        if (odabranaKomanda.equals(addUser)) {
            getAdresaFlag = false;
            getAdresaLoginFlag = false;
            addAdresaFlag = false;
            addUserFlag = true;
            pauseFlag = false;
            startFlag = false;
            stopFlag = false;
            neispravnaFlag = false;
            testFlag = false;

        } else if (odabranaKomanda.equals(addAdresa)) {
            getAdresaFlag = false;
            getAdresaLoginFlag = false;
            addAdresaFlag = true;
            addUserFlag = false;
            pauseFlag = false;
            startFlag = false;
            stopFlag = false;
            neispravnaFlag = false;
            testFlag = false;

        } else if (odabranaKomanda.equals(getAdresa)) {
            getAdresaFlag = true;
            getAdresaLoginFlag = false;
            addAdresaFlag = false;
            addUserFlag = false;
            pauseFlag = false;
            startFlag = false;
            stopFlag = false;
            neispravnaFlag = false;
            testFlag = false;

        } else if (odabranaKomanda.equals(getAdresawLogin)) {
            getAdresaFlag = false;
            getAdresaLoginFlag = true;
            addAdresaFlag = false;
            addUserFlag = false;
            pauseFlag = false;
            startFlag = false;
            stopFlag = false;
            neispravnaFlag = false;
            testFlag = false;

        } else if (odabranaKomanda.equals(pause)) {
            getAdresaFlag = false;
            getAdresaLoginFlag = false;
            addAdresaFlag = false;
            addUserFlag = false;
            pauseFlag = true;
            startFlag = false;
            stopFlag = false;
            neispravnaFlag = false;
            testFlag = false;

        } else if (odabranaKomanda.equals(start)) {
            getAdresaFlag = false;
            getAdresaLoginFlag = false;
            addAdresaFlag = false;
            addUserFlag = false;
            pauseFlag = false;
            startFlag = true;
            stopFlag = false;
            neispravnaFlag = false;
            testFlag = false;

        } else if (odabranaKomanda.equals(stop)) {
            getAdresaFlag = false;
            getAdresaLoginFlag = false;
            addAdresaFlag = false;
            addUserFlag = false;
            pauseFlag = false;
            startFlag = false;
            stopFlag = true;
            neispravnaFlag = false;
            testFlag = false;

        } else if (odabranaKomanda.equals(test)) {
            getAdresaFlag = false;
            getAdresaLoginFlag = false;
            addAdresaFlag = false;
            addUserFlag = false;
            pauseFlag = false;
            startFlag = false;
            stopFlag = false;
            neispravnaFlag = false;
            testFlag = true;

        } else if (odabranaKomanda.equals(neispravna)) {
            getAdresaFlag = false;
            getAdresaLoginFlag = false;
            addAdresaFlag = false;
            addUserFlag = false;
            pauseFlag = false;
            startFlag = false;
            stopFlag = false;
            neispravnaFlag = true;
            testFlag = false;

        }
    }

    public void clearFlags() {

        getAdresaFlag = false;
        getAdresaLoginFlag = false;
        addAdresaFlag = false;
        addUserFlag = false;
        pauseFlag = false;
        startFlag = false;
        stopFlag = false;
        neispravnaFlag = false;
        testFlag = false;

    }

    public void ocistiVarijable() {
    korisnikAddUser = null;
    lozinkaAddUser = null;
    noviAddUser = null;
    novaAddUser = null;
    korisnik = null;
    adresa = null;
}

public void odaberiSlanje() {

        if (odabranaKomanda.equals(addUser)) {
            clearFlags();
            request = "USER " + korisnikAddUser + "; PASSWD " + lozinkaAddUser + ";" + " ADD " + noviAddUser + "; NEWPASSWD " + novaAddUser + ";";
            System.out.println("POSLAO SAM KOMANDU: " +request);
            response = slanjeKomandi.saljiKomandu(request);           
            responseFlag = true;
            ocistiVarijable();

        } else if (odabranaKomanda.equals(addAdresa)) {
            clearFlags();
            request = "USER " + korisnikAddUser + "; PASSWD " + lozinkaAddUser + "; ADD " + adresa + ";";
            System.out.println("POSLAO SAM KOMANDU: " +request);
            response = slanjeKomandi.saljiKomandu(request);            
            responseFlag = true;
            ocistiVarijable();

        } else if (odabranaKomanda.equals(getAdresa)) {
            clearFlags();
            request = "USER " + korisnik + "; GET " + adresa + ";";
            response = slanjeKomandi.saljiKomandu(request);
            responseFlag = true;
            ocistiVarijable();

        } else if (odabranaKomanda.equals(getAdresawLogin)) {
            clearFlags();
            request = "USER " + korisnikAddUser + "; PASSWD " + lozinkaAddUser + "; GET " + adresa + ";";
            System.out.println("POSLAO SAM KOMANDU: " +request);
            response = slanjeKomandi.saljiKomandu(request);
            responseFlag = true;
            ocistiVarijable();
        }else if(odabranaKomanda.equals(pause)){
            clearFlags();
            request = "USER " + korisnikAddUser + "; PASSWD " + lozinkaAddUser + "; PAUSE;";
            System.out.println("POSLAO SAM KOMANDU: " +request);
            response = slanjeKomandi.saljiKomandu(request);
            responseFlag = true;
            ocistiVarijable();
        
        }else if(odabranaKomanda.equals(start)){
             clearFlags();
            request = "USER " + korisnikAddUser + "; PASSWD " + lozinkaAddUser + "; START;";
            System.out.println("POSLAO SAM KOMANDU: " +request);
            response = slanjeKomandi.saljiKomandu(request);
            responseFlag = true;
            ocistiVarijable();
        
        }else if(odabranaKomanda.equals(stop)){
             clearFlags();
            request = "USER " + korisnikAddUser + "; PASSWD " + lozinkaAddUser + "; STOP;";
            System.out.println("POSLAO SAM KOMANDU: " +request);
            response = slanjeKomandi.saljiKomandu(request);
            responseFlag = true;
            ocistiVarijable();
        
        }else if(odabranaKomanda.equals(test)){
            clearFlags();
            request = "USER " + korisnikAddUser + "; PASSWD " + lozinkaAddUser + "; TEST " + adresa + ";";
            System.out.println("POSLAO SAM KOMANDU: " +request);
            response = slanjeKomandi.saljiKomandu(request);
            responseFlag = true;
            ocistiVarijable();
        
        }else if(odabranaKomanda.equals(neispravna)){
            clearFlags();
            request = korisnikAddUser;
            response = slanjeKomandi.saljiKomandu(request);
            System.out.println("POSLAO SAM KOMANDU: " +request);
            responseFlag = true;
            ocistiVarijable();
        
        }

    }

    public org.foi.nwtis.jovidic.sb.SlanjeKomandi getSlanjeKomandi() {
        return slanjeKomandi;
    }

    public void setSlanjeKomandi(org.foi.nwtis.jovidic.sb.SlanjeKomandi slanjeKomandi) {
        this.slanjeKomandi = slanjeKomandi;
    }

    public boolean isPauseFlag() {
        return pauseFlag;
    }

    public void setPauseFlag(boolean pauseFlag) {
        this.pauseFlag = pauseFlag;
    }

    public boolean isStartFlag() {
        return startFlag;
    }

    public void setStartFlag(boolean startFlag) {
        this.startFlag = startFlag;
    }

    public boolean isStopFlag() {
        return stopFlag;
    }

    public void setStopFlag(boolean stopFlag) {
        this.stopFlag = stopFlag;
    }

    public boolean isNeispravnaFlag() {
        return neispravnaFlag;
    }

    public void setNeispravnaFlag(boolean neispravnaFlag) {
        this.neispravnaFlag = neispravnaFlag;
    }

    public boolean isTestFlag() {
        return testFlag;
    }

    public void setTestFlag(boolean testFlag) {
        this.testFlag = testFlag;
    }

    public String getNeispravna() {
        return neispravna;
    }

    public void setNeispravna(String neispravna) {
        this.neispravna = neispravna;
    }

    public String getPause() {
        return pause;
    }

    public void setPause(String pause) {
        this.pause = pause;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public boolean isGetAdresaFlag() {
        return getAdresaFlag;
    }

    public String getKorisnikAddUser() {
        return korisnikAddUser;
    }

    public void setKorisnikAddUser(String korisnikAddUser) {
        this.korisnikAddUser = korisnikAddUser;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getLozinkaAddUser() {
        return lozinkaAddUser;
    }

    public void setLozinkaAddUser(String lozinkaAddUser) {
        this.lozinkaAddUser = lozinkaAddUser;
    }

    public String getNoviAddUser() {
        return noviAddUser;
    }

    public void setNoviAddUser(String noviAddUser) {
        this.noviAddUser = noviAddUser;
    }

    public String getNovaAddUser() {
        return novaAddUser;
    }

    public void setNovaAddUser(String novaAddUser) {
        this.novaAddUser = novaAddUser;
    }

    public void setGetAdresaFlag(boolean getAdresaFlag) {
        this.getAdresaFlag = getAdresaFlag;
    }

    public boolean isGetAdresaLoginFlag() {
        return getAdresaLoginFlag;
    }

    public void setGetAdresaLoginFlag(boolean getAdresaLoginFlag) {
        this.getAdresaLoginFlag = getAdresaLoginFlag;
    }

    public String getOdabranaKomanda() {
        return odabranaKomanda;
    }

    public void setOdabranaKomanda(String odabranaKomanda) {
        this.odabranaKomanda = odabranaKomanda;
    }

    public String getAddUser() {
        return addUser;
    }

    public void setAddUser(String addUser) {
        this.addUser = addUser;
    }

    public String getGetAdresa() {
        return getAdresa;
    }

    public void setGetAdresa(String getAdresa) {
        this.getAdresa = getAdresa;
    }

    public String getGetAdresawLogin() {
        return getAdresawLogin;
    }

    public void setGetAdresawLogin(String getAdresawLogin) {
        this.getAdresawLogin = getAdresawLogin;
    }

    public String getAddAdresa() {
        return addAdresa;
    }

    public void setAddAdresa(String addAdresa) {
        this.addAdresa = addAdresa;
    }

    public Map<String, String> getKomande() {

        return komande;
    }

    public void setKomande(Map<String, String> komande) {

        this.komande = komande;
    }

    public boolean isAddUserFlag() {
        return addUserFlag;
    }

    public void setAddUserFlag(boolean addUserFlag) {
        this.addUserFlag = addUserFlag;
    }

    public boolean isAddAdresaFlag() {
        return addAdresaFlag;
    }

    public void setAddAdresaFlag(boolean addAdresaFlag) {
        this.addAdresaFlag = addAdresaFlag;
    }

    public boolean isResponseFlag() {
        return responseFlag;
    }

    public void setResponseFlag(boolean responseFlag) {
        this.responseFlag = responseFlag;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
