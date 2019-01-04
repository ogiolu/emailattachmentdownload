package com.apfoods.filedownloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;


@Component
    public class ScheduledTask {

        private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);

        private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


        @Scheduled(cron ="0 0 12 28 * ?")
        public void reportCurrentTime() {
            EmailAttachmentReceiver m= new EmailAttachmentReceiver();
            try {
                m.peformDownload();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

