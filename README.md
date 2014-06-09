About
=====
Repository contains some scripts to generate stuff for [Berlin Vegan](http://www.berlin-vegan.de) project.
Datasource are several Google Docs tables (https://developers.google.com/google-apps/spreadsheets/) , which are converted to different output formats.

* Website - static pages (location list, map, factsheets)
* JSON Files for the iOS/Android App
* KML file to import to Google Maps

Main Components
===============
FactsheetGenerator : generates the Factsheet for every restaurant
MapGenerator: generates the Berlin Vegan Map, including JavaScript
ListGenerator: generates the Restaurantlist
ExtJsStoreGenerator: generates the json files for the Berlin Vegan App
KMLGenerator: file to import to Google Maps


Build & Run
===========
    mvn assembly:assembly -DdescriptorId=jar-with-dependencies
    ./generate.sh

Deployment
===========
Deployment is done with ansible

ansible-playbook ansible/deploy.yml -i ansible/stage

Used Software
=============

    +- com.google.gdata:core:jar:1.47.1:compile
    |  +- com.google.guava:guava:jar:13.0.1:compile
    |  +- com.google.oauth-client:google-oauth-client-jetty:jar:1.11.0-beta:compile
    |  |  +- com.google.oauth-client:google-oauth-client-java6:jar:1.11.0-beta:compile
    |  |  |  \- com.google.oauth-client:google-oauth-client:jar:1.11.0-beta:compile
    |  |  |     \- com.google.http-client:google-http-client:jar:1.11.0-beta:compile
    |  |  |        +- org.apache.httpcomponents:httpclient:jar:4.0.3:compile
    |  |  |        |  +- org.apache.httpcomponents:httpcore:jar:4.0.1:compile
    |  |  |        |  +- commons-logging:commons-logging:jar:1.1.1:compile
    |  |  |        |  \- commons-codec:commons-codec:jar:1.3:compile
    |  |  |        \- xpp3:xpp3:jar:1.1.4c:compile
    |  |  \- org.mortbay.jetty:jetty:jar:6.1.26:compile
    |  |     +- org.mortbay.jetty:jetty-util:jar:6.1.26:compile
    |  |     \- org.mortbay.jetty:servlet-api:jar:2.5-20081211:compile
    |  +- com.google.code.findbugs:jsr305:jar:1.3.7:compile
    |  \- javax.mail:mail:jar:1.4:compile
    |     \- javax.activation:activation:jar:1.1:compile
    +- org.jsoup:jsoup:jar:1.7.1:compile
    +- com.googlecode.texhyphj:texhyphj:jar:1.2:compile
    +- junit:junit:jar:4.10:compile
    |  \- org.hamcrest:hamcrest-core:jar:1.1:compile
    +- commons-io:commons-io:jar:2.4:compile
    +- org.freemarker:freemarker:jar:2.3.19:compile
    +- commons-cli:commons-cli:jar:1.2:compile
    \- org.apache.commons:commons-lang3:jar:3.1:compile

License
=======
GPLv2

Tests
=======
[![Build Status](https://drone.io/github.com/smeir/berlin-vegan-tools/status.png)](https://drone.io/github.com/smeir/berlin-vegan-tools/latest)