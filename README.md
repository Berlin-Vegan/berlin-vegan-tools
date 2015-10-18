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
ExtJsStoreGenerator: generates the JavaScript files for the legacy Berlin Vegan App (phonegap)
KMLGenerator: file to import to Google Maps
GastroLocationJsonGenerator: generates json file with GastroLocations for the new Berlin-Vegan app (android native)
ShoppingLocationJsonGenerator: generates json file with ShoppingLocations for the new Berlin-Vegan app (android native)


Build & Run
===========
    mvn assembly:assembly -DdescriptorId=jar-with-dependencies
    ./generate.sh

Deployment
===========
Deployment is done with ansible

ansible-playbook ansible/deploy.yml -i ansible/stage

Access
===========

The app use OAuth2 to access the Berlin-Vegan Sheets. See https://developers.google.com/identity/protocols/OAuth2InstalledApp for details.

ask for authorization code
Browser: https://accounts.google.com/o/oauth2/auth?scope=https://spreadsheets.google.com/feeds&redirect_uri=urn:ietf:wg:oauth:2.0:oob&response_type=code&client_id=YOUR_CLIENT_ID
ask for access code
curl -X POST -d "AUTHORIZATION_CODE_FROM_LAST_REQUEST&client_id=YOUR_CLIENT_ID&client_secret=YOUR_CLIENT_SECRET&redirect_uri=urn:ietf:wg:oauth:2.0:oob&grant_type=authorization_code"  https://www.googleapis.com/oauth2/v3/token

Used Software
=============

    [INFO] +- com.google.gdata:core:jar:1.47.1:compile
    [INFO] |  +- com.google.guava:guava:jar:13.0.1:compile
    [INFO] |  +- com.google.oauth-client:google-oauth-client-jetty:jar:1.11.0-beta:compile
    [INFO] |  |  +- com.google.oauth-client:google-oauth-client-java6:jar:1.11.0-beta:compile
    [INFO] |  |  \- org.mortbay.jetty:jetty:jar:6.1.26:compile
    [INFO] |  |     +- org.mortbay.jetty:jetty-util:jar:6.1.26:compile
    [INFO] |  |     \- org.mortbay.jetty:servlet-api:jar:2.5-20081211:compile
    [INFO] |  +- com.google.code.findbugs:jsr305:jar:1.3.7:compile
    [INFO] |  \- javax.mail:mail:jar:1.4:compile
    [INFO] |     \- javax.activation:activation:jar:1.1:compile
    [INFO] +- com.google.api-client:google-api-client:jar:1.20.0:compile
    [INFO] |  +- com.google.oauth-client:google-oauth-client:jar:1.20.0:compile
    [INFO] |  +- com.google.http-client:google-http-client-jackson2:jar:1.20.0:compile
    [INFO] |  \- com.google.guava:guava-jdk5:jar:13.0:compile
    [INFO] +- com.fasterxml.jackson.core:jackson-core:jar:2.6.0-rc2:compile
    [INFO] +- com.google.http-client:google-http-client-jackson:jar:1.20.0:compile
    [INFO] |  +- com.google.http-client:google-http-client:jar:1.20.0:compile
    [INFO] |  |  \- org.apache.httpcomponents:httpclient:jar:4.0.1:compile
    [INFO] |  |     +- org.apache.httpcomponents:httpcore:jar:4.0.1:compile
    [INFO] |  |     +- commons-logging:commons-logging:jar:1.1.1:compile
    [INFO] |  |     \- commons-codec:commons-codec:jar:1.3:compile
    [INFO] |  \- org.codehaus.jackson:jackson-core-asl:jar:1.9.11:compile
    [INFO] +- org.jsoup:jsoup:jar:1.7.1:compile
    [INFO] +- com.googlecode.texhyphj:texhyphj:jar:1.2:compile
    [INFO] +- junit:junit:jar:4.10:compile
    [INFO] |  \- org.hamcrest:hamcrest-core:jar:1.1:compile
    [INFO] +- commons-io:commons-io:jar:2.4:compile
    [INFO] +- org.freemarker:freemarker:jar:2.3.22:compile
    [INFO] +- commons-cli:commons-cli:jar:1.2:compile
    [INFO] +- org.apache.commons:commons-lang3:jar:3.1:compile
    [INFO] \- com.google.code.gson:gson:jar:2.2.2:compile

License
=======
GPLv2

Tests
=======
[![Build Status](https://drone.io/github.com/smeir/berlin-vegan-tools/status.png)](https://drone.io/github.com/smeir/berlin-vegan-tools/latest)

Static Analysis
===============

Checkstyle violations can be suppressed with the annotation SuppressWarnings or with a special comment starting with NOCHECKSTYLE.

Example for module (i.e. rule) "LocalVariableName":

@SuppressWarnings("checkstyle:localvariablename")
int MY_INTEGER = 1; //NOCHECKSTYLE LocalVariableName

Note the casing and the missing space between // and NOCHECKSTYLE.
