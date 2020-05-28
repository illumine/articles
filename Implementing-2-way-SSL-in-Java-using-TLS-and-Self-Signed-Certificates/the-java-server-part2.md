# Implementing 2-way SSL in Java using TLS and Self Signed Certificates part2: the Java Server

**Step 2:** The server (Get the complete server code here)

Requires the Trustore/Keystore created in Step-1.

To write the server process in Java is pretty simple. You just have to do a couple of steps:\
Specify a couple of properties so that the Trustore/Keystore can be loaded like the following code fragment shows: \

<code>
System.setProperty("javax.net.ssl.keyStore","mysystem.jks");
System.setProperty("javax.net.ssl.keyStorePassword","welcome");

System.setProperty("javax.net.ssl.trustStore","mysystem.jks");
System.setProperty("javax.net.ssl.trustStorePassword","welcome");
</code>

Create the ServerSocket as anSSLServerSocketlike the following code fragment shows:
 
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

The entire code for server implementation can be downloaded here.
Posted by Michael Mountrakis at 13:47 No comments:  
Labels: 2-way ssl, CA, Java, javax.net.ssl, key management, keystore, keytool, openssl, RSA, Secure HTTP, self signed certificates, socket, SSL, SunX509, TLS, trustore, x509
Implementing 2-way SSL in Java using TLS and Self Signed Certificates part1
Consider that we want to implement in Java a secure communication (Transport Layer Security ) for a system called MySystem.

The problem

The security scenario for the implementation of  MySystem is simple:
Authentication only between peers that both share the Keystore/Trustore file
Session establishment only between peers that have the Keystore/Trustore file
Doing so, the entire communication between client and server requires authentication and is encrypted:




Before going further on this study, pay a visit to this site for Java SSL: ssljavaguide.

To implement the scenario, there are three basic steps:
Create the Java Keystore/Trustore that will be used for Authentication and Encryption of Transport/Session. This will be used from both Client and Server parties. (Current Part)
Implement the Client side: (See blog article Part-2)
Implement the Server side: (See blog article Part-3)
Part-4 deals with debugging the Client/Server SSL/TLS communication.

Step 1: Create the Keystore/Trustore
Following steps of this section, results in the creation of a  Keystore/Trustore .jks file that contains:
MySystem Private key 
MySystem Selfsigned Certificate
To do so we are going to use the tools openssl  and keytool. We prefer using openssl because it can work silently - without prompt the user to put passwords, domains, server names....

The steps are:
1) Generate RSA 1024 bit private key. The key will be password protected:
openssl genrsa -out mysystem.key 1024 -passin pass:welcome

2) Generate Certificate Request for CA (.csr) using the private key
openssl req -x509 -sha256 -new -subj '/C=GR/ST=Athens/L=Chalandri/CN=mysystem'  -key mysystem.key -out mysystem.csr

3) Generate self signed certificate expiry-time 10 years from the certificate request
openssl x509 -sha256 -days 3652 -in mysystem.csr -signkey mysystem.key -out mysystem.crt


4) Import the pair (private key and selfsigned certificate) in a new JKS (Trustore/Keystore together)
First we need to create PKCS12 keystore from private key and self signed certificate.
openssl pkcs12 -export -name mysystem -in mysystem.crt -inkey mysystem.key -out mysystem.p12 -passin pass:welcome -password pass:welcome

Then we need to convert PKCS12 keystore into a JKS keystore
keytool -importkeystore -destkeystore mysystem.jks -srckeystore mysystem.p12 -srcstoretype pkcs12 -alias mysystem -srcstorepass welcome  -storepass welcome  -noprompt

At this point we have created the Java  Keystore/Trustore mysystem.jks file.

Copy mysystem.jks on both client and server machines.

Download all the commands for the Keystore/Trustore .jks file generation here