/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jovidic.servisi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.foi.nwtis.jovidic.db.DBHelper;
import org.foi.nwtis.jovidic.konfiguracije.bp.BP_Konfiguracija;

import org.foi.nwtis.jovidic.podaci.Adresa;
import org.foi.nwtis.jovidic.podaci.WeatherData;
import org.foi.nwtis.jovidic.slusaci.SlusacAplikacije;

/**
 * Web servis koji služi za dohvat svih adresa, dohvat važećih meteo podataka za
 * adresu te dohvat zadnjih upisanih meteo podataka za adresu.
 *
 * @author jovidic
 */
@WebService(serviceName = "GeoMeteoWS")
public class GeoMeteoWS {

    /**
     * Operacija web servisa koja dohvaća sve adrese u obliku liste. Web service
     * operation
     */
    @WebMethod(operationName = "dajSveAdrese")
    public List<Adresa> dajSveAdrese() {
        BP_Konfiguracija bpkonfig = SlusacAplikacije.getBpkonfig();

        List<Adresa> adrese = DBHelper.vratiSveAdrese(bpkonfig);

        return adrese;

    }

    /**
     *
     * Operacija web servisa koja dohvaća važeće meteo podatke za upisanu
     * adresu. Web service operation
     */
    @WebMethod(operationName = "dajVazeceMeteoPodatkeZaAdresu")
    public WeatherData dajVazeceMeteoPodatkeZaAdresu(@WebParam(name = "adresa") final String adresa) {

        if (adresa != null && adresa.length() > 0) {
            WeatherData wd = new WeatherData();
            return wd = DBHelper.dajMeteoZaAdresu(adresa);
        }

        return null;
    }

    /**
     *
     * Web operacija koja dohvaća zadnje važeće podatke za upisanu adresu. Web
     * service operation
     */
    @WebMethod(operationName = "dajZadnjeVazecePodatkeZaAdresu")
    public WeatherData dajZadnjeVazecePodatkeZaAdresu(@WebParam(name = "adresa") String adresa) {

        WeatherData wd = DBHelper.dajZadnjeVazece(adresa);
        return wd;
    }

    /**
     * Web operacija koja na temelju adrese dohvaća sve meteo podatke za adresu.
     */
    @WebMethod(operationName = "dajSveMeteoPodatkeZaAdresu")
    public List<WeatherData> dajSveMeteoPodatkeZaAdresu(@WebParam(name = "adresa") String adresa) {

        List<WeatherData> lwd = new ArrayList<WeatherData>();

        BP_Konfiguracija bpkonfig = SlusacAplikacije.getBpkonfig();

        lwd = DBHelper.dajSveMeteoZaAdresu(adresa, bpkonfig);
        return lwd;

    }

    /**
     * Operacija web servisa koji pruža prvih N zapisa za zadanu adresu
     * sortirano prema datumu unosa.
     */
    @WebMethod(operationName = "dajNmeteoZaAdresu")
    public List<WeatherData> dajNmeteoZaAdresu(@WebParam(name = "top") String top, @WebParam(name = "adresa") String adresa) {
        List<WeatherData> lwd = DBHelper.dajNmeteo(top, adresa);
        return lwd;
    }

    /**
     * Operacija web servisa koja na temelju zadanog parametra top daje listu
     * stringova adresa s najviše pojava u tablici JOVIDIC_METEO
     */
    @WebMethod(operationName = "dajRangAdresa")
    public List<String> dajRangAdresa(@WebParam(name = "top") String top) {

        List<String> ls = DBHelper.dajRang(top);
        return ls;

    }

    /**
     * Operacija web servisa koja na temelju parametara adresa, od datuma i do
     * datuma vrši upit nad bazom podataka i dohvaća željene podatke. Web
     * service operation
     */
    @WebMethod(operationName = "dajMeteoIzmeduDatuma")
    public List<WeatherData> dajMeteoIzmeduDatuma(@WebParam(name = "odDatuma") String odDatuma, @WebParam(name = "doDatuma") String doDatuma) {
        List<WeatherData> meteoPodaci = DBHelper.dajMeteoIzmeduTimestamp(odDatuma, doDatuma);
                return meteoPodaci;

    }

    /**
     * Operacija web servisa koja pruža provjeru postojanja adrese Web service
     * operation
     */
    @WebMethod(operationName = "provjeriAdresu")
    public Boolean provjeriAdresu(@WebParam(name = "adresa") String adresa) {
        if (DBHelper.provjeriAdresu(adresa) == true) {
            return true;
        } else {

            return false;
        }

    }

    /**
     * Operacija web servisa koja pruža sve meteo podatke iz baze podataka
     */
    @WebMethod(operationName = "dajSveMeteo")
    public List<WeatherData> dajSveMeteo() {
        List<WeatherData> lwd = new ArrayList<WeatherData>();

        BP_Konfiguracija bpkonfig = SlusacAplikacije.getBpkonfig();

        lwd = DBHelper.dajSveMeteo(bpkonfig);
        return lwd;
    }

}
