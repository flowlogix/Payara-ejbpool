/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.flowlogix.ejbpool;

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
        log.info("Stateless Constructor");
    }


    public void incrInvocations() {
        ++numInvocations;
        log.info("Incrementing Invocation Number");
        log.info("Stateless: Exit");
    }


    public int getInvocations() {
        return numInvocations;
    }


    private int numInvocations = 0;
    private static final Logger log = Logger.getLogger(StartupBean.class.getName());
}
