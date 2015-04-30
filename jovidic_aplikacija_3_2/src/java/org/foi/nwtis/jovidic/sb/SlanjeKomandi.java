/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jovidic.sb;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 * Zrno koje služi za slanje komandi socket serveru u prvoj aplikaciji.
 *
 * @author jovidic
 */
@Stateless
@LocalBean
public class SlanjeKomandi {

    private String odgovor;

    public String getOdgovor() {
        return odgovor;
    }

    public void setOdgovor(String odgovor) {
        this.odgovor = odgovor;
    }

    /**
     * Metoda koja se spaja na socket server i prima odgovor.
     *
     * @param request
     * @return
     */
    public String saljiKomandu(String request) {
        try {
            Socket server = new Socket("127.0.0.1", 8055);
            InputStream is = server.getInputStream();
            OutputStream os = server.getOutputStream();
            //     String command = "USER dbandic; PASSWD bandic; ADD ZIP 46000;";
            System.out.println("Naredba: " + request);
            os.write(request.getBytes());
            os.flush();
            server.shutdownOutput();

            StringBuilder response = new StringBuilder();
            while (true) {
                int n = is.read();
                if (n == -1) {
                    break;
                }
                response.append((char) n);
            }
            setOdgovor(response.toString());

            is.close();
            server.close();
            os.close();

            return response.toString();
        } catch (IOException ex) {
            Logger.getLogger(SlanjeKomandi.class.getName()).log(Level.SEVERE, null, ex);
        }
        return odgovor;

    }

}
