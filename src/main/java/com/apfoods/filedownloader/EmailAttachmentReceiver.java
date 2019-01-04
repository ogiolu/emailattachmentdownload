package com.apfoods.filedownloader;


import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.zeroturnaround.zip.ZipUtil;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.io.File;
import java.io.IOException;
import javax.mail.*;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.FlagTerm;


public class EmailAttachmentReceiver {
    private String saveDirectory;
    @Value("${sftpusername}")
    private String sftpusername="INT017";
    @Value("${sftpassword}")
    private String sftpassword="dh}hitca";
    @Value("${sftphost}")
    private String sftphost="sftp.biscuits.com";
    @Value("${sftport}")
    private int sftport=22;
    @Value("${remotedirectory}")
    private String remotedirectory="/files/Payslips/";
    @Value("${host}")
    private String host="mail.petcom.com.ng";
    @Value("${port}")
    private String port="995";
    @Value("${emailuserName}")
    private String emailuserName="apf-payslip@petcom.com.ng";
    @Value("${password}")
    private String password="1Development2@";



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
                System.out.println("\t Subject: " + subject);
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
            ZipUtil.pack(new File(sourceFile), new File(zipFile));


    }

    public void uploadFileToSTFP(String ftpusername,String ftppassword,String ftphost,int ftpport,String localdirectory,String desctinationDirectory){
        MigrateFles migrateFles = new MigrateFles();
        System.out.println("Upload files to sfp");
        migrateFles.uploadFileToSTFP(ftpusername,ftppassword,ftphost,ftpport,localdirectory,desctinationDirectory);

    }



     public void peformDownload() throws Exception {

         final String tempDir = System.getProperty("java.io.tmpdir") + "attachment";
         final String ziDir = System.getProperty("java.io.tmpdir") + "attachmentzip";
         File f = new File(tempDir);

       System.out.println(" sftpusername >>>>" +sftpusername);
         if (!f.exists()) {
             System.out.println("tempDir does not exist >>>>" + tempDir);
             f.mkdir();
         }
         File z = new File(ziDir);
         if (!z.exists()) {
             System.out.println("ziDir does not exist >>>>>>" + ziDir);
             z.mkdir();
         }

		System.out.println("host>>>>"+host);
		System.out.println("port>>>>"+port);
		System.out.println("userName>>>>"+emailuserName);
		System.out.println("password>>>>"+password);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String suffix= sdf.format(timestamp);
		String saveDirectory = tempDir;
		System.out.println("saveDirectory>>>>"+saveDirectory);
		String zipDirectory = ziDir +File.separator+"payslips"+suffix+".zip";
		deleteContentOfLocalDirectory(saveDirectory);
		setSaveDirectory(saveDirectory);
		downloadEmailAttachments(host, port, emailuserName, password);
		zipFiles(saveDirectory,zipDirectory);
		uploadFileToSTFP(sftpusername,sftpassword,sftphost,Integer.valueOf(sftport),zipDirectory,remotedirectory);
     }


     }



