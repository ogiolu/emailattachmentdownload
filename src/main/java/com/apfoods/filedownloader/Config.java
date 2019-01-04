package com.apfoods.filedownloader;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "com.appfood")

public class Config {

    private String localdirectory;
    private String desctinationDirectory;
    private  Email email = new Email();
    private  Ftp ftp = new Ftp();
    private String savedirectory;

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Ftp getFtp() {
        return ftp;
    }

    public void setFtp(Ftp ftp) {
        this.ftp = ftp;
    }

    public static class Email{
        private String username;
        private String password;
        private String host;
        private int port;


        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }

    public static class Ftp{
        private String host;
        private String port;
        private String userName;
        private String password;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }



    public String getLocaldirectory() {
        return localdirectory;
    }

    public void setLocaldirectory(String localdirectory) {
        this.localdirectory = localdirectory;
    }

    public String getDesctinationDirectory() {
        return desctinationDirectory;
    }

    public void setDesctinationDirectory(String desctinationDirectory) {
        this.desctinationDirectory = desctinationDirectory;
    }


    public String getSavedirectory() {
        return savedirectory;
    }

    public void setSavedirectory(String savedirectory) {
        this.savedirectory = savedirectory;
    }
}
