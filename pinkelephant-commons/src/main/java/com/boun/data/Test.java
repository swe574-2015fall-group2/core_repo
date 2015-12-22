package com.boun.data;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.List;

import com.boun.data.dbpedia.OWLClassHierarchy;
import com.boun.data.dbpedia.OWLClassHierarchy.Element;
import com.boun.data.dbpedia.OWLClassHierarchy.Node;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.VCARD;

public class Test {

	public static void main(String args[]) {

		test3();

	}

	private static void test3() {
		String file = "C:/Users/mehmetce/Desktop/test/category-labels_en.ttl";

		Model model = FileManager.get().loadModel(file, null, "TURTLE");

		StmtIterator iter = model.listStatements();
		try {
			while (iter.hasNext()) {
				Statement stmt = iter.next();

				Resource s = stmt.getSubject();
				Resource p = stmt.getPredicate();
				RDFNode o = stmt.getObject();

				System.out.println("subject->" + s.getURI() + "--predicate->" + p.getURI() + "--node->" + o.asResource().getLocalName());

			}
		} finally {
			if (iter != null)
				iter.close();
		}
	}

	private static void test1() {
		Hashtable<String, Node> hierarchy = OWLClassHierarchy.getInstance().getHierarchy();

		for (String clazz : hierarchy.keySet()) {

			Node node = hierarchy.get(clazz);

			StringBuffer buffer = new StringBuffer();

			List<Element> childList = node.getChildList();
			for (Element string : childList) {
				if (buffer.length() != 0) {
					buffer.append(",");
				}
				buffer.append(string.getLabel());
			}

			if (node.getParent().getLabel().contains("TopicalConcept")) {
				System.out.println("Clazz->" + clazz + ", label->" + node.getLabel() + ", parent->"
						+ node.getParent().getLabel() + ", child->" + buffer.toString());
			}

		}

		boolean result = OWLClassHierarchy.getInstance().isChild("http://dbpedia.org/ontology/ProgrammingLanguage",
				"http://www.w3.org/2002/07/owl#Thing");

		System.out.println(result);
	}

	private static void test2() throws FileNotFoundException {

		String folder = "C:/Users/mehmetce/Desktop/test/";

		String personURI = "http://localhost/hrudya";
		String givenName = "GOPIKA";
		String familyName = "NG";
		String fullName = givenName + " " + familyName;
		String course1 = "http://localhost/relationship/";
		try {
			// create an empty model
			Model model = ModelFactory.createDefaultModel();
			// create the resource and add the properties cascading style
			Resource hrudya = model.createResource(personURI);
			Property course = model.createProperty(course1, "course");
			hrudya.addProperty(VCARD.FN, fullName);
			hrudya.addProperty(VCARD.CLASS, "Person");
			hrudya.addProperty(VCARD.Given, givenName);
			hrudya.addProperty(DC.title, "SEMANTIC WEB");
			hrudya.addProperty(course, "M.Tech_CSE");
			// model.write(new PrintWriter(System.out));
			FileOutputStream fout = new FileOutputStream(folder + "rr.rdf");
			model.write(fout);

			FileOutputStream fout1 = new FileOutputStream(folder + "hr.xml");
			model.write(fout1);

			Resource iterator = model.getResource(personURI);

		} catch (Exception e) {
			System.out.println("Failed: " + e);
		}
	}
}
