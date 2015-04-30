/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jovidic.mdb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.foi.nwtis.jovidic.podaci.JmsPoruka;
import org.foi.nwtis.jovidic.sb.JmsSessionBean;
import org.foi.nwtis.jovidic.websocket.JmsEndopoint;

/**
 * Message driven bean koji prima JMS poruke iz reda cekanja.
 * @author jovidic
 */
@MessageDriven(mappedName = "jms/redCekanja", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class JmsPrimatelj implements MessageListener {


    public JmsPrimatelj() {
    }

    @Override
    public void onMessage(Message message) {

        ObjectMessage objectPoruka = null;

        if (message instanceof ObjectMessage) {
            try {
                objectPoruka = (ObjectMessage) message;

                if (message != null) {
                    JmsPoruka poruka = (JmsPoruka) objectPoruka.getObject();
                    JmsSessionBean.setPoruka(poruka);
                    System.out.println("JMS primljena");
                    JmsEndopoint.obavijestiPromjenu();
                    
                } else {
                    System.out.println("JMS JE PRAZAN");
                }
            } catch (JMSException ex) {
                System.out.println("PROBLEM KOD DESERIJALIZACIJE");
                ex.printStackTrace();
            }
        }
       
    }

}
