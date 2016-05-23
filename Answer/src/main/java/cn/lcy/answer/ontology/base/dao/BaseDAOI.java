package cn.lcy.answer.ontology.base.dao;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;

public interface BaseDAOI {

	public static final OntModel model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
	
}
