package com.boun.data.dbpedia;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDFS;

import lombok.Data;

public class OWLClassHierarchy {

	private static OWLClassHierarchy instance = new OWLClassHierarchy();
	
	private Hashtable<String, Node> hierarchy = new Hashtable<String, Node>();
	
	private OWLClassHierarchy() {
		try{
			initialize();
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	
	public static OWLClassHierarchy getInstance() {
		return instance;
	}
	
	public Hashtable<String, Node> getHierarchy() {
		return hierarchy;
	}
	
	@Data
	public static class Node{
		private String label;
		private String uri;
		private Element parent;
		private List<Element> childList = new ArrayList<Element>();
	}
	
	@Data
	public static class Element{
		private String label;
		private String uri;
		
		private Element(String label, String uri){
			this.label = label;
			this.uri = uri;
		}
		
		public boolean equals(Object o){
			if(o == null){
				return false;
			}
			Element el = (Element)o;
			
			return (el.getUri().equalsIgnoreCase(this.getUri()));
		}
		
		@Override
		public int hashCode() {
			int code = 7;
			code = 13 * code * this.getUri().hashCode();
			return code;
		}
	}
	
	private void initialize() throws IOException{
		
		final Model dbpedia = ModelFactory.createDefaultModel();

		String os = System.getProperty("os.name");
		
		InputStream in = null;
		if(os.contains("Windows")){
			in = new FileInputStream(new File("C:/Users/mehmetce/git/core_repo/pinkelephant-commons/resources/dbpedia_2015-04.owl"));	
		}else{
			in = new FileInputStream(new File("/swe574/resources/dbpedia_2015-04.owl"));
		}
		
        
        dbpedia.read(in, "RDF/XML" );
        final StmtIterator stmts = dbpedia.listStatements(null, RDFS.subClassOf, (RDFNode) null);
        while ( stmts.hasNext() ) {
            final Statement stmt = stmts.next();
            
            String clazzUri = stmt.getSubject().getURI();
            String clazz = stmt.getSubject().getLocalName();
            String parentClazz = stmt.getObject().asResource().getLocalName();
            String parentClazzUri = stmt.getObject().asResource().getURI();
            
            processParent(parentClazz, parentClazzUri, clazz, clazzUri);
            processChild(parentClazz, parentClazzUri, clazz, clazzUri);
        }
	}
	
	private void processParent(String parentClazz, String parentClazzUri, String childClazz, String clazzUri){
		
		 Node parentNode = hierarchy.get(parentClazzUri);
         if(parentNode == null){
         	parentNode = new Node();
         	parentNode.setLabel(parentClazz);
         	parentNode.setUri(parentClazzUri);
         	parentNode.setParent(new Element(parentClazz, parentClazzUri));
         }
         parentNode.getChildList().add(new Element(childClazz, clazzUri));
         hierarchy.put(parentClazzUri, parentNode);
	}
	
	private void processChild(String parentClazz, String parentClazzUri, String childClazz, String clazzUri){
		
		Node node = hierarchy.get(clazzUri);
        if(node == null){
        	node = new Node();
        	node.setLabel(childClazz);
        	node.setUri(clazzUri);
        	node.setParent(new Element(parentClazz, parentClazzUri));
        }
        node.setParent(new Element(parentClazz, parentClazzUri));
        hierarchy.put(clazzUri, node);
	}
	
	public boolean isChild(String uri1, String uri2){
		
		Node node1 = getInstance().getHierarchy().get(uri1);
		Node node2 = getInstance().getHierarchy().get(uri2);
		
		if(node1.getParent().getLabel().equalsIgnoreCase(node2.getLabel())){
			return true;
		}
		
		for (Element element : node2.getChildList()) {
			if(node1.getLabel().equalsIgnoreCase(element.getLabel())){
				return true;
			}
		}
		
		for (Element element : node2.getChildList()) {
			if(isChild(uri1, element.getUri())){
				return true;
			}
		}
		
		return false;
	}
}
