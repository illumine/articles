# Implementing 2-way SSL in Java using TLS and Self Signed Certificates part2: the Java Server

Requires the Trustore/Keystore created in [Step-1.](part-1-keystore-trustore.md)


**Step 2: The server.** 
[The entire code for server implementation can be downloaded here](src/com/illumineit/tls/TwoWaySslServer.java)


To write the server process in Java is pretty simple. You just have to do a couple of steps:\
Specify a couple of properties so that the Trustore/Keystore can be loaded like the following code fragment shows: 

<code>
System.setProperty("javax.net.ssl.keyStore","mysystem.jks");
System.setProperty("javax.net.ssl.keyStorePassword","welcome");

System.setProperty("javax.net.ssl.trustStore","mysystem.jks");
System.setProperty("javax.net.ssl.trustStorePassword","welcome");
</code>

Create the ServerSocket as anSSLServerSocketlike the following code fragment shows:
 
<code> 
char ksPass[] = "welcome".toCharArray();
char ctPass[] = "welcome".toCharArray();

//Create and load the Keystore
KeyStore ks = KeyStore.getInstance("JKS");
ks.load(new FileInputStream("ianalyzer.jks"), ksPass);
KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
kmf.init(ks, ctPass);

//Create the ServerSocket as an SSLServerSocket
SSLContext secureSocket = SSLContext.getInstance("TLS");
secureSocket.init(kmf.getKeyManagers(), null, null);
SSLServerSocketFactory ssf = secureSocket.getServerSocketFactory();
ssocket = (SSLServerSocket) ssf.createServerSocket(8095);
SSLServerSocket ss = (SSLServerSocket) ssocket;

//This explicitly states TLS with 2-way authentication
ss.setNeedClientAuth(true); 
</code>


[The entire code for server implementation can be downloaded here](src/com/illumineit/tls/TwoWaySslServer.java)