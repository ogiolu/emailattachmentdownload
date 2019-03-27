package com.apfoods.filedownloader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;



@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@EnableAsync
@EnableScheduling
public class FiledownloaderApplication {


	public static void main(String[] args) throws Exception {
	SpringApplication.run(FiledownloaderApplication.class, args);

		EmailAttachmentReceiver mailAttachmentReceiver = new EmailAttachmentReceiver();

		mailAttachmentReceiver.peformDownload();


	}
}


