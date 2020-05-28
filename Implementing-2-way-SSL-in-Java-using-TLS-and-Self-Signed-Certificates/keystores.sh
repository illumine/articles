#!/bin/sh
# Requires the Keytool and openssl tools


# Generate RSA key pair of 2048 bits
openssl genrsa -out illumineit.com.key 2048  

# Generate certificate request for CA (.csr)
openssl req -x509 -sha256 -new -subj '/C=CY/ST=Nikosia/L=Center/CN=illumineit.com'  -key illumineit.com.key -out illumineit.com.csr

# Generate self signed certificate expiry-time 10 years from the certificate request
openssl x509 -sha256 -days 3652 -in illumineit.com.csr -signkey illumineit.com.key -out illumineit.com.crt


# Import the pair (private key and selfsigned certificate) in a new JKS (Trustore and Keystore together)
# Create PKCS12 keystore from private key and public certificate.
openssl pkcs12 -export -name illumineit.com -in illumineit.com.crt -inkey illumineit.com.key -out illumineit.com.p12 -passin pass:welcome -password pass:welcome

# Convert PKCS12 keystore into a JKS keystore
keytool -importkeystore -destkeystore illumineit.com.jks -srckeystore illumineit.com.p12 -srcstoretype pkcs12 -alias illumineit.com -srcstorepass welcome  -storepass welcome  -noprompt