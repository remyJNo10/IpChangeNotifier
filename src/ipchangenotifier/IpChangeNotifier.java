/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipchangenotifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.MalformedURLException;
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
     * @throws java.net.SocketException
     * @throws java.net.UnknownHostException
     */
    public static void main(String[] args) throws IOException, InterruptedException, java.net.SocketException, java.net.UnknownHostException {
        // TODO code application logic here
        String toaddress = YOUR_EMAIL_ADDRESS;
        String fromaddress = YOUR_EMAIL_ADDRESS;
        final String username = YOUR_EMAIL_USERNAME;
        final String password = YOUR_EMAIL_PASSWORD;
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
        String currentip = "";
        FileWriter fwr;
        while(currentip.isEmpty()){
            try{
                currentip = getTextFromURL("http://checkip.amazonaws.com");
                System.out.println("currentip" + currentip);
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(fromaddress));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(toaddress));
                message.setSubject("IP Change Notification");
                message.setText("Your computer has been turned on with IP " + currentip);
                Transport.send(message);
                System.out.println("Message sent successfully");           
                fwr = new FileWriter("ipcheck");
                fwr.write(currentip);
                fwr.close();
                Thread.sleep(10000);
            } catch (MessagingException | java.net.UnknownHostException ex) {
                Logger.getLogger(IpChangeNotifier.class.getName()).log(Level.SEVERE, null, ex);
                Thread.sleep(10000);
            }
        }
        while (true){
            try{
                currentip = getTextFromURL("http://checkip.amazonaws.com");
                if (currentip == null){
                    Thread.sleep(10000);
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
                Thread.sleep(10000);
            } catch(NullPointerException | java.net.SocketException ex){
                Logger.getLogger(IpChangeNotifier.class.getName()).log(Level.SEVERE, null, ex);
                Thread.sleep(10000);
            }
        }
        
    }
    
	/**
	 * 
	 * @param url Address to get text from
	 * @return Page content
         * @throws java.net.SocketException
         * @throws java.net.MalformedURLException
         * @throws java.net.UnknownHostException
	 */
	public static String getTextFromURL(String url) throws java.net.SocketException, MalformedURLException, java.net.UnknownHostException, IOException {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                    connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) 
            response.append(inputLine);

        in.close();

        return response.toString();
    }
    
}
