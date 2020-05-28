# Implementing 2-way SSL in Java using TLS and Self Signed Certificates part4: test/debugging

This part depends on the:
Communication credentials, or the Keystore/Trustore file created in [Part-1](part-1-keystore-trustore.md) 


Server implemented in [Part-2](part-2-the-java-server.md)


Client implemented in [Part-3](part-3-the-java-client.md)


**Step 4: Debug the Client/Server Communication**


To start with we need to include on our JVM arguments on both client and server the following option:
`-Djavax.net.debug=all`

So for example to run the server we do:
`$ java TwoWaySslServer -Djavax.net.debug=all`

As a result, we have all the debugging information we need for network operations from the JVM and the imported classes, like SSLServerSocket.


Having our server in debug mode, we can observe that the Keystore (mysystem.jks) was loaded correctly if we notice the following private key log entry:

<pre>
***
found key for : mysystem
chain [0] = [
[
  Version: V3
  Subject: CN=mysystem, L=Chalandri, ST=Athens, C=GR
  Signature Algorithm: SHA256withRSA, OID = 1.2.840.113549.1.1.11

  Key:  Sun RSA public key, 1024 bits
  modulus: 127165120252867655295291767293201905001859144644390543231567027664179957281793248832544049047501722299712701237862474932181664724853946779349563166371011412260964043029373627517538842247060193170649833260910804612805979354599504164270912367917881965338674535760796311997608873587262396297225200721624071184029
  public exponent: 65537
  Validity: [From: Wed Jan 22 12:46:44 CET 2014,
               To: Mon Jan 22 12:46:44 CET 2024]
  Issuer: CN=mysystem, L=Chalandri, ST=Athens, C=GR
  SerialNumber: [    af1b0164 bd3095fa]

Certificate Extensions: 3
[1]: ObjectId: 2.5.29.35 Criticality=false
AuthorityKeyIdentifier [
KeyIdentifier [
0000: 03 B2 09 B4 89 36 32 E4   D6 A3 51 4A 5D 3B CD ED  .....62...QJ];..
0010: B2 00 77 EC                                        ..w.
]

]

[2]: ObjectId: 2.5.29.19 Criticality=false
BasicConstraints:[
  CA:true
  PathLen:2147483647
]

[3]: ObjectId: 2.5.29.14 Criticality=false
SubjectKeyIdentifier [
KeyIdentifier [
0000: 03 B2 09 B4 89 36 32 E4   D6 A3 51 4A 5D 3B CD ED  .....62...QJ];..
0010: B2 00 77 EC                                        ..w.
]
]

]
  Algorithm: [SHA256withRSA]
  Signature:
0000: 78 58 C9 F5 C0 42 2D 62   B5 0D 8A 79 6B 57 5A 85  xX...B-b...ykWZ.
0010: 8C 85 20 4D 7B B3 8A 0A   DF 83 D9 D1 5A FA F6 26  .. M........Z..&
0020: 53 56 DB FE B3 82 42 35   0C BF E8 BD 75 0A 18 7A  SV....B5....u..z
0030: D7 B0 36 E5 4E F9 82 FB   23 57 EC 23 3F D0 92 9E  ..6.N...#W.#?...
0040: 9C D6 FA 26 32 7C B6 4A   62 A4 4B AB F7 D3 64 7C  ...&2..Jb.K...d.
0050: 37 92 ED F2 2B 62 BC E7   A6 35 E6 87 67 9E BD 0D  7...+b...5..g...
0060: 97 5E 0F 31 A9 B1 AB 64   CC F9 4B 51 3E 90 7B 2F  .^.1...d..KQ>../
0070: E9 2E 23 E5 BC D3 DA 32   20 3B 6C 2C B8 E2 7C 6B  ..#....2 ;l,...k

]
</pre>

If we don´t find our private key in the above debugging information, we need to check again the parameters:
<code>
  System.setProperty("javax.net.ssl.keyStore","mysystem.jks");
  System.setProperty("javax.net.ssl.keyStorePassword","welcome");
</code>
Similarly again in debugging mode, we can observe that the Trustore (mysystem.jks) was loaded correctly from server if we notice the following log entry:

<pre>
***
trustStore is: mysystem.jks
trustStore type is : jks
trustStore provider is : 
init truststore
adding as trusted cert:
  Subject: CN=mysystem, L=Chalandri, ST=Athens, C=GR
  Issuer:  CN=mysystem, L=Chalandri, ST=Athens, C=GR
  Algorithm: RSA; Serial number: 0xaf1b0164bd3095fa
  Valid from Wed Jan 22 12:46:44 CET 2014 until Mon Jan 22 12:46:44 CET 2024

trigger seeding of SecureRandom
done seeding SecureRandom
SERVER
 socket class: class com.sun.net.ssl.internal.ssl.SSLServerSocketImpl
 Socker address = 0.0.0.0/0.0.0.0
 Socker port = 8095
 Need client authentication = true
 Want client authentication = false
 Use client mode = false
</pre>

If we don´t find the self signed certificate key in the above debugging information, we need to check again the parameters:

<code>
System.setProperty("javax.net.ssl.trustStore","mysystem.jks");
System.setProperty("javax.net.ssl.trustStorePassword","welcome");
</code>

Some really interesting Exceptions you may come across while debugging your application:

`javax.net.ssl.SSLException: Unrecognized SSL message, plaintext connection?`
Caught on Server
Probably you have created the client socket with something like:
<code>
clientSocket = new Socket();
</code>
Instead of :
<code>
SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
clientSocket = (SSLSocket) factory.createSocket("localhost",port);
</code>


Next Java Exception is even more interesting:


`java.lang.IllegalStateException: KeyManagerFactoryImpl is not initialized`

You have not initialized KeyManagerFactory object, meaning that you should call method init() with a valid KeyStore and Certificate Password:

<code>
  String keystoreFile = "/opt/mysystem/etc/mysystem.jks";
  String keystorePassword = "welcome";
  KeyStore ks = KeyStore.getInstance("JKS");
  ks.load(new FileInputStream(keystoreFile), keystorePassword );
  KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
  kmf.init(ks,"myCertificatePassword");
</code>


Another exception is the following one:
`java.security.cert.CertificateException: No X509TrustManager implementation avaiable`
Check the trustrore. Ensure that property javax.net.ssl.trustStore is set appropriately.



The following exception cautgh on server:
`javax.net.ssl.SSLHandshakeException: null cert chain`
Received on server
Check that both Client and server share the trustore.
Check that trustore contains the same signed certificate

Finaly, the classic `IOException`:
`java.io.IOException: Keystore was tampered with, or password was incorrect`
Check the Keystore password, system property javax.net.ssl.keyStorePassword
