/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.flowlogix.ejbpool;

import java.util.logging.Logger;
import java.util.stream.IntStream;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;

/**
 *
 * @author lprimak
 */
@Singleton
@LocalBean
@Startup
public class StartupBean {
    @PostConstruct
    void init() {
        testSessionBeanPool();
        testMDBPool();
    }


    private void testSessionBeanPool() {
        int numOfInvocations = 10;
        IntStream.range(0, numOfInvocations).forEach(ic -> mes.execute(() -> psb.incrInvocations()));
        try {
            Thread.sleep(1 * 1000);
        } catch (InterruptedException ex) {}
        int totalInvocations = PooledStatelessBean.getInvocations();
        log.info(String.format("Stateless - Number of Invocations: %d", totalInvocations));
        if(totalInvocations > 1) {
            log.info("Stateless: More than one thread!");
        }
    }


    private void testMDBPool() {
        mes.execute(() -> doTestMDBPool());
    }


    private void doTestMDBPool() {
        int numOfInvocations = 10;
        try (JMSContext jmsc = cf.createContext()) {
            IntStream.range(0, numOfInvocations).forEach(ic -> jmsc.createProducer().send(q, jmsc.createTextMessage("hello")));
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {}
        int totalInvocations = PooledMDB.getInvocations();
        log.info(String.format("MDB - Number of Invocations: %d", totalInvocations));
        if(totalInvocations > 1) {
            log.info("MDB: More than one thread!");
        }
    }


    private @Inject PooledStatelessBean psb;
    private @Resource ManagedExecutorService mes;
    private @Resource(lookup = "java:module/jms/sampleQueue") Queue q;
    private @Resource ConnectionFactory cf;
    private static final Logger log = Logger.getLogger(StartupBean.class.getName());
}
