package com.apfoods.filedownloader;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Configuration
//@PropertySource("classpath:application.properties")
@PropertySource("file:${PETCOM_HOME}/application.properties")
public class Config {
    public String getSftpusername() {
        return sftpusername;
    }

    public void setSftpusername(String sftpusername) {
        this.sftpusername = sftpusername;
    }

    private String sftpusername;
}
