/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipchangenotifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *
 * @author netizen
 */
public class IpChangeNotifier {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        // TODO code application logic here
        String toaddress = YOUR_EMAIL_ID;
        String fromaddress = YOUR_EMAIL_ID;
        final String username = YOUR_EMAIL_USERNAME;
        final String password = YOUR_EMAIL_PASSOWORDs;
        String host = "smtp.gmail.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        Session session;
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator(){
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication(){
                        return new PasswordAuthentication(username, password);
                    }
                });
        Runtime r;
        Process p;
        File ipfile = new File("ipcheck");
        ipfile.createNewFile();
        r = Runtime.getRuntime();
        p = r.exec("wget http://ipinfo.io/ip -qO -");
        p.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String currentip = reader.readLine();
        try{
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromaddress));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toaddress));
            message.setSubject("IP Change Notification");
            message.setText("Your computer has been turned on with IP " + currentip);
            Transport.send(message);
            System.out.println("Message sent successfully");
        } catch (MessagingException ex) {
            Logger.getLogger(IpChangeNotifier.class.getName()).log(Level.SEVERE, null, ex);
        }
        FileWriter fwr = new FileWriter("ipcheck");
        fwr.write(currentip);
        fwr.close();
        Thread.sleep(300000);
        while (true){
            try{
                p = r.exec("wget http://ipinfo.io/ip -qO -");
                p.waitFor();
                reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                currentip = reader.readLine();
                if (currentip == null){
                    Thread.sleep(300000);
                    continue;
                }
                BufferedReader fre = new BufferedReader(new FileReader("ipcheck"));
                String pre = fre.readLine();
                fre.close();
                if(!pre.equals(currentip)){
                    try{
                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(fromaddress));
                        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toaddress));
                        message.setSubject("IP Change Notification");
                        message.setText("IP has changed to " + currentip);
                        Transport.send(message);
                        System.out.println("Message sent successfully");
                    } catch (MessagingException ex) {
                        Logger.getLogger(IpChangeNotifier.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    fwr = new FileWriter("ipcheck");
                    fwr.write(currentip);
                    fwr.close();
                }
                Thread.sleep(300000);
            } catch(NullPointerException ex){
                Logger.getLogger(IpChangeNotifier.class.getName()).log(Level.SEVERE, null, ex);
                Thread.sleep(300000);
            }
        }
        
    }
    
}
