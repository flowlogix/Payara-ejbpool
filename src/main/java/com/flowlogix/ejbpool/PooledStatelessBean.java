/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.flowlogix.ejbpool;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author lprimak
 */
@Stateless
@LocalBean
public class PooledStatelessBean {
    public PooledStatelessBean() {
        log.fine("Stateless Constructor");
    }


    public void incrInvocations() {
        log.fine("Incrementing Invocation Number");
        allBeans.put(this, Void.TYPE);
        log.fine("Stateless: Exit");
    }


    public static int getInvocations() {
        return allBeans.size();
    }


    private static final Map<PooledStatelessBean, Object> allBeans = Collections.synchronizedMap(new IdentityHashMap<>());
    private static final Logger log = Logger.getLogger(StartupBean.class.getName());
}
