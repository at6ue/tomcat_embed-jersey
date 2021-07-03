package com.github.at6ue.tomcat_embed;

import java.nio.file.Paths;

import org.apache.catalina.startup.Tomcat;

public class App {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.getConnector(); // Mandatory from Tomcat9
        var context = tomcat.addWebapp("", Paths.get("src/main/webapp/").toAbsolutePath().toString());
        context.setName("app"); // Optional
        tomcat.enableNaming(); // Mandatory for JNDI lookup
        tomcat.start();
        tomcat.getServer().await();
    }
}