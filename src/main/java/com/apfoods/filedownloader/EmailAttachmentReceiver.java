package com.apfoods.filedownloader;


import com.jcraft.jsch.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.zeroturnaround.zip.ZipUtil;

import java.util.Collection;
import java.util.Properties;
import java.io.File;
import java.io.IOException;
import javax.mail.*;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.FlagTerm;

public class EmailAttachmentReceiver {
    private String saveDirectory;

    /**
     * Sets the directory where attached files will be stored.
     *
     * @param dir absolute path of the directory
     */
    public void setSaveDirectory(String dir) {
        this.saveDirectory = dir;
    }

    /**
     * Downloads new messages and saves attachments to disk if any.
     *
     * @param host
     * @param port
     * @param userName
     * @param password
     */
    
    public void downloadEmailAttachments(String host, String port,
                                         String userName, String password) {
        Properties properties = new Properties();

        // server setting
        properties.put("mail.pop3.host", host);
        properties.put("mail.pop3.port", port);

        // SSL setting
        properties.setProperty("mail.pop3.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.pop3.socketFactory.fallback", "false");
        properties.setProperty("mail.pop3.socketFactory.port",
                String.valueOf(port));

        Session session = Session.getDefaultInstance(properties);

        try {
            // connects to the message store
            Store store = session.getStore("pop3");
            store.connect(userName, password);

            // opens the inbox folder
            Folder folderInbox = store.getFolder("INBOX");
          //  folderInbox.open(Folder.READ_ONLY);
            folderInbox.open(Folder.READ_WRITE);

            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm unseenFlagTerm = new FlagTerm(seen, false);

            // fetches new messages from server
           //Message[] arrayMessages = folderInbox.getMessages();
            Message[] arrayMessages=folderInbox.search(unseenFlagTerm);

            for (int i = 0; i < arrayMessages.length; i++) {
                Message message = arrayMessages[i];
                Address[] fromAddress = message.getFrom();
                String from = fromAddress[0].toString();
                String subject = message.getSubject();
                String sentDate = message.getSentDate().toString();

                String contentType = message.getContentType();
                String messageContent = "";

                // store attachment file name, separated by comma
                String attachFiles = "";

                if (contentType.contains("multipart")) {
                    // content may contain attachments
                    Multipart multiPart = (Multipart) message.getContent();
                    int numberOfParts = multiPart.getCount();

                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()  )) {

                            // this part is attachment
                            String fileName = part.getFileName();

                            if(!fileName.contains("Taxslip")){
                                System.out.println("fileName >>>>"+fileName);
                                attachFiles += fileName + ", ";
                                part.saveFile(saveDirectory + File.separator + fileName);
                            }


                        } else {
                            // this part may be the message content
                            messageContent = part.getContent().toString();
                        }
                    }

                    if (attachFiles.length() > 1) {
                        attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
                    }
                } else if (contentType.contains("text/plain")
                        || contentType.contains("text/html")) {
                    Object content = message.getContent();
                    if (content != null) {
                        messageContent = content.toString();
                    }
                }

                // print out details of each message
                System.out.println("Message #" + (i + 1) + ":");
                System.out.println("\t From: " + from);
                System.out.println("\t Subject: " + subject);
                System.out.println("\t Sent Date: " + sentDate);
                System.out.println("\t Message: " + messageContent);
                System.out.println("\t Attachments: " + attachFiles);

                message.setFlag(Flags.Flag.DELETED, false);
            }

            // disconnect
            folderInbox.close(true);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for pop3.");
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    ///Delete Existing files from FOlder
    //Download all the files
    //Rename the files
    //Zip the files in the location
    //Send the files to Sftp

    public void deleteContentOfLocalDirectory (String folder){

        File srcDir = new File(folder);
        try {
          if (srcDir.exists()){
              FileUtils.cleanDirectory(srcDir);
          }

        } catch (IOException e) {

            e.printStackTrace();
        }


    }

    public void zipFiles(String sourceFile,String zipFile) throws Exception{
        System.out.println("Inside zip file >>>>>>>>>>>");
        if (new File(sourceFile).exists() &&  new File(zipFile).exists()) {
            ZipUtil.pack(new File(sourceFile), new File(zipFile));
        }
        else{
            System.out.println("source or distination file for zipiing does not exists >>>>>>>>>>>");
        }

    }

    public void uploadFileToSTFP(String ftpusername,String ftppassword,String ftphost,int ftpport,String localdirectory,String desctinationDirectory){
        MigrateFles migrateFles = new MigrateFles();
        System.out.println("Upload files to sfp");
        migrateFles.uploadFileToSTFP(ftpusername,ftppassword,ftphost,ftpport,localdirectory,desctinationDirectory);

    }







}

