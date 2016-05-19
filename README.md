# IpChangeNotifier
This is a Java Application designed to run as a startup application (Windows,Mac and Linux) that announces the
public IP address of the computer the first time it connects to the internet and notifies you of any change in your IP address
thereafter via email by using any email service that supports SMTP.

This was designed as an alternative to using DynDNS and similar services that track changes in Dynamic IP and uploads it to
their servers. Note that DynDNS provides much more advanced services and this is free alternative that provides very basic
IP change notification.

STEPS TO USE IPCHANGENOTIFIER:                                                                                                
1.Install Java                                                                                                                
2.Compile it after filling in the placeholders                                                                                
2.Set `java -jar %%path to jar file%%IpChangeNotifier.jar' as a startup command in your respective operating systems.
