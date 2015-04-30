/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.nwtis.jovidic.klijent;

/**
 *  Klijent za web servis iz aplikacije 1
 * @author jovidic
 */
public class MeteoKlijent {

    public static WeatherData dajVazeceMeteoPodatkeZaAdresu(java.lang.String adresa) {
        org.foi.nwtis.jovidic.klijent.GeoMeteoWS_Service service = new org.foi.nwtis.jovidic.klijent.GeoMeteoWS_Service();
        org.foi.nwtis.jovidic.klijent.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajVazeceMeteoPodatkeZaAdresu(adresa);
    }

    public static java.util.List<org.foi.nwtis.jovidic.klijent.WeatherData> dajSveMeteoPodatkeZaAdresu(java.lang.String adresa) {
        org.foi.nwtis.jovidic.klijent.GeoMeteoWS_Service service = new org.foi.nwtis.jovidic.klijent.GeoMeteoWS_Service();
        org.foi.nwtis.jovidic.klijent.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajSveMeteoPodatkeZaAdresu(adresa);
    }

    public static java.util.List<org.foi.nwtis.jovidic.klijent.Adresa> dajSveAdrese() {
        org.foi.nwtis.jovidic.klijent.GeoMeteoWS_Service service = new org.foi.nwtis.jovidic.klijent.GeoMeteoWS_Service();
        org.foi.nwtis.jovidic.klijent.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajSveAdrese();
    }

    public static java.util.List<org.foi.nwtis.jovidic.klijent.WeatherData> dajSveMeteo() {
        org.foi.nwtis.jovidic.klijent.GeoMeteoWS_Service service = new org.foi.nwtis.jovidic.klijent.GeoMeteoWS_Service();
        org.foi.nwtis.jovidic.klijent.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajSveMeteo();
    }

    public static WeatherData dajVazeceMeteoPodatkeZaAdresu_1(java.lang.String adresa) {
        org.foi.nwtis.jovidic.klijent.GeoMeteoWS_Service service = new org.foi.nwtis.jovidic.klijent.GeoMeteoWS_Service();
        org.foi.nwtis.jovidic.klijent.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajVazeceMeteoPodatkeZaAdresu(adresa);
    }

    public static WeatherData dajZadnjeVazecePodatkeZaAdresu(java.lang.String adresa) {
        org.foi.nwtis.jovidic.klijent.GeoMeteoWS_Service service = new org.foi.nwtis.jovidic.klijent.GeoMeteoWS_Service();
        org.foi.nwtis.jovidic.klijent.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajZadnjeVazecePodatkeZaAdresu(adresa);
    }
    

    
    
    
    
    
    
    
    
}
