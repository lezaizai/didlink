package com.didlink.app;

class ShutdownHook extends Thread {
    public void run() {
//        H2JdbcDatabase jdbcDatabase = AppServer.getJdbcDatabase();
//        if (jdbcDatabase != null) {
//            jdbcDatabase.close();
//        }
//
//        JmdnsService jmdnsService = AppServer.getJmdnsService();
//        if (jmdnsService != null) {
//            jmdnsService.unRegister();
//        }

        System.out.println("Shutdown");
    }
}