/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.foi.nwtis.jovidic.podaci;

import java.io.Serializable;

/**
 *Definira izgled objekta stanica.
 * @author jovidic
 */
public class Stanica implements Serializable{
    private int providerID;
    private String stationid;
    private String providername;
    private String stationname;
    private String latitude;
    private String longitude;
    private String elevationabovesealevel;    
    private String displayflag;

    public Stanica() {
    }

    public int getProviderID() {
        return providerID;
    }

    public void setProviderID(int providerID) {
        this.providerID = providerID;
    }

    public String getStationid() {
        return stationid;
    }

    public void setStationid(String stationid) {
        this.stationid = stationid;
    }

    public String getProvidername() {
        return providername;
    }

    public void setProvidername(String providername) {
        this.providername = providername;
    }

    public String getStationname() {
        return stationname;
    }

    public void setStationname(String stationname) {
        this.stationname = stationname;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getElevationabovesealevel() {
        return elevationabovesealevel;
    }

    public void setElevationabovesealevel(String elevationabovesealevel) {
        this.elevationabovesealevel = elevationabovesealevel;
    }

    public String getDisplayflag() {
        return displayflag;
    }

    public void setDisplayflag(String displayflag) {
        this.displayflag = displayflag;
    }
    
    
}
