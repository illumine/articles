package com.illumineit.tls;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;


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
 * Example SSL server.
 * @author Michael Mountrakis, mountrakis@illumineit.com
 *
 */
public class TwoWaySslServer  implements Runnable{

	volatile boolean iAmRunning = true;
	Config config = new Config();
	
	public class Config{
		public static final int PORT=8095;
		
		String keystoreFile = "ianalyzer.jks";
		String keytoolPassword="welcome";
		String certificatePassword = "welcome";

		public String getKeystoreFile() {
			return keystoreFile;
		}
		public void setKeystoreFile(String keystoreFile) {
			this.keystoreFile = keystoreFile;
		}
		public String getCertificatePassword() {
			return certificatePassword;
		}
		public void setCertificatePassword(String certificatePassword) {
			this.certificatePassword = certificatePassword;
		}
		public String getKeytoolPassword() {
			return keytoolPassword;
		}
		public void setKeytoolPassword(String keytoolPassword) {
			this.keytoolPassword = keytoolPassword;
		}
		
			
	}
	
	
	public TwoWaySslServer() throws Exception {
	}
	
	
	public void run() {
		serverProcess();
	}	
	
	public void serverProcess(){
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = initializeServerSocket();

			Socket clientSocket = null;

			while (iAmRunning) {

				clientSocket = (SSLSocket) serverSocket.accept();

				//printSocketInfo(clientSocket);
				debug("Request from client : "  + clientSocket.toString());

				serveClient(clientSocket);
				
				Thread.sleep(2);
			}
		}catch( Exception e ){
			e.printStackTrace();
		}finally{
			//whatever happens, make sure you close server socket in the end...
			if( serverSocket != null && !serverSocket.isClosed() ){
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	ServerSocket initializeServerSocket() throws Exception {
		ServerSocket  ssocket = null;
		
		
		//System.setProperty("javax.net.ssl.keyStore","ianalyzer.jks");
		//System.setProperty("javax.net.ssl.keyStorePassword","welcome");

		System.setProperty("javax.net.ssl.trustStore","ianalyzer.jks");
		System.setProperty("javax.net.ssl.trustStorePassword","welcome");
		
		char ksPass[] = config.getKeytoolPassword().toCharArray();
		char ctPass[] = config.getCertificatePassword().toCharArray();
		
		KeyStore ks = KeyStore.getInstance("JKS");

		ks.load(new FileInputStream(config.getKeystoreFile()), ksPass);
		
		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
		kmf.init(ks, ctPass);

		SSLContext secureSocket = SSLContext.getInstance("TLS");
		
		secureSocket.init(kmf.getKeyManagers(), null, null);
		SSLServerSocketFactory ssf = secureSocket.getServerSocketFactory();
		ssocket = (SSLServerSocket) ssf.createServerSocket(config.PORT);
		SSLServerSocket ss = (SSLServerSocket) ssocket;

		ss.setNeedClientAuth(true); //<-- This explicitly states TLS

		
		printServerSocketInfo(ssocket);
		return ssocket;
	}
	
	

	
	
	
	protected void terminateClient(Socket clientSocket, PrintWriter out, BufferedReader in ){
		try {
			out.close();
			in.close();
			clientSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		

	public void serveClient(Socket clientSocket) throws Exception {
		PrintWriter out = null;
		BufferedReader in = null;
		
		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));

			String inputLine="";
			while ((inputLine = in.readLine()) != null) {
				debug(inputLine);
				
				out.println("Received correctly.");
				out.flush();
				break;
			}
			
			//-------------
			//processRequest(controlRequest );
			//---------------
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			terminateClient(clientSocket, out, in);
		}		
	}
	
	
	/**
	 * Starts the iAnalyzer agent by recreating the currying thread
	 */
	public void start(){
		Thread t = new Thread(this);
		t.setName("test-server");
		t.start();	
	}
	
	void printServerSocketInfo(ServerSocket s) {
		debug("SERVER\n\tsocket class: " + s.getClass());
		debug("\tSocker address = " + s.getInetAddress().toString());
		debug("\tSocker port = " + s.getLocalPort());
		
		if( s instanceof SSLServerSocket){
			SSLServerSocket ss = (SSLServerSocket) s;
			//ss.setWantClientAuth(true);
			//ss.setNeedClientAuth(true);
			
			debug("\tNeed client authentication = " + ss.getNeedClientAuth());
			debug("\tWant client authentication = " + ss.getWantClientAuth());
			debug("\tUse client mode = " + ss.getUseClientMode());
		}
		debug("Server started...");
		debug("-----------------------------------");
	}	
	
	
	
	public void debug(String s){
		System.out.println(s);
	}
	
	
	public static void main(String[] argc) {	
		try {
			TwoWaySslServer controller = new TwoWaySslServer();
			controller.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
}
