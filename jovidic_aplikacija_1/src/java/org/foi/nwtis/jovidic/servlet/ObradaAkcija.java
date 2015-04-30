/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jovidic.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.jovidic.db.DBHelper;
import org.foi.nwtis.jovidic.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.jovidic.rest.klijenti.GoogleMapsKlijent;
import org.foi.nwtis.jovidic.rest.klijenti.WeatherBugKlijent;
import org.foi.nwtis.jovidic.podaci.Adresa;
import org.foi.nwtis.jovidic.podaci.AppDnevnik;
import org.foi.nwtis.jovidic.podaci.Dnevnik;
import org.foi.nwtis.jovidic.podaci.Location;
import org.foi.nwtis.jovidic.podaci.WeatherData;
import org.foi.nwtis.jovidic.slusaci.SlusacAplikacije;

/**
 * Servlet koji obrađuje akcije ovisno o željama korisnika te ih preusmjeruje na
 * željene destinacije s potrebnim podacima za prikaz
 *
 * @author jovidic
 */
public class ObradaAkcija extends HttpServlet {

    /**
     *
     * Klasa koja procesuira zahtjeve za GET i POST te na temelju odabrane
     * akcije u indekx.jsp ispisuje potrebne podatke.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();

        String req = request.getServletPath();
        String destination = "";

        if (req.endsWith("/MeteoData")) {

            String odabir = request.getParameter("odabir");

            if (odabir == null) {
                odabir = "10";
            }

            String adresaFilter = request.getParameter("adresaFilter");
            String datumOd = request.getParameter("datumOd");
            String datumDo = request.getParameter("datumDo");
            List<WeatherData> lwd = dajMeteo();
            List<WeatherData> list = new ArrayList<WeatherData>();
            List<WeatherData> lista = new ArrayList<WeatherData>();

            if (adresaFilter != null) {
                list.clear();
                for (WeatherData wd : lwd) {
                    if (wd.getAdresaMeteoPodatka().startsWith(adresaFilter)) {
                        list.add(wd);
                    }
                }
            } else {
                list.addAll(lwd);
            }

            if (datumOd != null && datumDo != null && !datumOd.equals("") && !datumDo.equals("")) {
                datumOd = datumOd+" 00:00:00.000";
                datumDo = datumDo+" 00:00:00.000";
                list = DBHelper.dajMeteoIzmeduTimestamp(datumOd, datumDo);
                //list = DBHelper.dajMeteoIzmeđuDatuma(datumOd, datumDo);
                datumDo = null;
                datumOd = null;
                for (WeatherData wd : list) {
                    if (wd.getAdresaMeteoPodatka().startsWith(adresaFilter)) {
                        lista.add(wd);
                    }
                }
            } else {
                lista.addAll(list);
            }

            session.setAttribute("odabir", odabir);
            session.setAttribute("datumOd", datumOd);
            session.setAttribute("datumDo", datumDo);
            session.setAttribute("adresaFilter", adresaFilter);
            session.setAttribute("list", lista);
            destination = "/meteoData.jsp";

        } else if (req.endsWith("/SaveData")) {

            //String adr = new String(request.getParameter("adresaUnos").getBytes("iso-8859-1"), "UTF-8");
            String adr = request.getParameter("adresaUnos");
            if (adr != null) {

                boolean provjera = DBHelper.provjeriAdresu(adr);
                if (provjera == false) {
                    Adresa adresaUnos = dohvatiGeoPodatke(adr);
                    spremiPodatkeBaza(adr, request);
                    session.setAttribute("spremljeno", adresaUnos);
                }

                destination = "/saveData.jsp";

            } else {

                destination = "/saveData.jsp";
            }

        } else if (req.endsWith("/AdrData")) {

            List<Adresa> la = dajSveAdrese();
            List<Adresa> adrese = new ArrayList<Adresa>();

            adrese.addAll(la);

            session.setAttribute("adrese", adrese);
            destination = "/adreseData.jsp";

        } else if (req.endsWith("/NowData")) {
            //String adr = new String(request.getParameter("adresaNow").getBytes("iso-8859-1"), "UTF-8");
            String adr = request.getParameter("adresaNow");
            if (adr != null) {
                WeatherData wd = this.dohvatiMeteoPodatke(adr);
                session.setAttribute("vazeci", wd);
                destination = "/vazeciData.jsp";
            } else {
                destination = "/vazeciData.jsp";
            }

        } else if (req.endsWith("/LogData")) {
            List<Dnevnik> dnevnik = new ArrayList<Dnevnik>();
            dnevnik = DBHelper.dajsveDnevnik();
            System.out.println("DNEVNIK" + dnevnik + "ide na " + destination);

            session.setAttribute("dnevnik", dnevnik);

            destination = "/dnevnik.jsp";

        } else if (req.endsWith("/GeoData")) {

            //String adr = new String(request.getParameter("adresaGeo").getBytes("iso-8859-1"), "UTF-8");
            String adr = request.getParameter("adresaGeo");

            if (adr != null) {
                Adresa geo = dohvatiGeoPodatke(adr);
                session.setAttribute("geo", geo);
                destination = "/geoData.jsp";
            } else {
                destination = "/geoData.jsp";
            }

        }else if(req.endsWith("/AppData")){
             List<AppDnevnik> log = new ArrayList<AppDnevnik>();
            log = DBHelper.dajSveLog();

            session.setAttribute("log", log);

            destination = "/appDnevnik.jsp";
        }

        RequestDispatcher rd = getServletContext().getRequestDispatcher(destination);

        rd.forward(request, response);

    }

    /**
     * Metoda koja služi za dohvaćanje geolokacijskih podataka.
     *
     * @param adresa
     * @return
     */
    private Adresa dohvatiGeoPodatke(String adresa) {
        GoogleMapsKlijent gmk = new GoogleMapsKlijent();
        Location loc = gmk.getGeoLocation(adresa);
        return new Adresa(0, adresa, loc);
    }

