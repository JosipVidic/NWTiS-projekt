/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jovidic.rest.klijenti;

import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONObject;
import org.foi.nwtis.jovidic.podaci.Location;

/**
 * Klasa koja pristupa Google Maps API te preuzima longitude i latitude iz JSON
 * objekta.
 *
 * @author jovidic
 */
public class GoogleMapsKlijent {

    GMRESTHelper helper;
    Client client;

    public GoogleMapsKlijent() {
        client = ClientBuilder.newClient();

    }

    /**
     * Metoda koja dobavlja longitude i latitude za željenu adresu sa Google
     * Maps API.
     *
     * @param adresa
     * @return
     */
    public Location getGeoLocation(String adresa) {
        try {
            WebTarget webResource = client.target(GMRESTHelper.getGM_BASE_URI()).path("/maps/api/geocode/json");

            webResource = webResource.queryParam("address", URLEncoder.encode(adresa, "UTF-8"));
            webResource = webResource.queryParam("sensor", "false");

            String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);

            JSONObject jo = new JSONObject(odgovor);
            JSONObject obj = jo.getJSONArray("results")
                    .getJSONObject(0)
                    .getJSONObject("geometry")
                    .getJSONObject("location");
            Location loc = new Location(obj.getString("lat"), obj.getString("lng"));

            return loc;

        } catch (Exception ex) {
            Logger.getLogger(GoogleMapsKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
