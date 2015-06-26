# Distributed Systems Technologies DST15 #

* Project for **184.260 Distributed Systems Technologies 2015S** on TU Wien
* All bugs were fixed

### Run test ###
* mvn install -Pass1-jpa
* mvn install -Pass1-nosql
* mvn install -Pass2-ejb
* mvn install -Pass2-ws
* mvn install -Pass2-di
* mvn install -Pass2-di-agent
* mvn install -Pass3-jms
* mvn install -Pass3-event
* mvn install -Pass3-aop
* mvn install clean -Pall -DskipTests

### Webservice url - SOAP ###
* http://localhost:4204/ass2-ws/StatisticsService/service
* http://localhost:4204/ass2-ws/StatisticsService/service?wsdl

### SOAP test ###
* in WSBaseTest.closeContainer() add Thread.sleep(4*60 * 1000) to wait 4 minutes
* run SOAP-UI (by SmartBear) -> create project by adding wsdl url and run test.
  project is also sawed in remoting_artifacts
* capture pacets on loopback with wireshark

### EJB remote call test ###
* wireshark - filter GIOP 

