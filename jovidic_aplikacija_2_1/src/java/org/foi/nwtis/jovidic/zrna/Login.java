/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jovidic.zrna;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.jovidic.ejb.eb.JovidicKorisnici;
import org.foi.nwtis.jovidic.ejb.sb.JovidicKorisniciFacade;

/**
 *  Zrno koje služi za prijavu korisnika u privatni dio sustava
 * @author jovidic
 */
@ManagedBean
@SessionScoped
public class Login {

    @EJB
    private JovidicKorisniciFacade jovidicKorisniciFacade;

    private HttpSession session;
    private FacesContext context;
    private boolean auth = true;
    private List<String> users = new ArrayList<String>();

    private String korIme;
    private String lozinka;

    /**
     * Creates a new instance of Login
     */
    public Login() {
        
          context = FacesContext.getCurrentInstance();
        session = (HttpSession) (context.getExternalContext().getSession(true));
    }

    public String getKorIme() {
        return korIme;
    }

    public void setKorIme(String korIme) {
        this.korIme = korIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String loginUser() {
        List<JovidicKorisnici> listUsers = jovidicKorisniciFacade.provjeriKorisnika(korIme, lozinka);
        boolean check = false;
        for (JovidicKorisnici d : listUsers) {
            if (d.getAdmin() == 1) {
                session.setAttribute("admin", 1);
                users.add(d.getUsername());
                session.setAttribute("users", users);
                session.setAttribute("user", d.getUsername());
                check = true;
                break;
            } else if (d.getAdmin() == 2) {
                check = true;
                users.add(d.getUsername());
                session.setAttribute("users", users);
                session.setAttribute("user", d.getUsername());
                break;
            }
        }

        if (check) {
            return "OK";
        } else {
            return "NOT";
        }
    }
}
