package org.foi.nwtis.jovidic.zrna;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.foi.nwtis.jovidic.klijent.Adresa;
import org.foi.nwtis.jovidic.klijent.MeteoKlijent;
import org.foi.nwtis.jovidic.klijent.WeatherData;
import sun.awt.windows.WDataTransferer;

/**
 *
 *  Zrno koje služi za prikaz javnih podataka korisnicima
 * @author jovidic
 */
@ManagedBean(name = "public")
@ViewScoped
public class PublicPrikaz implements Serializable {

   
    private static List<WeatherData> sviMeteo = MeteoKlijent.dajSveMeteo();
    private static List<WeatherData> vazeciMeteo = new ArrayList<WeatherData>();
    private static List<Adresa> adrese = new ArrayList<Adresa>();
    private String adresa;
    List<WeatherData> a = new ArrayList<WeatherData>();
    private List<WeatherData> meteoZaAdresu = new ArrayList<WeatherData>();
    List<WeatherData> filtriranje;

    /**
     * Creates a new instance of Public
     */
    public PublicPrikaz() {
        this.meteoZaAdresu = meteoZaAdresu;
    }


    public List<WeatherData> getFiltriranje() {
        return filtriranje;
    }

    public void setFiltriranje(List<WeatherData> filtriranje) {
        this.filtriranje = filtriranje;
    }

    public static List<WeatherData> getVazeciMeteo() {
        return vazeciMeteo;
    }

    public static void setVazeciMeteo(List<WeatherData> vazeciMeteo) {
        PublicPrikaz.vazeciMeteo = vazeciMeteo;
    }

    
    public List<WeatherData> getA() {
        return a;
    }

    public void setA(List<WeatherData> a) {
        this.a = a;
    }

    public List<WeatherData> getSviMeteo() {
        return sviMeteo;
    }

    public void setSviMeteo(List<WeatherData> sviMeteo) {
        this.sviMeteo = MeteoKlijent.dajSveMeteo();
    }

    public List<WeatherData> getMeteoZaAdresu() {
        return meteoZaAdresu = MeteoKlijent.dajSveMeteoPodatkeZaAdresu(adresa);
    }

    public void setMeteoZaAdresu(List<WeatherData> meteoZaAdresu) {
        this.meteoZaAdresu = meteoZaAdresu;
    }

    public List<Adresa> getAdrese() {
        return adrese = MeteoKlijent.dajSveAdrese();
    }

    public void setAdrese(List<Adresa> adrese) {
        this.adrese = adrese;
    }

    public String getAdresa() {
        return adresa;
    }
    


    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public List<WeatherData> prikazMeteo() {
        // String sinj = "SINJ";
        List<WeatherData> wd = new ArrayList<WeatherData>();
        sviMeteo = MeteoKlijent.dajSveMeteo();
        wd.addAll(sviMeteo);
        return wd;
    }

    public List<Adresa> dajAdrese() {

        List<Adresa> a = new ArrayList<Adresa>();
        adrese = MeteoKlijent.dajSveAdrese();
        a.addAll(adrese);
        return a;
    }


    public List<WeatherData> dajMeteoAdresi() {

        meteoZaAdresu = MeteoKlijent.dajSveMeteoPodatkeZaAdresu(adresa);
       
        return meteoZaAdresu;
    }
    
    public List<WeatherData> dajSveMeteo(){
    
    
        sviMeteo = MeteoKlijent.dajSveMeteo();
        a.addAll(sviMeteo);
        return a;
    }
    
    public List<WeatherData> dajZadnjeVazece(){
        List<Adresa> lista = getAdrese();
        for(Adresa a : lista){
            WeatherData wd = MeteoKlijent.dajZadnjeVazecePodatkeZaAdresu(a.getAdresa());
            wd.setAdresaMeteoPodatka(a.getAdresa());
            vazeciMeteo.add(wd);
            
        }
        return  vazeciMeteo;
    
    }
    
}
