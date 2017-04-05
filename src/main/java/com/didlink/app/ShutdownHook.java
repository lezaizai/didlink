package com.didlink.app;

import com.didlink.db.JdbcDatabase;
import com.didlink.service.JmdnsService;

class ShutdownHook extends Thread {
    public void run() {
        JdbcDatabase jdbcDatabase = AppServer.getJdbcDatabase();
        if (jdbcDatabase != null) {
            jdbcDatabase.close();
        }

        JmdnsService jmdnsService = AppServer.getJmdnsService();
        if (jmdnsService != null) {
            jmdnsService.unRegister();
        }

        System.out.println("Shutdown");
    }
}