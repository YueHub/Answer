package cn.lcy.answer.ontology.construction.dao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileManager;

import cn.lcy.answer.ontology.base.dao.BaseDAOImpl;

public class ConstructionBaseDAOImpl extends BaseDAOImpl implements ConstructionBaseDAOI {
	
	/**
	 * 本体文件的路径
	 */
	public static String ontologySource = SOURCE + "Answer_Ontology_V2.owl";
	
	// G://Programming//Git//Answer//Answer//src//main//webapp//front//answer_image
	public static final String PIC_SAVE_PATH = "D://answer_image";

	public static final Model loadModel = FileManager.get().readModel(model, ontologySource);
	
	@Override
	public OntClass createOntClass(String className) {
		return model.createClass(PIZZA_NS + className);
	}
	
	@Override
	public OntClass getOntClass(String ontClassName) {
		return model.getOntClass(PIZZA_NS + ontClassName);
	}
	
	
	@Override
	public void addSameAs(Individual individual, Individual aliasIndividual) {
		individual.addSameAs(aliasIndividual);
	}
	
	@Override
	public void addSubClass(OntClass ontClass, OntClass subClass) {
		
		ontClass.addSubClass(subClass);
	}
	
	@Override
	public void addSubClass(Individual individual, OntClass ontClass) {
		individual.addOntClass(ontClass);
	}
	
	@Override
	public Individual createIndividual(String individualId, OntClass genusClass) {
		return model.createIndividual(PIZZA_NS + individualId, genusClass);
	}
	
	@Override
	public Individual getIndividual(String individualId) {
		return model.getIndividual(PIZZA_NS + individualId);
	}
	
	@Override
	public boolean addComment(Individual individual, String comment) {
		Literal ccommentLiteral = model.createLiteral(comment);
		individual.addComment(ccommentLiteral);
		File file = new File(ontologySource);
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			model.write(fw, "RDF/XML-ABBREV");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if(fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean addDataProperty(Individual individual, String propertyName, String propertyValue) {
		if(propertyName == null || propertyValue == null) {
			return false;
		}
		DatatypeProperty property = model.createDatatypeProperty(PIZZA_NS + "有" + propertyName);
		individual.addProperty(property, propertyValue);
		File file = new File(ontologySource);

		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			model.write(fw, "RDF/XML-ABBREV");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if(fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return true;
	}
	
	@Override
	public boolean addDataProperties(Individual individual , List<String> propertyNames, List<String> propertyValues) {
		if(propertyNames == null || propertyValues == null) {
			return false;
		}
		for(int i = 0; i < propertyNames.size(); i++) {
			DatatypeProperty property = model.createDatatypeProperty(PIZZA_NS + "有" + propertyNames.get(i));
			individual.addProperty(property, propertyValues.get(i));
		}
		
		File file = new File(ontologySource);
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			model.write(fw, "RDF/XML-ABBREV");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if(fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return true;
	}

	@Override
	public Resource addObjectProperty(Individual individualStart, String objectPropertyName, Individual individualEnd) {
		if(individualStart == null || objectPropertyName == null || individualEnd == null) {
			return null;
		}
		ObjectProperty property = model.createObjectProperty(PIZZA_NS + objectPropertyName);
		Resource resource = individualStart.addProperty(property, individualEnd);
		
		File file = new File(ontologySource);
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			model.write(fw, "RDF/XML-ABBREV");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return resource;
	}

	@Override
	public List<Resource> addObjectProperties(Individual individualStart, List<String> objectPropertyNames, List<Individual> individualEnds) {
		if(individualStart == null || objectPropertyNames == null || individualEnds == null) {
			return null;
		}
		int index = 0;
		List<Resource> resources = new ArrayList<Resource>();
		for(Individual individualEnd : individualEnds) {
			ObjectProperty property = model.createObjectProperty(PIZZA_NS + objectPropertyNames.get(index));
			Resource resource = individualStart.addProperty(property, individualEnd);
			resources.add(resource);
			++index;
		}
		return resources;
	}
}