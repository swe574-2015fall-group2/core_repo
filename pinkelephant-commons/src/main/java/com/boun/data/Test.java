package com.boun.data;

import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.List;

import com.boun.data.dbpedia.OWLClassHierarchy;
import com.boun.data.dbpedia.OWLClassHierarchy.Element;
import com.boun.data.dbpedia.OWLClassHierarchy.Node;

public class Test {

	public static void main(String[] args) throws FileNotFoundException {
        
		Hashtable<String, Node> hierarchy = OWLClassHierarchy.getInstance().getHierarchy();
		
		for (String clazz : hierarchy.keySet()) {
			
			Node node = hierarchy.get(clazz);
			
			StringBuffer buffer = new StringBuffer();
			
			List<Element> childList = node.getChildList();
			for (Element string : childList) {
				if(buffer.length() != 0){
					buffer.append(",");
				}
				buffer.append(string.getLabel());
			}
			
			if(node.getParent().getLabel().contains("TopicalConcept")){
				System.out.println("Clazz->" + clazz + ", label->" +node.getLabel() + ", parent->" + node.getParent().getLabel() + ", child->" + buffer.toString());	
			}
			
		}
		
		boolean result = OWLClassHierarchy.getInstance().isChild("http://dbpedia.org/ontology/ProgrammingLanguage", "http://www.w3.org/2002/07/owl#Thing");
		
		System.out.println(result);
		
    }
}
