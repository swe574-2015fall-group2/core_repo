package com.boun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.boun.data.dbpedia.OWLClassHierarchy;

@SpringBootApplication
public class PinkElephantApiApplication {

    public static void main(String[] args) {
    	
    	//Load OWL RDF class hierarchy
    	OWLClassHierarchy.getInstance();
    	
        SpringApplication.run(PinkElephantApiApplication.class, args);
    } 
}