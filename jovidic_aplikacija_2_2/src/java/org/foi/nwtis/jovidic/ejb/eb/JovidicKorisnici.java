/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.nwtis.jovidic.ejb.eb;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *  klasa koja imitira pristup bp
 * @author jovidic
 */
@Entity
@Table(name = "JOVIDIC_KORISNICI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JovidicKorisnici.findAll", query = "SELECT j FROM JovidicKorisnici j"),
    @NamedQuery(name = "JovidicKorisnici.findById", query = "SELECT j FROM JovidicKorisnici j WHERE j.id = :id"),
    @NamedQuery(name = "JovidicKorisnici.findByUsername", query = "SELECT j FROM JovidicKorisnici j WHERE j.username = :username"),
    @NamedQuery(name = "JovidicKorisnici.findByEmail", query = "SELECT j FROM JovidicKorisnici j WHERE j.email = :email"),
    @NamedQuery(name = "JovidicKorisnici.findByLozinka", query = "SELECT j FROM JovidicKorisnici j WHERE j.lozinka = :lozinka")})
public class JovidicKorisnici implements Serializable {
    @Column(name = "ADMIN")
    private Integer admin;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "USERNAME")
    private String username;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 70)
    @Column(name = "EMAIL")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "LOZINKA")
    private String lozinka;

    public JovidicKorisnici() {
    }

    public JovidicKorisnici(Integer id) {
        this.id = id;
    }

    public JovidicKorisnici(Integer id, String username, String email, String lozinka) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.lozinka = lozinka;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JovidicKorisnici)) {
            return false;
        }
        JovidicKorisnici other = (JovidicKorisnici) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.foi.nwtis.jovidic.ejb.eb.JovidicKorisnici[ id=" + id + " ]";
    }

    public Integer getAdmin() {
        return admin;
    }

    public void setAdmin(Integer admin) {
        this.admin = admin;
    }
    
}
