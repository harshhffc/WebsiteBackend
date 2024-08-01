FROM tomcat:9.0

WORKDIR /app
COPY build/libs/HomefirstWebsiteSpring-V1.0.0.war /usr/local/tomcat/webapps/homefirstweb.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
