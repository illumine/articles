package com.illumineit.tls;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/*
See
http://illumineconsulting.blogspot.de/2014/01/implementing-2-way-ssl-in-java-using.html

1) Generate RSA key 
openssl genrsa -out ianalyzer.key 1024  -passin pass:welcome

2) Generate certificate request for CA (.csr)
openssl req -x509 -sha256 -new -subj '/C=DE/ST=Baden/L=Walldorf/CN=ianalyzer'  -key ianalyzer.key -out ianalyzer.csr

3) Generate self signed certificate expiry-time 10 years from the certificate request
openssl x509 -sha256 -days 3652 -in ianalyzer.csr -signkey ianalyzer.key -out ianalyzer.crt


4) Import the pair (private key and selfsigned certificate) in a new JKS (Trustore and Keystore together)
# Create PKCS12 keystore from private key and public certificate.
openssl pkcs12 -export -name ianalyzer -in ianalyzer.crt -inkey ianalyzer.key -out ianalyzer.p12 -passin pass:welcome -password pass:welcome

# Convert PKCS12 keystore into a JKS keystore
keytool -importkeystore -destkeystore ianalyzer.jks -srckeystore ianalyzer.p12 -srcstoretype pkcs12 -alias ianalyzer -srcstorepass welcome  -storepass welcome  -noprompt
*/

/**
 * Example ssl client
 * @author Michael Mountrakis, mountrakis@illumineit.com 
 *
 */
public class TwoWaySslClient {
    public static void main(String[] args) {
        
        try{
            System.setProperty("java.protocol.handler.pkgs","com.sun.net.ssl.internal.www.protocol");
            
            //Client settings:
            System.setProperty("javax.net.ssl.keyStore","ianalyzer.jks");
            System.setProperty("javax.net.ssl.keyStorePassword","welcome");

            System.setProperty("javax.net.ssl.trustStore","ianalyzer.jks");
            System.setProperty("javax.net.ssl.trustStorePassword","welcome");

             System.out.println("Ok passed.");
            //connect to google           
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();            
            SSLSocket sslSock = (SSLSocket) factory.createSocket("localhost",8095);
                       
            //send HTTP get request
            PrintWriter out = new PrintWriter(sslSock.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(sslSock.getInputStream()));

            out.println("--------------------------> It works!!!");
            System.out.println("Written to server.");

            // read response
            StringBuffer buf = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null) {
               System.out.println("Received : " + line);
               buf.append(line);
            }
            System.out.println("Read from server: " + buf.toString());
           
            in.close();
            out.close();
            // Close connection.
            sslSock.close();
           
        }catch(Exception ex){
           ex.printStackTrace();
        }
    }
}
