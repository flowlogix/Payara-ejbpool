/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.flowlogix.ejbpool;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 *
 * @author lprimak
 */
@JMSDestinationDefinition(
        name = "java:module/jms/sampleQueue",
        interfaceName = "javax.jms.Queue",
        destinationName = "Sample"
)
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:module/jms/sampleQueue"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class PooledMDB implements MessageListener {
    public PooledMDB() {
        log.fine("MDB: Constructor");
    }
    

    @Override
    public void onMessage(Message message) {
        try {
            log.fine(String.format("MDB Read: %s", message.getBody(String.class)));
//            Thread.sleep(1000);
        } catch (JMSException /* | InterruptedException */ ex) {
            log.log(Level.SEVERE, "JMS read", ex);
        }
        allBeans.put(this, Void.TYPE);
        log.fine("MDB Exit");
    }
    
    
    public static int getInvocations() {
        return allBeans.size();
    }


    private static final Map<PooledMDB, Object> allBeans = Collections.synchronizedMap(new IdentityHashMap<>());
    private static final Logger log = Logger.getLogger(PooledMDB.class.getName());
}
