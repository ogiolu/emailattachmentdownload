package com.apfoods.filedownloader;

import com.jcraft.jsch.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.IOException;


public class MigrateFles {

    private static final Logger log = LoggerFactory.getLogger(MigrateFles.class);


    public void uploadFileToSTFP(String ftpusername,String ftppassword,String ftphost,int ftpport,String localdirectory,String desctinationDirectory)  {
        try {
            Session session = getSession(ftpusername, ftppassword, ftphost, ftpport);
            log.info("connection >>>>>");
            ChannelSftp sftpChannel = getChannelSftp(session);

            uploadFileToRemoteServer(session, sftpChannel, localdirectory, desctinationDirectory);
        }
        catch (Exception ex){
            log.error("Error Occured while  Connecting to SFP >>>"+ex.getMessage());
          ex.printStackTrace();
        }

    }

    private  Session getSession(String ftpusername, String ftppassword, String ftphost, int ftpport) throws JSchException {

        JSch jsch = new JSch();
        Session session;
        session = jsch.getSession(ftpusername, ftphost, ftpport);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(ftppassword);
        session.connect();
        return session;
    }


    private  Boolean uploadFileToRemoteServer(Session session, ChannelSftp sftpChannel, String localdirectory, String remotedirectory ){
        try {
            uploadFiles(localdirectory,remotedirectory,sftpChannel);
            sftpChannel.exit();
            session.disconnect();
            return true;
        } catch (Exception e) {
            log.error("Error Occurred while Putting file "+e.getMessage());
            e.printStackTrace();
            return false;
        }

    }

    public void uploadFiles(String localFolder, String remoteFolder,ChannelSftp sftp) throws SftpException {
        File localFile = new File(localFolder);


        if (localFile.exists() ) {

            sftp.put(localFile.getAbsolutePath(), remoteFolder);
           log.info("Files Succefully Uploaded");
        } else {
            log.error("local folder \"" + localFile.getAbsolutePath() + "\" does not exist");

        }
    }

    public void deleteContentOfLocalDirectory (String folder){

        File srcDir = new File(folder);
        try {
            FileUtils.cleanDirectory(srcDir);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private  ChannelSftp getChannelSftp( Session session) throws JSchException, SftpException {
        Channel channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp sftpChannel = (ChannelSftp) channel;
        return sftpChannel;
    }

    public  boolean isRealFile(String filename) {
        return (!filename.equals("..") && !filename.equals("."));
    }

    public void test(){
        System.out.println("Here to print");
    }

    public void zipFiles( String sourceFile,String zipFile){
        ZipUtil.pack(new File(sourceFile), new File(zipFile));
    }

}
