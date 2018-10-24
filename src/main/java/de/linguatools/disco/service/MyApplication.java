/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.linguatools.disco.service;

import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author peter
 */
public class MyApplication extends ResourceConfig {
    
    public MyApplication() {
          packages("de.linguatools.disco.service");
    }
}
