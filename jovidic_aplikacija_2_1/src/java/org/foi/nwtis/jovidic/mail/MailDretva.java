/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jovidic.mail;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.foi.nwtis.jovidic.konfiguracije.Konfiguracija;
import org.foi.nwtis.jovidic.podaci.JmsPoruka;

/**
 * Dretva koja čita poruke iz pretinca te ih sortira u odgovarajuće lokacije
 * zadane konfiguracijom te šalje jms poruke na kraju svakog ciklusa
 *
 * @author jovidic
 */
public class MailDretva extends Thread {

    private Konfiguracija konfig;
    private int interval;
    private static String defaultFolder;
    private String emailHost;
    private String emailPort;
    private String username;
    private String password;
    private String subject;
    private String ispravan;
    private String neispravan;
    private static List<String> popisFoldera = new ArrayList<String>();

    public MailDretva(Konfiguracija konfig) {
        super();

        this.konfig = konfig;
        this.interval = Integer.parseInt(konfig.dajPostavku("interval"));
        emailHost = konfig.dajPostavku("emailPosluzitelj");
        username = konfig.dajPostavku("korisnickoIme");
        password = konfig.dajPostavku("korisnickaLozinka");
        emailPort = konfig.dajPostavku("email_port");
        subject = konfig.dajPostavku("email_subject_nwtis");
        ispravan = konfig.dajPostavku("email_folder_nwtis");
        neispravan = konfig.dajPostavku("email_folder_other");
    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        Session session = null;
        Store store = null;
        Folder folder = null;
        Message message = null;
        Message[] messages = null;
        Object messagecontentObject = null;
        String sender = null;
        String subject = null;
        Multipart multipart = null;
        Part part = null;
        String contentType = null;
        int svePoruke;
        int brojPoruka;
        int ispravnePoruke;
        int neispravnePoruke;
        int prezetePoruke;

        while (!isInterrupted()) {
            try {

                svePoruke = 0;
                brojPoruka = 0;
                ispravnePoruke = 0;
                neispravnePoruke = 0;
                prezetePoruke = 0;
                long start = System.currentTimeMillis();
                Date startDate = new Date(start);
                session = Session.getDefaultInstance(System.getProperties(), null);

                store = session.getStore("imap");

                store.connect(emailHost, username, password);

                folder = store.getDefaultFolder();
                folder = folder.getFolder("inbox");
                folder.open(Folder.READ_WRITE);
                messages = folder.getMessages();

                for (int messageNumber = 0; messageNumber < messages.length; messageNumber++) {
                    svePoruke++;
                    brojPoruka++;
                    message = messages[messageNumber];

                    String messageType = message.getContentType();

                    //todo u if && (messageType.startsWith("TEXT/PLAIN") || messageType.startsWith("text/plain"))
                    if (message.getSubject().startsWith(this.subject)) {

                        messagecontentObject = message.getContent();
                        String content = messagecontentObject.toString();

                        prebaciPoruku(store, folder, message, ispravan);
                        ispravnePoruke++;
                        System.out.println("ISPRAVNA PORUKA PREBACENA");
                    } else {
                        prebaciPoruku(store, folder, message, neispravan);
                        neispravnePoruke++;
                        System.out.println("NEISPRAVNA PORUKA PREBACENA");
                    }
                    prezetePoruke = ispravnePoruke + neispravnePoruke;
                }
                Folder[] folders = store.getDefaultFolder().list();

                popisFoldera = new ArrayList<String>();
                for (Folder f : folders) {
                    popisFoldera.add(f.getName());
                }

                defaultFolder = store.getDefaultFolder().getName();
                folder.close(true);
                store.close();
                long end = System.currentTimeMillis();
                Date stopDate = new Date(end);

                String pocetak = startDate.toString();
                String kraj = stopDate.toString();

                if (prezetePoruke > 0) {
                    saljiJMS(pocetak, kraj, svePoruke, ispravnePoruke, neispravnePoruke, prezetePoruke);
                    System.out.println("Spavam : " + (interval * 1000) + " i saljem JMS poruku.");
                }

                try {
                    System.out.println("Spavam : " + (interval * 1000));
                    Thread.sleep(interval * 1000);

                } catch (InterruptedException ex) {
                    break;
                }

            } catch (NoSuchProviderException ex) {
                Logger.getLogger(MailDretva.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MessagingException ex) {
                Logger.getLogger(MailDretva.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MailDretva.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private void prebaciPoruku(Store store, Folder folder, Message poruka, String naziv) throws MessagingException {
        System.out.println("prebacujem poruke");
        Folder noviFolder = store.getFolder(naziv);
        if (!noviFolder.exists()) {
            noviFolder.create(Folder.HOLDS_MESSAGES);
        }
        noviFolder.open(Folder.READ_WRITE);
        Message[] poruke = new Message[1];
        poruke[0] = poruka;
        if (folder != null) {
            folder.copyMessages(poruke, noviFolder);
        } else {
            noviFolder.appendMessages(poruke);
        }
        noviFolder.close(false);
        poruka.setFlag(Flags.Flag.DELETED, true);
    }

    /**
     * Metoda koja definira izgled jms poruke te poziva metodu za slanje jms
     * poruke s potrebnim parametrom.
     *
     * @param pocetak
     * @param kraj
     * @param svePoruke
     * @param ispravnePoruke
     * @param neispravnePoruke
     * @param preuzetePoruke
     */
    private void saljiJMS(String pocetak, String kraj, int svePoruke, int ispravnePoruke, int neispravnePoruke, int preuzetePoruke) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        JmsPoruka p = new JmsPoruka();
        p.setPocetak(pocetak);
        p.setKraj(kraj);
        p.setIspravnePoruke(ispravnePoruke);
        p.setNeispravnePoruke(neispravnePoruke);
        p.setPreuzetePoruke(preuzetePoruke);
        p.setSvePoruke(svePoruke);
        try {
            sendJMSMessageToRedCekanja(p);
            System.out.println("KREIRAO SAM PORUKU");
        } catch (JMSException ex) {
            Logger.getLogger(MailDretva.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Metoda koja šalje poruku u red čekanja.
     *
     * @param poruka
     * @throws JMSException
     */
    public void sendJMSMessageToRedCekanja(JmsPoruka poruka) throws JMSException {
        Connection connection = null;
        javax.jms.Session session = null;
        try {
            InitialContext ic = new InitialContext();
            ConnectionFactory cf = (ConnectionFactory) ic.lookup("jms/jmsFactory");
            Queue oQ = (Queue) ic.lookup("jms/redCekanja");
            connection = cf.createConnection();
            session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
            MessageProducer mp = session.createProducer(oQ);

            connection.start();
            ObjectMessage message = session.createObjectMessage();
            message.setObject(poruka);
            mp.send(message);
            mp.close();
            connection.close();
        } catch (NamingException ex) {
            Logger.getLogger(MailDretva.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot close session", e);
                }
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

}
