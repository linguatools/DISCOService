package de.linguatools.disco.service;

import de.linguatools.disco.CorruptConfigFileException;
import de.linguatools.disco.DISCO;
import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        
        ServletContext ctx = servletContextEvent.getServletContext();
         
        // hole alle context-params aus web.xml
        // alle in web.xml angegebenen DISCOs werden geladen
        // web.xml:
        //    <param-name>en</param-name>
        //    <param-value>enwiki-20130403-word2vec-lm-mwl-lc-sim.denseMatrix</param-value>
        Enumeration<String> initParamNames = ctx.getInitParameterNames();
        while( initParamNames.hasMoreElements() ){
            String paramName = initParamNames.nextElement(); // en
            String discoFile = ctx.getInitParameter(paramName);
            DISCO disco = null;
            try {
                disco = DISCO.load(discoFile);
            } catch (IOException | CorruptConfigFileException ex) {
                System.err.println(ex+" while initialising "+paramName);
            }
            ctx.setAttribute(paramName, disco);
            System.err.println(paramName+" initialized for Application.");
        }
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        
    }
}
