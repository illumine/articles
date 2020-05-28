# Implementing 2-way SSL in Java using TLS and Self Signed Certificates part3: the Java Client

The client also requires the Keystore/Trustore created in [Part-1](part-1-keystore-trustore.md)
[The entire code for client implementation can be downloaded here](src/com/illumineit/tls/TwoWaySslClient.java)


Server implemented in [Part-2](part-2-the-java-server.md)



**Step 3: The Client** 



Again in the client we have to do a couple of things similar to the server:


The first is to specify the Java Keystore/Trustore we created in  Part-1 of this article:

<code>
System.setProperty("javax.net.ssl.keyStore","mysystem.jks");
System.setProperty("javax.net.ssl.keyStorePassword","welcome");

System.setProperty("javax.net.ssl.trustStore","mysystem.jks");
System.setProperty("javax.net.ssl.trustStorePassword","welcome");
</code>


Similarly with the server side described in Part-2, we have to create the client socket as an SSLSocket:


<code>
SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();    
SSLSocket sslSock = (SSLSocket) factory.createSocket("localhost",8095);
</code>



[The entire code for client implementation can be downloaded here](src/com/illumineit/tls/TwoWaySslClient.java)


Next article, [Part-4](part-4-test-java-tls-client-server.md), of this series will assist you to debug the SSL/TLS client/server communication.