#!/bin/bash
# Letsencrypt certificate automatic renewal. This script must be run as root.
# This script assumes that it run automatically by CRON.

# This is the system user name
APP_USER_NAME="user"
# Domain name, which is accessible through public IP
DOMAIN_NAME="www.mycompany.com"

# This is the location where your JAR file is deployed
APP_NAME="myAwesomeApp"
PKCS12_EXPORT_DIR="/home/$APP_USER_NAME/apps/$APP_NAME"

# Keystore password
PASSWORD="KEYSTORE_PASS"

# --no-self-upgrade: Prevent certbot automatic update which asks prompt and suspends this cron job
certbot renew --force-renewal --no-self-upgrade

openssl pkcs12 -export -in /etc/letsencrypt/live/${DOMAIN_NAME}/fullchain.pem \
  -inkey /etc/letsencrypt/live/${DOMAIN_NAME}/privkey.pem \
  -out ${PKCS12_EXPORT_DIR}/keystore.p12 \
  -passout pass:${PASSWORD} \
  -name nani \
  -CAfile /etc/letsencrypt/live/${DOMAIN_NAME}/chain.pem \
  -caname root

chmod g+rw,o+rw ${PKCS12_EXPORT_DIR}/keystore.p12
chown ${APP_USER_NAME} ${PKCS12_EXPORT_DIR}/keystore.p12
chgrp ${APP_USER_NAME} ${PKCS12_EXPORT_DIR}/keystore.p12
