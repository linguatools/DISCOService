# DISCOService
REST API for DISCO

## Install
```
git clone https://github.com/linguatools/DISCOService.git
cd DISCOService
./gradlew build
```
This creates a WAR file in ```DISCOService/build/libs/``` that can be deployed to a web container, e.g. tomcat.

## Configuration

You have to tell DISCOService which word space(s) to load. Currently, this is done by editing the file
```DISCOService/src/main/webapp/WEB-INF/web.xml``` (and re-building the WAR file).  
Assuming you want to load an English word space file ```/Users/peter/DISCO/enwiki-20130403-word2vec-lm-mwl-lc-sim.denseMatrix```
then your ```web.xml``` should look like this:
```
<context-param>  
    <param-name>en</param-name>  
    <param-value>/Users/peter/DISCO/enwiki-20130403-word2vec-lm-mwl-lc-sim.denseMatrix</param-value>  
</context-param>
```  
The ```param-name``` will be part of the REST API path (see below).  
Make sure that tomcat has the permission to read your word space file!  
You can add more than one word space, provided that your machine has enough memory. The word spaces will be identified by their ```param-name```.

### Configuring Tomcat
Since word spaces are typically quite large, you have to increase Java heap size in Tomcat. Edit ```<your-tomcat-dir>/bin/setenv.sh```:
```
export CATALINA_OPTS="$CATALINA_OPTS -Xmx6g"
```
If you deployed to localhost, the service will be available at ```http://localhost:8080/DISCOService/rest/disco/{param-name}/similar-words/{word}```.

## API description

```<host>:<port>/DISCOService/rest/disco/{param-name}/similar-words/{word}```
calls [DISCO.similarWords](https://www.linguatools.de/disco/disco-api-3.0/de/linguatools/disco/DISCO.html#similarWords-java.lang.String-).  
Returns JSON array with similar words for ```{word}``` from the word space identified by ```{param-name}``` in the ```web.xml``` file.

more methods to be added...
