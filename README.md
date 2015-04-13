# README #

* Project of **184.260 Distributed Systems Technologies 2015S** on TU Wien
* Assignment documets can be found in this repository.

### Run test ###
* mvn install -Pass1-jpa
* mvn install -Pass1-nosql
* mvn install -Pass2-ejb
* mvn install -Pass2-ws
* mvn install -Pass2-di
* mvn install -Pass2-di-agent
* mvn install clean install -U

### Original TU git repo ###
* TU repository for basic project structure is located on git://hypnotoad.infosys.tuwien.ac.at/dst15/dst15.git
* for pulling changes do git pull tu master
* git remote -v -- list all remotes

### Links on other similar projects ### 
* https://github.com/eeezyy/dst14
* https://github.com/mwagnurr/DST2013 - full points of first assignment
* https://github.com/beikov/dst14
* https://bitbucket.org/8191/tu_dst/src/c3d63c58c225?at=master

### Webservice url - SOAP ###
* http://localhost:4204/ass2-ws/StatisticsService/service
* http://localhost:4204/ass2-ws/StatisticsService/service?wsdl

### SOAP test ###
* in WSBaseTest.closeContainer() add Thread.sleep(4*60 * 1000) to wait 4 minutes
* run SOAP-UI (by SmartBear) -> create project by adding wsdl url and run test.
  project is also sawed in remoting_artifacts
* capture pacets on loopback with wireshark

### EJB remote call test ###
* wireshark

