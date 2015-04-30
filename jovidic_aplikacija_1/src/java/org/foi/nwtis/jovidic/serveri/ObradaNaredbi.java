package org.foi.nwtis.jovidic.serveri;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.foi.nwtis.jovidic.db.DBHelper;
import org.foi.nwtis.jovidic.db.PozadinskaDretva;
import org.foi.nwtis.jovidic.konfiguracije.Konfiguracija;
import org.foi.nwtis.jovidic.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.jovidic.podaci.Adresa;
import org.foi.nwtis.jovidic.podaci.Location;

import org.foi.nwtis.jovidic.podaci.WeatherData;
import org.foi.nwtis.jovidic.rest.klijenti.GoogleMapsKlijent;
import org.foi.nwtis.jovidic.slusaci.SlusacAplikacije;

/**
 * Dretva koja obrađuje sve korisničke zahtjeve.
 *
 * @author jovidic
 */
public class ObradaNaredbi extends Thread {

    private Socket client;
    private String username;
    private String password;
    private String host;
    private String emailPort;
    private String posiljatelj;
    private String primatelj;
    private String tema;
    private Konfiguracija konfig = null;
    private long vrijemePocetka;
    private boolean kraj = false;
    private int ispravneKomande = 0;
    private int neispravneKomande = 0;

    private long trajanjePrethodnog;

    public ObradaNaredbi() {
    }

