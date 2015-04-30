/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.nwtis.jovidic.ejb.sb;

import java.util.List;
import javax.ejb.Stateless;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.foi.nwtis.jovidic.ejb.eb.JovidicKorisnici;

/**
 *
 * @author jovidic
 */
@Stateless
public class JovidicKorisniciFacade extends AbstractFacade<JovidicKorisnici> {
    @PersistenceContext(unitName = "jovidic_aplikacija_2_2PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public JovidicKorisniciFacade() {
        super(JovidicKorisnici.class);
    }
    
    /**
     * Metoda za dohvat korisnika iz baze podataka
     * @param name
     * @return
     * @throws NamingException 
     */
      public JovidicKorisnici dajKorisnika(String name) throws NamingException {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<JovidicKorisnici> user = cq.from(JovidicKorisniciFacade.class);
        cq.select(user);
        cq.where(cb.equal(user.get("username"), name));
        return (JovidicKorisnici) em.createQuery(cq).getResultList().get(0);
    }
/**
 * Metoda za provjeru korisnika u bp
 * @param username
 * @param password
 * @return 
 */
    public List<JovidicKorisnici> provjeriKorisnika(String username, String password) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<JovidicKorisnici> users = cq.from(JovidicKorisnici.class);
        cq.select(users);
        cq.where(cb.and(users.<String>get("username").in(username), users.<String>get("lozinka").in(password)));
        return em.createQuery(cq).getResultList();
    }
    
    
    /**
     * Metoda za provjeru korisnika u bp.
     * @param email
     * @param password
     * @return 
     */
    
      public List<JovidicKorisnici> provjeriKorisnikaEmail(String email, String password) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<JovidicKorisnici> users = cq.from(JovidicKorisnici.class);
        cq.select(users);
        cq.where(cb.and(users.<String>get("email").in(email), users.<String>get("lozinka").in(password)));
        return em.createQuery(cq).getResultList();
    }
/**
 * Metoda za unos novog korisnika u bp
 * @param username
 * @param password
 * @return 
 */
    public JovidicKorisnici unesiKorisnika(String username, String password) {
        JovidicKorisnici u = new JovidicKorisnici();
        u.setUsername(username);
        u.setLozinka(password);
        String email = username+"@nwtis.nastava.foi.hr";
        u.setEmail(email);
        u.setAdmin(2);
        em.persist(u);
        return u;
    }
    
}
