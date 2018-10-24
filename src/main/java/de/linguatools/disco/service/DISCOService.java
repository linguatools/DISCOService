package de.linguatools.disco.service;

import de.linguatools.disco.DISCO;
import de.linguatools.disco.ReturnDataBN;
import de.linguatools.disco.WrongWordspaceTypeException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/disco")
public class DISCOService {
    
    @Context ServletContext context;
    private final ExecutorService executorService = java.util.concurrent.Executors
            .newCachedThreadPool();
    
    @GET
    @Produces(value = MediaType.TEXT_HTML)
    @Path(value = "/{lang}/similar-words/{word}")
    public void sayHtmlHello(@Suspended final AsyncResponse asyncResponse, 
            @PathParam(value = "lang") final String lang,
            @PathParam(value = "word") final String word) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doSayHtmlHello(lang, word));
            }
        });
    }

    private String doSayHtmlHello(@PathParam("lang") String lang, @PathParam("word") String word) {
        
        String[] result = doGetSimilarWords(lang, word);
        
        if( result == null ){
            return "<html> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
                    + "<title>" + "DISCO " +word+ "</title>"
                    + "<body><h1>" + "Das Wort " + word + " wurde in "+lang+" nicht gefunden."
                    + "</body></h1>" + "</html> ";
        }
        StringBuilder html = new StringBuilder();
        html.append("<html><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
                + "<title>DISCO</title><body><h1>").append(word).append("</h1>");
        for(String w : result){
            html.append(w).append(", ");
        }
        html.append("</body></html>");
        return html.toString();
    }

    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    @Path(value = "/{lang}/similar-words/{word}")
    public void getSimilarWords(@Suspended final AsyncResponse asyncResponse, 
            @PathParam(value = "lang") final String lang, 
            @PathParam(value = "word") final String word) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doGetSimilarWords(lang, word));
            }
        });
    }

    private String[] doGetSimilarWords(String lang, String word) {
        
        lang = lang.toLowerCase();
        word = word.trim().replaceAll("\\s+", "_");
        
        DISCO disco = (DISCO) context.getAttribute(lang);
        ReturnDataBN result = null;
        try {
            result = disco.similarWords(word);
        } catch (IOException | WrongWordspaceTypeException ex) {
            System.err.println(ex+" for ctx param "+lang+" in DISCOService.doGetSimilarWords");
        }
        if( result == null ){
            return new String[0];
        }
        return result.words;
    }
    
    /*
    DISCO:
    frequency
    semanticSimilarity
    
    Compositionality:
    - find most similar words for a phrase or sentence
    
    TextSimilarity:
    textSimilarity
    
    Cluster:
    growSet

    */
    
    
}
