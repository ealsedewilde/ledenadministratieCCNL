REM Create a selfsigned codeSigning certificate (pfx-file)
REM This is used in the Maven build to sign the LedenadministratieCCNL-8.5.msi
md ..\..\target\cert
openssl req -x509 -config cert_config.txt -nodes -days 3650 -newkey rsa:4096 -keyout ../../target/cert/myekukey.pem -out ../../target/cert/myeku.pem
openssl pkcs12 -export -out myeku.pfx -inkey ../../target/cert/myekukey.pem -in ../../target/cert/myeku.pem -passout pass:
pause
