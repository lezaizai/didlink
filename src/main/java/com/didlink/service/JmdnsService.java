package com.didlink.service;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.net.InetAddress;

public class JmdnsService {
    private final static String REMOTE_TYPE = "_didsample._tcp.local.";
    private JmDNS jmdns = null;

    public void register() {
        try {
            // Create a JmDNS instance
            jmdns = JmDNS.create(InetAddress.getLocalHost());

            // Register a service
            ServiceInfo serviceInfo = ServiceInfo.create(REMOTE_TYPE, "_didsample", 5946, "");
            jmdns.registerService(serviceInfo);
            System.out.println("mDns service " + REMOTE_TYPE + " was registed.");

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void unRegister() {
        if (jmdns != null) {
            jmdns.unregisterAllServices();
            jmdns = null;
            System.out.println("mDns service " + REMOTE_TYPE + " was unregisted.");
        }
    }
}
