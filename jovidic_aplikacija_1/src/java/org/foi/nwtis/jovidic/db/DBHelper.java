/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jovidic.db;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.jovidic.konfiguracije.Konfiguracija;
import org.foi.nwtis.jovidic.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.jovidic.rest.klijenti.WeatherBugKlijent;
import org.foi.nwtis.jovidic.podaci.Adresa;
import org.foi.nwtis.jovidic.podaci.AppDnevnik;
import org.foi.nwtis.jovidic.podaci.Dnevnik;
import org.foi.nwtis.jovidic.podaci.Location;
import org.foi.nwtis.jovidic.podaci.WeatherData;
import org.foi.nwtis.jovidic.rest.klijenti.GoogleMapsKlijent;
import org.foi.nwtis.jovidic.slusaci.SlusacAplikacije;

/**
 * Sadrži pomoćne metode za rad aplikacije.
 *
 * @author jovidic
 */
public class DBHelper {

    private DBHelper() {
    }

    
        /**
     * Metoda koja dohvaća sve zapise iz aplikacijskog dnevnika.
     *
     * @return
     */
    public static List<AppDnevnik> dajSveLog() {
        List<AppDnevnik> lista = new ArrayList<AppDnevnik>();
        BP_Konfiguracija konfig = SlusacAplikacije.getBpkonfig();
        String url = konfig.getServer_database() + konfig.getUser_database();
        String korisnikBP = konfig.getUser_username();
        String lozinkaBP = konfig.getUser_password();
        String driver = konfig.getDriver_database();

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        String upit = "select * from NWTIS_JOVIDIC_BP.JOVIDIC_APLIKACIJSKI_DNEVNIK";

        try {
            Connection veza = DriverManager.getConnection(url, korisnikBP, lozinkaBP);
            Statement instrukcija = veza.createStatement();
            ResultSet rs = instrukcija.executeQuery(upit);
            while (rs.next()) {
                AppDnevnik ad = new AppDnevnik();
                String urlPristupa = rs.getString("URL");
                String vrijemeUnosa = rs.getString("VRIJEME");
                String pocetak = rs.getString("POCETAK");
                String kraj = rs.getString("KRAJ");
                String vrijemeTrajanja = rs.getString("VRIJEME_TRAJANJA").toString();

                ad.setKraj(kraj);
                ad.setPocetak(pocetak);
                ad.setUrl(urlPristupa);
                ad.setVrijemeTrajanja(vrijemeTrajanja);
                ad.setVrijemeUnosa(vrijemeUnosa);
                lista.add(ad);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;

    }
    
    /**
     * Metoda koja pristupa bazi podataka i unosi podatke u aplikacijski
     * dnevnik.
     *
     * @param urlZahjeva
     * @param pocetak
     * @param kraj
     * @param trajanje
     */
    public static void unesiLog(String urlZahjeva, long pocetak, long kraj, long trajanje) {

        Date start = new Date(pocetak);
        Date end = new Date(kraj);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String oddatuma = formatter.format(start);
        String dodatuma = formatter.format(kraj);

        int i = (int) trajanje;

        BP_Konfiguracija konfig = SlusacAplikacije.getBpkonfig();
        String url = konfig.getServer_database() + konfig.getUser_database();
        String korisnikBP = konfig.getUser_username();
        String lozinkaBP = konfig.getUser_password();
        String driver = konfig.getDriver_database();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        String upit = "INSERT INTO NWTIS_JOVIDIC_BP.JOVIDIC_APLIKACIJSKI_DNEVNIK (URL, POCETAK, KRAJ, VRIJEME_TRAJANJA) VALUES ('" + urlZahjeva + "', '" + oddatuma + "', '" + dodatuma + "'," + i + ")";
        //INSERT INTO NWTIS_JOVIDIC_BP.JOVIDIC_APLIKACIJSKI_DNEVNIK (URL, POCETAK, KRAJ, VRIJEME_TRAJANJA) VALUES ('test', NULL, NULL, NULL);

        try {
            Connection veza;
            veza = DriverManager.getConnection(url, korisnikBP, lozinkaBP);
            Statement instrukcija = veza.createStatement();

            instrukcija.executeUpdate(upit);

            veza.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Metoda koja pristupa bazi podataka i unosi podatke u dnevnik koji sadrži
     * informacije o obradi naredbi.
     *
     * @param user
     * @param komanda
     */
    public static void unesiDnevnik(String user, String komanda) {
        BP_Konfiguracija konfig = SlusacAplikacije.getBpkonfig();
        String url = konfig.getServer_database() + konfig.getUser_database();
        String korisnikBP = konfig.getUser_username();
        String lozinkaBP = konfig.getUser_password();
        String driver = konfig.getDriver_database();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        String upit = "INSERT INTO NWTIS_JOVIDIC_BP.JOVIDIC_DNEVNIK (KOMANDA, KORISNIK) VALUES ('" + komanda + "', '" + user + "')";

        //INSERT INTO NWTIS_JOVIDIC_BP.JOVIDIC_DNEVNIK (KOMANDA, KORISNIK, VRIJEME) VALUES ('TEST', 'TEST', '2014-06-13 22:52:53.384');
        try {
            Connection veza;
            veza = DriverManager.getConnection(url, korisnikBP, lozinkaBP);
            Statement instrukcija = veza.createStatement();

            instrukcija.executeUpdate(upit);

            veza.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

   /**
    * Metoda koja unosi neispravne komande u bazu podataka
    * @param user
    * @param komanda 
    */
    public static void unesiDnevnikOdbacene(String user, String komanda) {
        BP_Konfiguracija konfig = SlusacAplikacije.getBpkonfig();
        String url = konfig.getServer_database() + konfig.getUser_database();
        String korisnikBP = konfig.getUser_username();
        String lozinkaBP = konfig.getUser_password();
        String driver = konfig.getDriver_database();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        String upit = "INSERT INTO NWTIS_JOVIDIC_BP.JOVIDIC_ODBACENE_KOMANDE (KOMANDA, KORISNIK) VALUES ('" + komanda + "', '" + user + "')";

        //INSERT INTO NWTIS_JOVIDIC_BP.JOVIDIC_DNEVNIK (KOMANDA, KORISNIK, VRIJEME) VALUES ('TEST', 'TEST', '2014-06-13 22:52:53.384');
        try {
            Connection veza;
            veza = DriverManager.getConnection(url, korisnikBP, lozinkaBP);
            Statement instrukcija = veza.createStatement();

            instrukcija.executeUpdate(upit);

            veza.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Metoda koja dohvaća sve komande iz baze podataka
     * @return 
     */
    public static int dajSveKomande() {
        int brojKomandi = 0;
        BP_Konfiguracija konfig = SlusacAplikacije.getBpkonfig();
        String url = konfig.getServer_database() + konfig.getUser_database();
        String korisnikBP = konfig.getUser_username();
        String lozinkaBP = konfig.getUser_password();
        String driver = konfig.getDriver_database();

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        String upit = "select count(*) from NWTIS_JOVIDIC_BP.JOVIDIC_DNEVNIK";

        try {
            Connection veza = DriverManager.getConnection(url, korisnikBP, lozinkaBP);
            Statement instrukcija = veza.createStatement();
            ResultSet rs = instrukcija.executeQuery(upit);
            while (rs.next()) {
                brojKomandi = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return brojKomandi;

    }

    /**
     * Metoda koja dohvaća sve neispravne komande iz baze podataka.
     * @return 
     */
    public static int dajSveNeispravneKomande() {
        int brojKomandi = 0;
        BP_Konfiguracija konfig = SlusacAplikacije.getBpkonfig();
        String url = konfig.getServer_database() + konfig.getUser_database();
        String korisnikBP = konfig.getUser_username();
        String lozinkaBP = konfig.getUser_password();
        String driver = konfig.getDriver_database();

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        String upit = "select count(*) from NWTIS_JOVIDIC_BP.JOVIDIC_ODBACENE_KOMANDE";

        try {
            Connection veza = DriverManager.getConnection(url, korisnikBP, lozinkaBP);
            Statement instrukcija = veza.createStatement();
            ResultSet rs = instrukcija.executeQuery(upit);
            while (rs.next()) {
                brojKomandi = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return brojKomandi;

    }

    /**
     * Metoda koja dohvaća sve podatke iz tablice dnevnik
     * @return 
     */
    public static List<Dnevnik> dajsveDnevnik() {
        BP_Konfiguracija konfig = SlusacAplikacije.getBpkonfig();
        String url = konfig.getServer_database() + konfig.getUser_database();
        String korisnikBP = konfig.getUser_username();
        String lozinkaBP = konfig.getUser_password();
        String driver = konfig.getDriver_database();

        List<Dnevnik> dnevnici = new ArrayList<Dnevnik>();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        String upit = "SELECT * FROM NWTIS_JOVIDIC_BP.JOVIDIC_DNEVNIK";

        try {
            Connection veza = DriverManager.getConnection(url, korisnikBP, lozinkaBP);
            Statement instrukcija = veza.createStatement();
            ResultSet rs = instrukcija.executeQuery(upit);
            while (rs.next()) {
                String komanda = rs.getString("KOMANDA");
                String korisnik = rs.getString("KORISNIK");
                String vrijeme = rs.getTimestamp("VRIJEME").toString();
                Dnevnik d = new Dnevnik(komanda, korisnik, vrijeme);
                dnevnici.add(d);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dnevnici;
    }

    /**
     * Otvara vezu prema bazi te nakon uspješne veze upisuje adresu u bazu
     * podataka.
     *
     * @param a
     * @param konfig
     */
    public static void spremiAdresu(Adresa a, BP_Konfiguracija konfig) {

        String url = konfig.getServer_database() + konfig.getUser_database();
        String korisnikBP = konfig.getUser_username();
        String lozinkaBP = konfig.getUser_password();
        String driver = konfig.getDriver_database();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        String adresa = a.getAdresa();
        String lat = a.getGeoloc().getLatitude();
        String lon = a.getGeoloc().getLatitude();
        String upit = "INSERT INTO NWTIS_JOVIDIC_BP.JOVIDIC_ADRESE (ADRESA, LATITUDE, LONGITUDE) VALUES ('" + adresa + "', '" + lat + "', '" + lon + "')";

        try {
            Connection veza = DriverManager.getConnection(url, korisnikBP, lozinkaBP);
            Statement instrukcija = veza.createStatement();

            instrukcija.executeUpdate(upit);

            veza.close();

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    /**
     * Metoda koja sprema novog korisnika u bazu podataka.
     * @param username
     * @param pass 
     */
    public static void spremiUsera(String username, String pass) {
        BP_Konfiguracija konfig = SlusacAplikacije.getBpkonfig();
        String url = konfig.getServer_database() + konfig.getUser_database();
        String korisnikBP = konfig.getUser_username();
        String lozinkaBP = konfig.getUser_password();
        String driver = konfig.getDriver_database();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        String email = new StringBuilder().append(username).append("@nwtis.nastava.foi").toString();

        String upit = " INSERT INTO NWTIS_JOVIDIC_BP.JOVIDIC_KORISNICI (USERNAME, EMAIL, LOZINKA) VALUES ('" + username + "', '" + email + "', '" + pass + "')";

        try {
            Connection veza = DriverManager.getConnection(url, korisnikBP, lozinkaBP);
            Statement instrukcija = veza.createStatement();

            instrukcija.executeUpdate(upit);

            veza.close();

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    /**
     * Metoda koja vraća listu svih upisanih adresa u bazu podataka.
     *
     * @param konfig
     * @return
     */
    public static List<Adresa> vratiSveAdrese(BP_Konfiguracija konfig) {
        List<Adresa> adrese = new ArrayList<Adresa>();

        String url = konfig.getServer_database() + konfig.getUser_database();
        String korisnikBP = konfig.getUser_username();
        String lozinkaBP = konfig.getUser_password();
        String driver = konfig.getDriver_database();

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        String upit = "SELECT * FROM NWTIS_JOVIDIC_BP.JOVIDIC_ADRESE";

        try {
            Connection veza = DriverManager.getConnection(url, korisnikBP, lozinkaBP);
            Statement instrukcija = veza.createStatement();
            ResultSet rs = instrukcija.executeQuery(upit);
            while (rs.next()) {
                Location l = new Location(rs.getString("LATITUDE"), rs.getString("LONGITUDE"));
                Adresa a = new Adresa(rs.getInt("IDADRESA"), rs.getString("ADRESA"), l);
                adrese.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adrese;
    }

    /**
     * Metoda koja dohvaća adresu po ID.
     * @param konfig
     * @param id
     * @return 
     */
    public static Adresa vratiAdresuID(BP_Konfiguracija konfig, int id) {
        Adresa adresaVracena;
        String url = konfig.getServer_database() + konfig.getUser_database();
        String korisnikBP = konfig.getUser_username();
        String lozinkaBP = konfig.getUser_password();
        String driver = konfig.getDriver_database();

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        String upit = "SELECT * FROM NWTIS_JOVIDIC_BP.JOVIDIC_ADRESE where idadresa=" + id + "";
        try {
            Connection veza = DriverManager.getConnection(url, korisnikBP, lozinkaBP);
            Statement instrukcija = veza.createStatement();
            ResultSet rs = instrukcija.executeQuery(upit);
            if (rs.next()) {
                adresaVracena = new Adresa(id, rs.getString("ADRESA"), new Location(rs.getString("LATITUDE"), rs.getString("LONGITUDE")));
                return adresaVracena;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Metoda koja dohvaća sve meteo podatke iz baze podataka.
     * @param konfig
     * @return 
     */
    public static List<WeatherData> dajSveMeteo(BP_Konfiguracija konfig) {
        List<WeatherData> lwd = new ArrayList<WeatherData>();

        String url = konfig.getServer_database() + konfig.getUser_database();
        String korisnikBP = konfig.getUser_username();
        String lozinkaBP = konfig.getUser_password();
        String driver = konfig.getDriver_database();

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        String upit = "SELECT * FROM NWTIS_JOVIDIC_BP.JOVIDIC_METEO";

        try {
            Connection veza = DriverManager.getConnection(url, korisnikBP, lozinkaBP);
            Statement instrukcija = veza.createStatement();
            ResultSet rs = instrukcija.executeQuery(upit);
            while (rs.next()) {

                WeatherData wd = new WeatherData();
                Float temp = rs.getFloat("TEMP");
                Float tlak = rs.getFloat("TLAK");
                Float vlaga = rs.getFloat("VLAGA");
                Float vjetar = rs.getFloat("VJETAR");
                String adresa = rs.getString("ADRESA");
                Date datum = rs.getDate("DATUM");

                wd.setAdresaMeteoPodatka(adresa);
                wd.setTemperature(temp);
                wd.setHumidity(vlaga);
                wd.setPressureSeaLevel(tlak);
                wd.setWindSpeed(vjetar);
                wd.setDatumUnosa(datum);

                lwd.add(wd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lwd;

    }

    /**
     * Metoda koja dohvaća sve meteo podatke za adresu iz baze podataka.
     * @param adr
     * @param konfig
     * @return 
     */
    public static List<WeatherData> dajSveMeteoZaAdresu(String adr, BP_Konfiguracija konfig) {
        List<WeatherData> lwd = new ArrayList<WeatherData>();

        String url = konfig.getServer_database() + konfig.getUser_database();
        String korisnikBP = konfig.getUser_username();
        String lozinkaBP = konfig.getUser_password();
        String driver = konfig.getDriver_database();

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        String upit = "SELECT * FROM NWTIS_JOVIDIC_BP.JOVIDIC_METEO WHERE ADRESA = '" + adr + "'";

        try {
            Connection veza = DriverManager.getConnection(url, korisnikBP, lozinkaBP);
            Statement instrukcija = veza.createStatement();
            ResultSet rs = instrukcija.executeQuery(upit);
            while (rs.next()) {

                WeatherData wd = new WeatherData();
                Float temp = rs.getFloat("TEMP");
                Float tlak = rs.getFloat("TLAK");
                Float vlaga = rs.getFloat("VLAGA");
                Float vjetar = rs.getFloat("VJETAR");
                String adresa = rs.getString("ADRESA");
                Date datum = rs.getDate("DATUM");

                wd.setAdresaMeteoPodatka(adresa);
                wd.setTemperature(temp);
                wd.setHumidity(vlaga);
                wd.setPressureSeaLevel(tlak);
                wd.setWindSpeed(vjetar);
                wd.setDatumUnosa(datum);

                lwd.add(wd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lwd;

    }

    /**
     * Metoda koja sprema meteo podatke u bazu podataka.
     *
     * @param a
     * @param wd
     * @param konfig
     */
    public static void spremiMeteoPodatke(Adresa a, WeatherData wd, BP_Konfiguracija konfig, Connection veza) {

        String url = konfig.getServer_database() + konfig.getUser_database();
        String korisnikBP = konfig.getUser_username();
        String lozinkaBP = konfig.getUser_password();
        String driver = konfig.getDriver_database();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        String adresa = a.getAdresa();
        float temp = 0;

        float tlak = 0;
        float vlaga = 0;
        float vjetar = 0;
        float kisa = 0;

        if (wd.getTemperature() != null) {
            temp = wd.getTemperature();
        }
        if (wd.getPressureSeaLevel() != null) {
            tlak = wd.getPressureSeaLevel();
        }
        if (wd.getHumidity() != null) {
            vlaga = wd.getHumidity();
        }
        if (wd.getWindSpeed() != null) {
            vjetar = wd.getWindSpeed();
        }
        if (wd.getRainDaily() != null) {
            kisa = wd.getRainDaily();
        }

        // String upit = "INSERT INTO NWTIS_G1.ADRESE (ADRESA, LATITUDE, LONGITUDE) VALUES ('" + adresa + "', '" + lat + "', '" + lon + "')";
        String upit = "INSERT INTO NWTIS_JOVIDIC_BP.JOVIDIC_METEO (ADRESA, TEMP, TLAK, VLAGA, VJETAR, KISA, POJAVA) VALUES ('" + adresa + "', " + temp + ", " + tlak + ", " + vlaga + ", " + vjetar + "," + kisa + ", (SELECT MAX(POJAVA)+1 FROM NWTIS_JOVIDIC_BP.JOVIDIC_METEO WHERE ADRESA ='" + adresa + "'))";
        System.out.println(upit);

        try {
            veza = DriverManager.getConnection(url, korisnikBP, lozinkaBP);
            Statement instrukcija = veza.createStatement();

            instrukcija.executeUpdate(upit);

            veza.close();

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    /**
     * Metoda koja služi za unos meteo podataka prvi puta.
     * @param a
     * @param wd
     * @param konfig
     * @param veza 
     */
    public static void spremiMeteoPodatkePrviPut(Adresa a, WeatherData wd, BP_Konfiguracija konfig, Connection veza) {

        String url = konfig.getServer_database() + konfig.getUser_database();
        String korisnikBP = konfig.getUser_username();
        String lozinkaBP = konfig.getUser_password();
        String driver = konfig.getDriver_database();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        String adresa = a.getAdresa();
        float temp = 0;

        float tlak = 0;
        float vlaga = 0;
        float vjetar = 0;
        float kisa = 0;

        if (wd.getTemperature() != null) {
            temp = wd.getTemperature();
        }
        if (wd.getPressureSeaLevel() != null) {
            tlak = wd.getPressureSeaLevel();
        }
        if (wd.getHumidity() != null) {
            vlaga = wd.getHumidity();
        }
        if (wd.getWindSpeed() != null) {
            vjetar = wd.getWindSpeed();
        }
        if (wd.getRainDaily() != null) {
            kisa = wd.getRainDaily();
        }

        // String upit = "INSERT INTO NWTIS_G1.ADRESE (ADRESA, LATITUDE, LONGITUDE) VALUES ('" + adresa + "', '" + lat + "', '" + lon + "')";
        String upit = "INSERT INTO NWTIS_JOVIDIC_BP.JOVIDIC_METEO (ADRESA, TEMP, TLAK, VLAGA, VJETAR, KISA, POJAVA) VALUES ('" + adresa + "', " + temp + ", " + tlak + ", " + vlaga + ", " + vjetar + "," + kisa + ", 1)";

        try {
            veza = DriverManager.getConnection(url, korisnikBP, lozinkaBP);
            Statement instrukcija = veza.createStatement();

            instrukcija.executeUpdate(upit);

            veza.close();

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    /**
     * Metoda koja dohvaća meteo podatke za adresu.
     * @param adresa
     * @return 
     */
    public static WeatherData dajMeteoZaAdresu(String adresa) {
        Konfiguracija konfig = SlusacAplikacije.getKonfig();

        GoogleMapsKlijent gmk = new GoogleMapsKlijent();
        Location loc = gmk.getGeoLocation(adresa);

        String cKey = konfig.dajPostavku("cKey");
        String sKey = konfig.dajPostavku("sKey");
        WeatherBugKlijent wbk = new WeatherBugKlijent(cKey, sKey);

        WeatherData wd = wbk.getRealTimeWeather(loc.getLatitude(), loc.getLongitude());
        return wd;

    }

    /**
     * Metoda koja dohvaća zadnje važeće meteo podatke za adresu.
     * @param adresa
     * @return 
     */
    public static WeatherData dajZadnjeVazece(String adresa) {
        BP_Konfiguracija bpkonfig = SlusacAplikacije.getBpkonfig();
        String url = bpkonfig.getServer_database() + bpkonfig.getUser_database();
        String korisnikBP = bpkonfig.getUser_username();
        String lozinkaBP = bpkonfig.getUser_password();

        WeatherData wd = new WeatherData();

        String upit = "SELECT * FROM NWTIS_JOVIDIC_BP.JOVIDIC_METEO WHERE adresa = '" + adresa + "'ORDER BY id DESC FETCH NEXT 1 ROWS ONLY";

        try {

            Connection veza = DriverManager.getConnection(url, korisnikBP, lozinkaBP);
            Statement instrukcija = veza.createStatement();
            ResultSet rs = instrukcija.executeQuery(upit);
            if (rs.next()) {
                wd.setTemperature(rs.getFloat("TEMP"));
                wd.setPressureSeaLevel(rs.getFloat("TLAK"));
                wd.setHumidity(rs.getFloat("VLAGA"));
                wd.setWindSpeed(rs.getFloat("VJETAR"));
                wd.setRainDaily(rs.getFloat("KISA"));
            }

            veza.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wd;

    }

    /**
     * Metoda koja dohvaća određen broj meteo podataka ovisno o parametru
     * @param top
     * @param adresa
     * @return 
     */
    public static List<WeatherData> dajNmeteo(String top, String adresa) {
        BP_Konfiguracija bpkonfig = SlusacAplikacije.getBpkonfig();
        String url = bpkonfig.getServer_database() + bpkonfig.getUser_database();
        String korisnikBP = bpkonfig.getUser_username();
        String lozinkaBP = bpkonfig.getUser_password();

        int broj = Integer.parseInt(top);
        List<WeatherData> lwd = new ArrayList<WeatherData>();

        String upit = "SELECT * FROM NWTIS_JOVIDIC_BP.JOVIDIC_METEO WHERE adresa = '" + adresa + "'ORDER BY vrijeme DESC FETCH NEXT " + top + " ROWS ONLY";
        //SELECT * FROM NWTIS_JOVIDIC_BP.JOVIDIC_METEO WHERE adresa = 'Varaždin, Pavlinska 2' ORDER BY vrijeme DESC FETCH NEXT 5 ROWS ONLY

        try {

            Connection veza = DriverManager.getConnection(url, korisnikBP, lozinkaBP);
            Statement instrukcija = veza.createStatement();
            ResultSet rs = instrukcija.executeQuery(upit);

            while (rs.next()) {
                WeatherData wd = new WeatherData();
                Float temp = rs.getFloat("TEMP");
                Float tlak = rs.getFloat("TLAK");
                Float vlaga = rs.getFloat("VLAGA");
                Float vjetar = rs.getFloat("VJETAR");
                Float kisa = rs.getFloat("KISA");

                wd.setTemperature(temp);
                wd.setHumidity(vlaga);
                wd.setPressureSeaLevel(tlak);
                wd.setWindSpeed(vjetar);
                wd.setRainDaily(kisa);

                lwd.add(wd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lwd;

    }
/**
 * Metoda koja dohvaća adrese rangirane po broju pojava u bazi podataka.
 * @param top
 * @return 
 */
    public static List<String> dajRang(String top) {
        BP_Konfiguracija bpkonfig = SlusacAplikacije.getBpkonfig();

        List<String> adrese = new ArrayList<String>();

        String url = bpkonfig.getServer_database() + bpkonfig.getUser_database();
        String korisnikBP = bpkonfig.getUser_username();
        String lozinkaBP = bpkonfig.getUser_password();

        int broj = Integer.parseInt(top);

        String upit = "SELECT ADRESA FROM NWTIS_JOVIDIC_BP.JOVIDIC_METEO ORDER BY POJAVA DESC FETCH NEXT " + top + " ROWS ONLY";
        //DISTINCT

        try {

            Connection veza = DriverManager.getConnection(url, korisnikBP, lozinkaBP);
            Statement instrukcija = veza.createStatement();
            ResultSet rs = instrukcija.executeQuery(upit);

            while (rs.next()) {
                //Location l = new Location(, rs.getString("LONGITUDE"));
                String a = new String(rs.getString("ADRESA"));
                adrese.add(a);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adrese;
    }
/**
 * Metoda koja provjerava postojanje adrese u bazi podataka.
 * @param adresa
 * @return 
 */
    public static boolean provjeriAdresu(String adresa) {

        BP_Konfiguracija bpkonfig = SlusacAplikacije.getBpkonfig();
        String url = bpkonfig.getServer_database() + bpkonfig.getUser_database();
        String korisnikBP = bpkonfig.getUser_username();
        String lozinkaBP = bpkonfig.getUser_password();

        String upit = "select * from NWTIS_JOVIDIC_BP.JOVIDIC_ADRESE WHERE ADRESA = '" + adresa + "'";

        //select * from NWTIS_JOVIDIC_BP.JOVIDIC_ADRESE WHERE ADRESA = 'Varaždin, Pavlinska ';
        try {
            Connection veza = DriverManager.getConnection(url, korisnikBP, lozinkaBP);
            Statement instrukcija = veza.createStatement();
            ResultSet rs = instrukcija.executeQuery(upit);

            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
/**
 * Metoda koja provjerava postojanje adrese u tablici meteo podataka.
 * @param adresa
 * @param veza
 * @return 
 */
    public static boolean provjeriAdresuMeteo(String adresa, Connection veza) {

        BP_Konfiguracija bpkonfig = SlusacAplikacije.getBpkonfig();
        String url = bpkonfig.getServer_database() + bpkonfig.getUser_database();
        String korisnikBP = bpkonfig.getUser_username();
        String lozinkaBP = bpkonfig.getUser_password();

        String upit = "select * from NWTIS_JOVIDIC_BP.JOVIDIC_METEO WHERE ADRESA = '" + adresa + "'";

        //select * from NWTIS_JOVIDIC_BP.JOVIDIC_ADRESE WHERE ADRESA = 'Varaždin, Pavlinska ';
        try {
            veza = DriverManager.getConnection(url, korisnikBP, lozinkaBP);
            Statement instrukcija = veza.createStatement();
            ResultSet rs = instrukcija.executeQuery(upit);

            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
/**
 * Metoda koja provjerava postojanje korisnika u bazi podataka.
 * @param username
 * @param password
 * @return 
 */
    public static boolean provjeriKorisnika(String username, String password) {

        BP_Konfiguracija bpkonfig = SlusacAplikacije.getBpkonfig();
        String url = bpkonfig.getServer_database() + bpkonfig.getUser_database();
        String korisnikBP = bpkonfig.getUser_username();
        String lozinkaBP = bpkonfig.getUser_password();

        String upit = "SELECT username FROM NWTIS_JOVIDIC_BP.JOVIDIC_KORISNICI WHERE username='" + username + "' AND lozinka= '" + password + "'";

        try {
            Connection veza = DriverManager.getConnection(url, korisnikBP, lozinkaBP);
            Statement instrukcija = veza.createStatement();
            ResultSet rs = instrukcija.executeQuery(upit);

            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
/**
 * Metoda koja dohvaća meteo podatke između datuma.
 * @param odDatuma
 * @param doDatuma
 * @return 
 */
    public static List<WeatherData> dajMeteoIzmeđuDatuma(String odDatuma, String doDatuma) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        List<WeatherData> meteoPodaci = new ArrayList<WeatherData>();

        BP_Konfiguracija bpkonfig = SlusacAplikacije.getBpkonfig();

        String url = bpkonfig.getServer_database() + bpkonfig.getUser_database();
        String korisnikBP = bpkonfig.getUser_username();
        String lozinkaBP = bpkonfig.getUser_password();

        String upit = " SELECT * FROM NWTIS_JOVIDIC_BP.JOVIDIC_METEO WHERE DATUM BETWEEN '" + odDatuma + "' AND '" + doDatuma + "' ";
        System.out.println("UPIT ZA DATUM" + upit);

        try {

            Connection veza = DriverManager.getConnection(url, korisnikBP, lozinkaBP);
            Statement instrukcija = veza.createStatement();
            ResultSet rs = instrukcija.executeQuery(upit);

            while (rs.next()) {
                WeatherData wd = new WeatherData();
                Float temp = rs.getFloat("TEMP");
                Float tlak = rs.getFloat("TLAK");
                Float vlaga = rs.getFloat("VLAGA");
                Float vjetar = rs.getFloat("VJETAR");
                Float kisa = rs.getFloat("KISA");
                String adresa = rs.getString("ADRESA");
                Date datum = rs.getDate("DATUM");

                wd.setDatumUnosa(datum);
                wd.setAdresaMeteoPodatka(adresa);
                wd.setTemperature(temp);
                wd.setHumidity(vlaga);
                wd.setPressureSeaLevel(tlak);
                wd.setWindSpeed(vjetar);
                wd.setRainDaily(kisa);

                meteoPodaci.add(wd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return meteoPodaci;

    }
    
    
     public static  List<WeatherData> dajMeteoIzmeduTimestamp(String odDatuma, String doDatuma){
     
        // Timestamp od = java.sql.Timestamp.valueOf(odDatuma);
        //Timestamp ddo = java.sql.Timestamp.valueOf(doDatuma);

        //odDatuma = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(od);
        //doDatuma = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ddo);

        List<WeatherData> meteoPodaci = new ArrayList<WeatherData>();

        BP_Konfiguracija bpkonfig = SlusacAplikacije.getBpkonfig();

        String url = bpkonfig.getServer_database() + bpkonfig.getUser_database();
        String korisnikBP = bpkonfig.getUser_username();
        String lozinkaBP = bpkonfig.getUser_password();

        String upit = " SELECT * FROM NWTIS_JOVIDIC_BP.JOVIDIC_METEO WHERE VRIJEME BETWEEN '" + odDatuma + "' AND '" + doDatuma + "'";
        System.out.println("UPIT ZA DATUM" + upit);

        try {

            Connection veza = DriverManager.getConnection(url, korisnikBP, lozinkaBP);
            Statement instrukcija = veza.createStatement();
            ResultSet rs = instrukcija.executeQuery(upit);

            while (rs.next()) {
                WeatherData wd = new WeatherData();
                Float temp = rs.getFloat("TEMP");
                Float tlak = rs.getFloat("TLAK");
                Float vlaga = rs.getFloat("VLAGA");
                Float vjetar = rs.getFloat("VJETAR");
                Float kisa = rs.getFloat("KISA");
                String adresaMeteo = rs.getString("ADRESA");
                Date datum = rs.getDate("DATUM");

                wd.setTemperature(temp);
                wd.setHumidity(vlaga);
                wd.setPressureSeaLevel(tlak);
                wd.setWindSpeed(vjetar);
                wd.setRainDaily(kisa);
                wd.setAdresaMeteoPodatka(adresaMeteo);
                wd.setDatumUnosa(datum);

                meteoPodaci.add(wd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return meteoPodaci;
     
     }

}
