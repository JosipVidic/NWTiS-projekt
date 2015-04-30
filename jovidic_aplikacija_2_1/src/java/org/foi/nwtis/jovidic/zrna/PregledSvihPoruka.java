/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jovidic.zrna;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.servlet.ServletContext;
import org.foi.nwtis.jovidic.konfiguracije.Konfiguracija;
import org.foi.nwtis.jovidic.podaci.Poruka;


/**
 * Klasa koja prikazuje sve poruke u dolaznoj mapi za prijavljenog korisnika.
 *
 * @author jovidic
 */
@ManagedBean
@SessionScoped
public class PregledSvihPoruka {

    private String email_posluzitelj;
    private String korisnicko_ime;
    private String lozinka;
    private List<Poruka> poruke;
    private Map<String, String> mape;
    private String odabranaMapa;
    private Poruka odabranaPoruka;

    private String porukaID;
    private Poruka poruka;

    private Konfiguracija konfig;

    Session session;
    Store store;
    Folder folder;
    Message[] messages;

    private int velicinaStranice;
    private int brojStranica;
    private int trenutnaStranica;
    private int indeksPocetni;
    private int indeksKrajnji;

    /**
     * Creates a new instance of PregledSvihPoruka
     */
    public PregledSvihPoruka() {

        EmailPovezivanje ep = (EmailPovezivanje) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("emailPovezivanje");
        if (ep == null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("emailPostavke.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(PregledSvihPoruka.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        email_posluzitelj = ep.getEmailPosluzitelj();
        korisnicko_ime = ep.getKorisnickoIme();
        lozinka = ep.getLozinka();
        this.trenutnaStranica = 1;

        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) context.getExternalContext().getContext();
        this.konfig = (Konfiguracija) sc.getAttribute("konfig");
        this.odabranaMapa = konfig.dajPostavku("pocetnaMapa");
        this.velicinaStranice = Integer.parseInt(konfig.dajPostavku("velicinaStranice"));

        poveziNaServer();

        try {
            preuzmiPoruke();
        } catch (MessagingException | IOException ex) {
            Logger.getLogger(PregledSvihPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<Poruka> getPoruke() {

        return poruke;
    }

    public void setPoruke(List<Poruka> poruke) {
        this.poruke = poruke;
    }

    public String getOdabranaMapa() {
        return odabranaMapa;
    }

    public void setOdabranaMapa(String odabranaMapa) {
        this.odabranaMapa = odabranaMapa;
    }

    public Poruka getOdabranaPoruka() {
        return odabranaPoruka;
    }

    public void setOdabranaPoruka(Poruka odabranaPoruka) {
        this.odabranaPoruka = odabranaPoruka;
    }

    public String getEmail_posluzitelj() {
        return email_posluzitelj;
    }

    public void setEmail_posluzitelj(String email_posluzitelj) {
        this.email_posluzitelj = email_posluzitelj;
    }

    public String getKorisnicko_ime() {
        return korisnicko_ime;
    }

    public void setKorisnicko_ime(String korisnicko_ime) {
        this.korisnicko_ime = korisnicko_ime;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    /**
     * Metoda koja poziva metodu preuzmiPoruke.
     *
     * @return
     */
    public String odaberiMapu() {
        poruke.clear();
        try {
            preuzmiPoruke();
        } catch (MessagingException | IOException ex) {
            Logger.getLogger(PregledSvihPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "promjenaMape";
    }

    public String citajPoruke() {
        return "citajPoruke";
    }

    /**
     * Metoda koja preuzima prvi n broj poruka na temelju izračuna.
     *
     * @throws NoSuchProviderException
     * @throws MessagingException
     * @throws IOException
     */
    private void preuzmiPoruke() throws NoSuchProviderException, MessagingException, IOException {

        // Open the INBOX folder
        folder = store.getFolder(this.odabranaMapa);
        folder.open(Folder.READ_ONLY);

        //Ako nema poruka, završi
        if (folder.getMessageCount() == 0) {
            return;
        }

        //Postavi vrijednosti varijabli za straničenje
        //this.izracunajBrojStranice(folder);

        //Koristi drugu metodu za stranicenje       
        messages = obrniRedoslijed(folder.getMessages());

        poruke = new ArrayList<>();
        for (int i = 0; i < messages.length; ++i) {
            Message m = messages[i];

            if (m.getContent() instanceof Multipart) {
                //Poruka s attachmentom
                //TODO obrada poruke

                String sadrzaj = "";

                Multipart mp = (Multipart) messages[i].getContent();
                int brojacPriloga = 0;
                List<PrivitakPoruke> privici = new ArrayList<>();

                for (int x = 0; x < mp.getCount(); x++) {
                    BodyPart bp = mp.getBodyPart(x);

                    String disposition = bp.getDisposition();

                    if (disposition != null && (disposition.equals(BodyPart.ATTACHMENT))) {
                        brojacPriloga++;
                        //DataHandler ostavljen zbog dretve
                        DataHandler handler = bp.getDataHandler();
                        File f = new File(bp.getFileName());
                        PrivitakPoruke pp = new PrivitakPoruke(brojacPriloga, bp.getContentType(), bp.getSize(), f.getName());
                        privici.add(pp);
                    } else {
                        sadrzaj = bp.getContent().toString();
                    }
                }

                Poruka p = new Poruka(m.getHeader("Message-ID")[0],
                        m.getSentDate(), m.getFrom()[0].toString(), m.getSubject(),
                        m.getContentType(), m.getSize(), brojacPriloga, m.getFlags(),
                        privici, false, true, sadrzaj);
               

                poruke.add(p);

            } else {
                //Obicna poruka
                Poruka p = new Poruka(m.getHeader("Message-ID")[0],
                        m.getSentDate(), m.getFrom()[0].toString(), m.getSubject(),
                        m.getContentType(), m.getSize(), 0, m.getFlags(),
                        null, false, true, m.getContent().toString());
                
               

                poruke.add(p);
            }

        }

    }

    public Map<String, String> getMape() {
        return mape;
    }

    public void setMape(Map<String, String> mape) {
        this.mape = mape;
    }

    /**
     * Metoda povezuje na server.
     */
    protected void poveziNaServer() {
        // Start the session
        java.util.Properties properties = System.getProperties();
        session = Session.getInstance(properties, null);

        try {
            // Connect to the store
            store = session.getStore("imap");
            store.connect(this.email_posluzitelj, this.korisnicko_ime, this.lozinka);
            System.out.println("Host: " + this.email_posluzitelj + ", user: " +korisnicko_ime + ", pw: " + lozinka);

            folder = store.getDefaultFolder();
            Folder[] folderi = folder.list();

            mape = new HashMap<>();

            for (Folder f : folderi) {
                System.out.println(f.getName());
                mape.put(f.getName(), f.getName());
            }
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(PregledSvihPoruka.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(PregledSvihPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getPorukaID() {
        return porukaID;
    }

    public void setPorukaID(String porukaID) {
        this.porukaID = porukaID;
    }

    public Poruka getPoruka() {
        return poruka;
    }

    public void setPoruka(Poruka poruka) {
        this.poruka = poruka;
    }

    public int getBrojStranica() {
        return brojStranica;
    }

    public void setBrojStranica(int brojStranica) {
        this.brojStranica = brojStranica;
    }

    public int getTrenutnaStranica() {
        return trenutnaStranica;
    }

    public void setTrenutnaStranica(int trenutnaStranica) {
        this.trenutnaStranica = trenutnaStranica;
    }

    /**
     * Metoda pomoću koje se prosljeđuje id odabrane poruke za pregled.
     *
     * @return
     */
    public String pregledPoruke() {
        odabranaPoruka = null;
        for (Poruka p : poruke) {
            if (p.getId().equals(porukaID)) {
                odabranaPoruka = p;
                break;
            }
        }

        return "PORUKA";
    }

    /**
     * Metoda koja polje poruka presloži u obrnut redosljed radi prikaza
     * najnovije poruke na vrhu.
     *
     * @param m
     * @return
     */
    private Message[] obrniRedoslijed(Message[] m) {
        List<Message> pom = new ArrayList<>();

        for (int i = m.length - 1; i >= 0; i--) {
            pom.add(m[i]);
        }

        return pom.toArray(new Message[pom.size()]);
    }

    /**
     * Metoda koja prelazi na sljedeću stranicu.
     *
     * @return
     *//*
    public String sljedeca() {
        if (this.trenutnaStranica < this.brojStranica) {
            this.trenutnaStranica++;
            //Osvjezi poruke
            this.poruke.clear();
            try {
                this.preuzmiPoruke();
            } catch (MessagingException | IOException ex) {
                Logger.getLogger(PregledSvihPoruka.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return "sljedeca";
    }*/

    /**
     * Metoda koja prelazi na prethodnu stranicu.
     *
     * @return
     */
    /*
    public String prethodna() {
        if (this.trenutnaStranica > 1) {
            this.trenutnaStranica--;
            //Osvjezi poruke
            this.poruke.clear();
            try {
                this.preuzmiPoruke();
            } catch (MessagingException | IOException ex) {
                Logger.getLogger(PregledSvihPoruka.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return "prethodna";
    }*/

    /**
     * Metoda koja izračunava broj stranica temeljeno na veličini stranice iz
     * konfiguracijske datoteke.
     *
     * @param f
     * @throws MessagingException
     */
    /*
    private void izracunajBrojStranice(Folder f) throws MessagingException {
        //Indeksi poruka započinju od 1, završavaju na f.getMessageCount()
        int brojPoruka = f.getMessageCount();
        if (brojPoruka % this.velicinaStranice == 0) {
            this.brojStranica = (brojPoruka / this.velicinaStranice);
        } else {
            this.brojStranica = (brojPoruka / this.velicinaStranice) + 1;
        }

        this.indeksPocetni = brojPoruka - (velicinaStranice * trenutnaStranica) + 1;
        //Provjeri ispravnost pocetnog indeksa
        if (this.indeksPocetni < 1) {
            indeksPocetni = 1;
        } else if (this.indeksPocetni > brojPoruka) {
            this.indeksPocetni = brojPoruka;
        }

        this.indeksKrajnji = brojPoruka - (velicinaStranice * (trenutnaStranica - 1));
        //Provjeri ispravnost krajnjeg indeksa
        if (this.indeksKrajnji < 1) {
            indeksKrajnji = 1;
        } else if (this.indeksKrajnji > brojPoruka) {
            this.indeksKrajnji = brojPoruka;
        }
    }*/
}
