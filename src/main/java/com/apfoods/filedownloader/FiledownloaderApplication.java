package com.apfoods.filedownloader;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.inject.Inject;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@EnableAsync
@EnableScheduling
public class FiledownloaderApplication {


	public static void main(String[] args) throws Exception {
	SpringApplication.run(FiledownloaderApplication.class, args);
		final String tempDir = System.getProperty("java.io.tmpdir")+"attachment";
		final String ziDir = System.getProperty("java.io.tmpdir")+"attachmentzip";
		File f = new File(tempDir);

		if(!f.exists()) {
			System.out.println("tempDir does not exist >>>>" + tempDir);
			f.mkdir();
		}
		File z = new File(ziDir);
		if(!z.exists()) {
			System.out.println("ziDir does not exist >>>>>>" + ziDir);
			z.mkdir();
		}

	}
}