    /**
     * Metoda koja dohvaća potrebne parametre te poziva metodu za spremanje u
     * bazu podataka.
     *
     * @param adresa
     * @param request
     */
    private void spremiPodatkeBaza(String adresa, HttpServletRequest request) {
        Adresa a = dohvatiGeoPodatke(adresa);
        BP_Konfiguracija konfig = SlusacAplikacije.getBpkonfig();
        DBHelper.spremiAdresu(a, konfig);
    }

    /**
     * Metoda koja dohvaa potrebne parametre te dohvaća meteo podatke za zadanu
     * adresu preko poziva metode za dohvat.
     *
     * @param adresa
     * @return
     */
    private WeatherData dohvatiMeteoPodatke(String adresa) {
        Adresa a = dohvatiGeoPodatke(adresa);

        String cKey = SlusacAplikacije.konfig.dajPostavku("cKey");
        String sKey = SlusacAplikacije.konfig.dajPostavku("sKey");
        //String cKey = "H1o8KrNH1csjLDYXWyj3AqOT34JsyQGm";
        //String sKey = "m9QNpUXSIAlZQZRl";
        WeatherBugKlijent wbk = new WeatherBugKlijent(cKey, sKey);

        WeatherData wd = wbk.getRealTimeWeather(a.getGeoloc().getLatitude(), a.getGeoloc().getLongitude());

        return wd;
    }

    private List<Adresa> dajSveAdrese() {
        BP_Konfiguracija konfig = SlusacAplikacije.getBpkonfig();
        List<Adresa> sveAdrese = DBHelper.vratiSveAdrese(konfig);
        return sveAdrese;
    }

    private List<WeatherData> dajMeteo() {
        BP_Konfiguracija konfig = SlusacAplikacije.getBpkonfig();
        List<WeatherData> lwd = DBHelper.dajSveMeteo(konfig);
        return lwd;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
