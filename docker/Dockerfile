FROM centos:centos7
MAINTAINER faithgreen lijinfeigreen@vip.163.com
RUN mkdir -p /var/tmp/jdk
RUN mkdir -p /usr/local/app/gateway
COPY jdk-8u311-linux-x64.tar.gz /var/tmp/jdk
RUN tar xzf /var/tmp/jdk/jdk-8u311-linux-x64.tar.gz -C /var/tmp/jdk
RUN rm -rf /var/tmp/jdk/jdk-8u311-linux-x64.tar.gz
COPY mygateway-1-SNAPSHOT.jar /usr/local/app/gateway
ENV JAVA_HOME=/var/tmp/jdk/jdk1.8.0_311
ENV PATH=${JAVA_HOME}/bin:${PATH}
ENV CLASSPATH=.:${JAVA_HOME}/lib/dt.jar:${JAVA_HOME}/lib/tools.jar

VOLUME ["log"]
WORKDIR /usr/local/app/gateway/

EXPOSE 8880
ENTRYPOINT ["java","-jar","-Xmx1g","-Xms1g","mygateway-1-SNAPSHOT.jar","--spring.profiles.active=office"]
