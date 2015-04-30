/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jovidic.serveri;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.jovidic.konfiguracije.Konfiguracija;
import org.foi.nwtis.jovidic.slusaci.SlusacAplikacije;

/**
 * Klasa u kojoj se kreira socket server za upravljanje te temeljeno na
 * zahtjevima kreira dretvu obrade naredbi.
 *
 * @author jovidic
 */
public class ServerZaUpravljanje extends Thread {

    private ServerSocket serverSocket;
    private Integer port;
    private boolean kraj = false;

    public ServerZaUpravljanje() {
    }

    @Override
    public synchronized void start() {
        Konfiguracija konf = SlusacAplikacije.getKonfig();
        port = Integer.parseInt(konf.dajPostavku("port"));
        super.start();
    }

    @Override
    public void run() {

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("SERVER POKRENUT NA PORTU " + port);
            ObradaNaredbi thread = null;

            long start = 0;
            while (!serverSocket.isClosed()) {
                Socket socket;
                try {
                    socket = serverSocket.accept();
                    thread = new ObradaNaredbi(socket, start);
                    thread.start();
                } catch (SocketException e) {
                    System.out.println("GASENJE SERVERA !");
                }
                start = System.currentTimeMillis();
            }
            thread.interrupt();
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerZaUpravljanje.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void interrupt() {

        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerZaUpravljanje.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

}
