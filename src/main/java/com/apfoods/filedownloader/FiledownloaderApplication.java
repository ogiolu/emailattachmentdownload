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

	@Autowired
	private  Environment env;

	@Bean(name = "threadPoolExecutor")
	@Scheduled(cron = "0 5 * * * ?")
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(7);
		executor.setMaxPoolSize(42);
		executor.setQueueCapacity(11);
		executor.setThreadNamePrefix("threadPoolExecutor-");
		executor.initialize();
		return executor;
	}
/*
	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(25);
		return executor;
	}

*/
	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext ctx =SpringApplication.run(FiledownloaderApplication.class, args);
		final String tempDir = System.getProperty("java.io.tmpdir")+"attachment";
		final String ziDir = System.getProperty("java.io.tmpdir")+"attachmentzip";
		File f = new File(tempDir);
		if(!f.exists())
			f.mkdir();
		File z = new File(ziDir);
		if(!z.exists())
			z.mkdir();
		String sftpusername=ctx.getEnvironment().getProperty("sftpusername");
		String sftpassword=ctx.getEnvironment().getProperty("sftpassword");
		String sftphost=ctx.getEnvironment().getProperty("sftphost");
		String sftport=ctx.getEnvironment().getProperty("sftport");
		String remotedirectory=ctx.getEnvironment().getProperty("remotedirectory");

		String host=ctx.getEnvironment().getProperty("host");
		String port=ctx.getEnvironment().getProperty("port");
		String userName=ctx.getEnvironment().getProperty("emailuserName");
		String password=ctx.getEnvironment().getProperty("password");
		System.out.println("host>>>>"+host);
		System.out.println("port>>>>"+port);
		System.out.println("userName>>>>"+userName);
		System.out.println("password>>>>"+password);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String suffix= sdf.format(timestamp);
		String saveDirectory = tempDir;
		String zipDirectory = ziDir +File.separator+"payslips"+suffix+".zip";

		EmailAttachmentReceiver receiver = new EmailAttachmentReceiver();
		receiver.deleteContentOfLocalDirectory(saveDirectory);
		receiver.setSaveDirectory(saveDirectory);
		receiver.downloadEmailAttachments(host, port, userName, password);
		receiver.zipFiles(saveDirectory,zipDirectory);
		receiver.uploadFileToSTFP(sftpusername,sftpassword,sftphost,Integer.valueOf(sftport),zipDirectory,remotedirectory);

	}
}