    public ObradaNaredbi(Socket c, long lastCommand) {
        client = c;
        vrijemePocetka = vrijemePocetka;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getEmailPort() {
        return emailPort;
    }

    public void setEmailPort(String emailPort) {
        this.emailPort = emailPort;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getPosiljatelj() {
        return posiljatelj;
    }

    public String getPrimatelj() {
        return primatelj;
    }

    public String getTema() {
        return tema;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public synchronized void start() {
        konfig = SlusacAplikacije.getKonfig();
        username = konfig.dajPostavku("korisnickoIme");
        password = konfig.dajPostavku("korisnickaLozinka");
        host = konfig.dajPostavku("localhost");
        emailPort = konfig.dajPostavku("emailPort");
        posiljatelj = konfig.dajPostavku("posiljatelj");
        primatelj = konfig.dajPostavku("primatelj");
        tema = konfig.dajPostavku("tema");
        
        super.start();
        
    }

    /**
     * If sbs gets through the regex it generates some response depending on the
     * request.
     */
    @Override
    public void run() {
        

        String user = "";
        String pass = "";
        String adresa = "";
        String noviuser = "";
        String novipass = "";
        String response = "";
        

        long startMillis = System.currentTimeMillis();
        Date startRun = new Date(startMillis);
        System.out.println("START RUN : " + startRun);
        OutputStream os;

        try {

            InputStream is = client.getInputStream();
            os = client.getOutputStream();
            StringBuilder sb = new StringBuilder();
            StringBuilder command = new StringBuilder();

            while (true) {
                int n = is.read();
                if (n == -1) {
                    break;
                }
                command.append((char) n);
            }

            String getSintaksa = "^USER ([A-Za-z0-9_-]+); PASSWD ([A-Za-z0-9_-]+); GET ([A-Za-z0-9_-]+);";
            String testSintaksa = "^USER ([A-Za-z0-9_-]+); PASSWD ([A-Za-z0-9_-]+); TEST ([A-Za-z0-9_-]+);";
            String addUserSintaksa = "^USER ([A-Za-z0-9_-]+); PASSWD ([A-Za-z0-9_-]+); ADD ([A-Za-z0-9_-]+); NEWPASSWD ([A-Za-z0-9_-]+);";
            String addAdresaSintaksa = "^USER ([A-Za-z0-9_-]+); PASSWD ([A-Za-z0-9_-]+); ADD ([A-Za-z0-9_-]+);";
            String pauseSintaksa = "^USER ([A-Za-z0-9_-]+); PASSWD ([A-Za-z0-9_-]+); PAUSE;";
            String startSintaksa = "^USER ([A-Za-z0-9_-]+); PASSWD ([A-Za-z0-9_-]+); START;";
            String stopSintaksa = "^USER ([A-Za-z0-9_-]+); PASSWD ([A-Za-z0-9_-]+); STOP;";
            String userSintaksa = "^USER ([a-zA-Z]+); GET (.+);";
            sb.append(command);

            String s = sb.toString().trim();

            if (s.startsWith("USER") && s.contains(" PASSWD ") && s.contains("TEST")) {
                            Pattern pattern = Pattern.compile(testSintaksa);
                            Matcher m = pattern.matcher(s);
                            boolean status = m.matches();
                            if (status) {
                                user = m.group(1);
                                pass = m.group(2);
                                adresa = m.group(3);
                            }
                            boolean postojiUser = DBHelper.provjeriKorisnika(user, pass);
                            if(postojiUser==true){
                                                boolean postojiAdresa = DBHelper.provjeriAdresu(adresa);
                                                DBHelper.unesiDnevnik(user, s.toString());
                                                if(postojiAdresa==true){
                                                    response="OK 10";
                                                    long stop =  System.currentTimeMillis();
                                                    long trajanje = stop-startMillis;
                                                    sadrzajPoruke(trajanje, DBHelper.dajSveKomande(), DBHelper.dajSveNeispravneKomande());
                                                    
                                                }else{
                                        
                                                    response ="ERR 51";
                                                        DBHelper.unesiDnevnikOdbacene(user, s.toString());
                                                }                          
                            }else{
                                    response ="ERR 30";
                                    DBHelper.unesiDnevnikOdbacene(user, s.toString());
                            }

            } else if (s.startsWith("USER") && s.contains("PASSWD") && s.contains("GET")) {
                            Pattern pattern = Pattern.compile(getSintaksa);
                            Matcher m = pattern.matcher(s);
                            boolean status = m.matches();
                            if (status) {
                                user = m.group(1);
                                pass = m.group(2);
                                adresa = m.group(3);
                            }
                            boolean postojiAdresaGET = DBHelper.provjeriAdresu(adresa);
                            boolean postojiUser = DBHelper.provjeriKorisnika(user, pass);
                            if(postojiUser==true){
                                                DBHelper.unesiDnevnik(user, s.toString());
                                                if(postojiAdresaGET==true){
                                                    WeatherData wd = DBHelper.dajZadnjeVazece(adresa);
                                                    response = " OK 10  TEMP: " + wd.getTemperature() + "  VLAGA: " + wd.getHumidity() + " TLAK: " + wd.getPressureSeaLevel();
                                                    long stop =  System.currentTimeMillis();
                                                    long trajanje = stop-startMillis;
                                                    sadrzajPoruke(trajanje, DBHelper.dajSveKomande(), DBHelper.dajSveNeispravneKomande());
                                                }else{
                                                    response ="ERR 52";
                                                    DBHelper.unesiDnevnikOdbacene(user, s.toString());
                                                }
                            }else{
                                    response ="ERR 30";
                                    DBHelper.unesiDnevnikOdbacene(user, s.toString());
                            }
            } else if (s.startsWith("USER") && s.contains("PASSWD") && s.contains("PAUSE")) {
                            Pattern pattern = Pattern.compile(pauseSintaksa);
                            Matcher m = pattern.matcher(s);
                            boolean status = m.matches();
                            if (status) {
                                user = m.group(1);
                                pass = m.group(2);

                            }
                            boolean postojiUser = DBHelper.provjeriKorisnika(user, pass);
                            if(postojiUser==true){
                                                DBHelper.unesiDnevnik(user, s.toString());
                                                if (PozadinskaDretva.isPauza() == false) {
                                                PozadinskaDretva.setPauza(true);
                                                response = "OK 10";
                                                long stop =  System.currentTimeMillis();
                                                long trajanje = stop-startMillis;
                                                sadrzajPoruke(trajanje, DBHelper.dajSveKomande(), DBHelper.dajSveNeispravneKomande());
                                                } else {
                                                    response = "ERR 40;";
                                                    DBHelper.unesiDnevnikOdbacene(user, s.toString());
                                                }
                            }else{
                                    response ="ERR 30";
                                    DBHelper.unesiDnevnikOdbacene(user, s.toString());
                            }
            } else if (s.startsWith("USER") && s.contains("PASSWD") && s.contains("STOP")) {
                            Pattern pattern = Pattern.compile(stopSintaksa);
                            Matcher m = pattern.matcher(s);
                            boolean status = m.matches();
                            if (status) {
                                user = m.group(1);
                                pass = m.group(2);

                            }
                            boolean postojiUser = DBHelper.provjeriKorisnika(user, pass);
                            if(postojiUser==true){
                                                DBHelper.unesiDnevnik(user, s.toString());
                                            if (PozadinskaDretva.isKraj() == false) {
                                                    PozadinskaDretva.setKraj(true);
                                                    response = "OK 10";
                                                    long stop =  System.currentTimeMillis();
                                                    long trajanje = stop-vrijemePocetka;
                                                    sadrzajPoruke(trajanje, DBHelper.dajSveKomande(), DBHelper.dajSveNeispravneKomande());
                                            } else {
                                                    response = "ERR 42";
                                                   DBHelper.unesiDnevnikOdbacene(user, s.toString());
                                                }
                                    
                            }else{
                                    response ="ERR 30";
                                    DBHelper.unesiDnevnikOdbacene(user, s.toString());
                            }
            } else if (s.startsWith("USER") && s.contains("PASSWD") && s.contains("START")) {
                             Pattern pattern = Pattern.compile(startSintaksa);
                            Matcher m = pattern.matcher(s);
                            boolean status = m.matches();
                            if (status) {
                                user = m.group(1);
                                pass = m.group(2);

                            }
                            boolean postojiUser = DBHelper.provjeriKorisnika(user, pass);
                            if(postojiUser==true){
                                                DBHelper.unesiDnevnik(user, s.toString());
                                                if (PozadinskaDretva.isPauza() == true) {
                                                    PozadinskaDretva.setPauza(false);
                                                    response = "OK 10";
                                                    long stop =  System.currentTimeMillis();
                                                    long trajanje = stop-startMillis;
                                                  sadrzajPoruke(trajanje, DBHelper.dajSveKomande(), DBHelper.dajSveNeispravneKomande());
                                                } else {
                                                    response = "ERR 41;";
                                                    DBHelper.unesiDnevnikOdbacene(user, s.toString());
                                                }
                            }else{
                                    response ="ERR 30";
                                    DBHelper.unesiDnevnikOdbacene(user, s.toString());
                            }
            } else if (s.startsWith("USER") && s.contains("PASSWD") && s.contains("ADD") && s.contains("NEWPASSWD")) {
                     Pattern pattern = Pattern.compile(addUserSintaksa);
                            Matcher m = pattern.matcher(s);
                            boolean status = m.matches();
                            if (status) {
                                user = m.group(1);
                                pass = m.group(2);
                                noviuser = m.group(3);
                                novipass = m.group(4);
                            }
                            boolean postojiUser = DBHelper.provjeriKorisnika(user, pass);
                            boolean postojiNovi = DBHelper.provjeriKorisnika(noviuser, novipass);
                            if(postojiUser==true && postojiNovi==false){
                                DBHelper.unesiDnevnik(user, s.toString());
                                DBHelper.spremiUsera(noviuser, novipass);
                                response ="SPREMIO SAM KORISNIKA: "+noviuser+" s passwordom : "+novipass;
                                long stop =  System.currentTimeMillis();
                                long trajanje = stop-startMillis;
                               sadrzajPoruke(trajanje, DBHelper.dajSveKomande(), DBHelper.dajSveNeispravneKomande());
                                    
                            }else{
                                    response ="ERR 30";
                                    DBHelper.unesiDnevnikOdbacene(user, s.toString());
                                  
                            }
            } else if (s.startsWith("USER") && s.contains("PASSWD") && s.contains("ADD")) {
                            BP_Konfiguracija konfig = SlusacAplikacije.getBpkonfig();
                            Pattern pattern = Pattern.compile(addAdresaSintaksa);
                            Matcher m = pattern.matcher(s);
                            boolean status = m.matches();
                            if (status) {
                                user = m.group(1);
                                pass = m.group(2);
                                adresa=m.group(3);
                            }
                            boolean postojiAdresa = DBHelper.provjeriAdresu(adresa);
                            boolean postojiUser = DBHelper.provjeriKorisnika(user, pass);
                            if(postojiAdresa==false){
                            if(postojiUser==true){
                                DBHelper.unesiDnevnik(user, s.toString());
                                GoogleMapsKlijent gmk = new GoogleMapsKlijent();
                                Location loc = gmk.getGeoLocation(adresa);
                            
                                    Adresa a = new Adresa();
                                    a.setAdresa(adresa);
                                    a.setGeoloc(loc);
                  
                                    DBHelper.spremiAdresu(a, konfig);
                                    response=" OK 10";
                                    long stop =  System.currentTimeMillis();
                                    long trajanje = stop-startMillis;
                                    sadrzajPoruke(trajanje, DBHelper.dajSveKomande(), DBHelper.dajSveNeispravneKomande());
                                    
                            }else{
                                    response ="ERR 30";
                                    DBHelper.unesiDnevnikOdbacene(user, s.toString());
                            }
                            }else{
                                    response="ERR 50";
                                    DBHelper.unesiDnevnikOdbacene(user, s.toString());
                                   
                            }

            } else if (s.startsWith("USER") && s.contains("GET")) {
             
                Pattern pattern = Pattern.compile(userSintaksa);
                Matcher m = pattern.matcher(s);
                boolean status = m.matches();
                if (status) {
                    user = m.group(1);
                    adresa = m.group(2);

                }
                boolean postojiAdresa = DBHelper.provjeriAdresu(adresa);
                if (postojiAdresa == true) {
                    DBHelper.unesiDnevnik(user, s.toString());

                    System.out.println("UNESENO? " + user + "i naredba" + s.toString());

                    WeatherData wd = DBHelper.dajZadnjeVazece(adresa);
                    response = " OK 10  TEMP: " + wd.getTemperature() + "  VLAGA: " + wd.getHumidity() + " TLAK: " + wd.getPressureSeaLevel();
                    long stop =  System.currentTimeMillis();
                    long trajanje = stop-startMillis;


                    sadrzajPoruke(trajanje, DBHelper.dajSveKomande(), DBHelper.dajSveNeispravneKomande());
                   
                   
                } else {
                    response = "Adresa ne postoji";
                }
            } else {
                response = "ERR 00 NE POSTOJI NAREDBA";
            }
            
            os.write(response.getBytes("ISO-8859-2"));
            is.close();
            os.close();
            os.flush();
        } catch (Exception e) {
        }
        try {
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(ObradaNaredbi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void interrupt() {
        System.out.println("GASIM OBRADU NAREDBI");

        super.interrupt();
    }

    public void setKraj(boolean kraj) {
        this.kraj = kraj;
    }

    
    /**
     * Metoda koja pristupa mail serveru te šalje poruku.
     * @param cont 
     */
    public void slanjePorukeobicna(String cont) {

        try {
            String smtpHost = host;
            String from = username;
            String to = primatelj;
            String predmet = tema;
            String passw = password;
            String sender = posiljatelj;

            // Start a session
            java.util.Properties properties = System.getProperties();
            properties.setProperty("mail.smtp.host", smtpHost);
            Session session = Session.getDefaultInstance(properties);

            // Construct a message
            MimeMessage message = new MimeMessage(session);

            message.setSentDate(new Date());
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(predmet);
            message.setText(cont);

            // Connect to the transport
            Transport transport = session.getTransport("smtp");
            transport.connect(smtpHost, from, passw);

            // Send the message and close the connection
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("POSLAO SAM MAIL");
            //Spremi poruku u odlaznu mapu
            //this.spremiPoruku(session.getStore("imap"), message);
        } catch (MessagingException ex) {
            System.out.println("NE RADI SLANJE");
        }

    }

    /**
     * Metoda koja postavlja sadržaj email poruke.
     * @param vrijemeIzvršavanja
     * @param brojIspravnih
     * @param brojNeispravnih 
     */
    public void sadrzajPoruke(long vrijemeIzvršavanja, int brojIspravnih, int brojNeispravnih) {
        String sadrzaj = "";
        int ukupno = brojIspravnih+brojNeispravnih;
        sadrzaj = "Vrijeme izvrsavanja je :"+vrijemeIzvršavanja+" broj ispravnih komandi je " + brojIspravnih +" broj neispravnih komandi je: "+brojNeispravnih + "ukupan broj komandi je: "+ukupno ;
        slanjePorukeobicna(sadrzaj);

    }

    public long getTrajanjePrethodnog() {
        return trajanjePrethodnog;
    }

    public void setTrajanjePrethodnog(long trajanjePrethodnog) {
        this.trajanjePrethodnog = trajanjePrethodnog;
    }

    public int getIspravneKomande() {
        return ispravneKomande;
    }

    public void setIspravneKomande(int ispravneKomande) {
        this.ispravneKomande = ispravneKomande;
    }

    public int getNeispravneKomande() {
        return neispravneKomande;
    }

    public void setNeispravneKomande(int neispravneKomande) {
        this.neispravneKomande = neispravneKomande;
    }



}
