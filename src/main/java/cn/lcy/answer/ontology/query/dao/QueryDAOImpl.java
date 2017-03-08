package cn.lcy.answer.ontology.query.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.ontology.Individual;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.query.ResultSetRewindable;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.sparql.util.FmtUtils;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import cn.lcy.answer.enums.AnswerTypeEnum;
import cn.lcy.answer.ontology.base.dao.BaseDAOImpl;
import cn.lcy.answer.sem.model.Answer;
import cn.lcy.answer.sem.model.QueryResult;

@Repository("queryDAO")
public class QueryDAOImpl extends BaseDAOImpl implements QueryDAOI {
	
	/**
	 * 本体文件的路径
	 */
	public static String ontologySource = SOURCE + "Answer_Ontology_V2.owl";
	
	public static final Model loadModel = FileManager.get().readModel(model, ontologySource);
	
	/**
	 * TODO 测试
	 * @param args
	 */
	public static void main( String[] args ) {
      // String sameIndividual = new JenaDAOImpl().querySameIndividual("星仔");
      // System.out.println("sameIndividual" + sameIndividual);
		new QueryDAOImpl().getStatementsByObject("美人鱼");
    }
	
    @SuppressWarnings( value = "unused" )
    private static final Logger log = LoggerFactory.getLogger( QueryDAOImpl.class );

    // TODO 测试
    public void test() {
        String prefix = "prefix mymo: <" + PIZZA_NS + ">\n" +
                        "prefix rdfs: <" + RDFS.getURI() + ">\n" +
                        "prefix owl: <" + OWL.getURI() + ">\n";
        String QL = "SELECT ?导演姓名 WHERE { mymo:美人鱼  mymo:有导演  ?导演.\n"
        		+ "?导演  mymo:有姓名  ?导演姓名.}";
        String SPARQL = prefix + QL;
        showQuery(model, SPARQL);
        // TODO 将测试代码写在这
    }
    
    // TODO 测试
    public void showQuery(Model model, String q ) {
        Query query = QueryFactory.create(q);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        try {
            ResultSet results = qexec.execSelect();
            ResultSetFormatter.out(results, model);
        }
        finally {
            qexec.close();
        }
    }
    
    /**
     * TODO 暂时还没使用到 查询实体是否存在
     */
    public boolean individualExist(String individualName) {
    	// TODO 获取模型和载入本体数据的方法是否应该写成静态方法
        String individualNameUrl = PIZZA_NS + individualName;
    	Individual individual = model.getIndividual(individualNameUrl);
    	return individual == null ? false : true;
    }
    
    /**
     * 查询等价实体 如查询星爷的等价实体？ ————》周星驰
     */
    public String querySameIndividual(String individualName) {
    	String sameIndividual = null;
        String prefix = "prefix mymo: <" + PIZZA_NS + ">\n" +
                "prefix rdfs: <" + RDFS.getURI() + ">\n" +
                "prefix owl: <" + OWL.getURI() + ">\n";
		String QL = "SELECT ?等价实体   WHERE {?等价实体   owl:sameAs mymo:" + individualName + ".\n}";
		String SPARQL = prefix + QL;
		Query query = QueryFactory.create(SPARQL);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ResultSet results = qexec.execSelect();
        ResultSetRewindable resultSetRewindable = ResultSetFactory.makeRewindable(results);
        int numCols = resultSetRewindable.getResultVars().size();
        while(resultSetRewindable.hasNext()) {
        	QuerySolution querySolution = resultSetRewindable.next();
        	for (int col = 0; col < numCols;col++) {
	        	String rVar = results.getResultVars().get(col);
	        	RDFNode obj = querySolution.get(rVar);
	        	sameIndividual = FmtUtils.stringForRDFNode(obj).split(":")[1];
        	}
        	
        }
        return sameIndividual;
    }
    
    /**
     * TODO 可以使用listSameAs方法  查询等价实体 如查询周星驰的所有等价实体
     */
    public List<String> querySameIndividuals(String individualName) {
    	List<String> sameIndividuals = new ArrayList<String>();
        String prefix = "prefix mymo: <" + PIZZA_NS + ">\n" +
                "prefix rdfs: <" + RDFS.getURI() + ">\n" +
                "prefix owl: <" + OWL.getURI() + ">\n";
		String QL = "SELECT ?等价实体   WHERE {mymo:"  + individualName + " owl:sameAs ?等价实体.\n}";
		String SPARQL = prefix + QL;
		Query query = QueryFactory.create(SPARQL);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ResultSet results = qexec.execSelect();
        ResultSetRewindable resultSetRewindable = ResultSetFactory.makeRewindable(results);
        int numCols = resultSetRewindable.getResultVars().size();
        while(resultSetRewindable.hasNext()) {
        	QuerySolution querySolution = resultSetRewindable.next();
        	for (int col = 0; col < numCols;col++) {
	        	String rVar = results.getResultVars().get(col);
	        	RDFNode obj = querySolution.get(rVar);
	        	String sameIndividual = FmtUtils.stringForRDFNode(obj).split(":")[1];
	        	sameIndividuals.add(sameIndividual);
        	}
        	
        }
        return sameIndividuals;
    }
    

    /**
     * 查询所有以Subject为主语的断言
     */
	@Override
	public List<Statement> getStatementsBySubject(String subject) {
		List<Statement> statements = new ArrayList<Statement>();
        StmtIterator stmtIter = model.listStatements();
        while(stmtIter.hasNext()) {
        	Statement statement = stmtIter.next();
        	String subjectName = null;
        	if(statement.getSubject() != null && statement.getSubject().getURI() != null) {
        		String[] urlFields = statement.getSubject().getURI().split("#");
            	if(urlFields.length > 1) {
            		subjectName = urlFields[1];
            	} else {
            		subjectName =urlFields[0];
            	}
            	if(subjectName != null) {
            		if(subjectName.equals(subject)) {
            			statements.add(statement);
            		}
            	}
        	}
        }
        return statements;
	}
	
	/**
     * 查询所有以Object为宾语的断言
     */
	@Override
	public List<Statement> getStatementsByObject(String object) {
		List<Statement> statements = new ArrayList<Statement>();
        StmtIterator stmtIter = model.listStatements();
        while(stmtIter.hasNext()) {
        	Statement statement = stmtIter.next();
        	if(statement.getObject().toString().split("#").length > 1) {
        		if(statement.getObject().toString().split("#")[1].equals(object)) {
        			statements.add(statement);
        		}
        	}
        }
        return statements;
	}
    
    /**
     * 查询答案
     */
    public QueryResult queryOntology(String sparql) {
         String prefix = "prefix mymo: <" + PIZZA_NS + ">\n" +
                         "prefix rdfs: <" + RDFS.getURI() + ">\n" +
                         "prefix owl: <" + OWL.getURI() + ">\n";

         Query query = QueryFactory.create(  prefix + sparql );
         QueryExecution qexec = QueryExecutionFactory.create( query, model );
         QueryResult result = new QueryResult();
         try {
             ResultSet resultSet = qexec.execSelect();
             ResultSetRewindable resultSetRewindable = ResultSetFactory.makeRewindable(resultSet);
             int numCols = resultSetRewindable.getResultVars().size();
             List<Answer> answers = new ArrayList<Answer>();
             for ( ; resultSetRewindable.hasNext() ; ) {
                 QuerySolution rBind = resultSetRewindable.nextSolution();
                 for ( int col = 0 ; col < numCols ; col++ ) {
                     String rVar = resultSet.getResultVars().get(col);
                     RDFNode obj = rBind.get(rVar);
                     String answerStr = FmtUtils.stringForRDFNode(obj);
                     if(numCols > 1) {
                    	 Answer answer = new Answer();
                    	 answer.setType(AnswerTypeEnum.TABLE);
                    	 answer.setContent(answerStr);
                    	 answers.add(answer);
                     } else {
                    	 Answer answer = new Answer();
                    	 answer.setType(AnswerTypeEnum.STRING);
                    	 answer.setContent(answerStr);
                    	 answers.add(answer);
                     }
                 }
             }
             result.setAnswers(answers);
             return result;
         }
         finally {
             qexec.close();
         }
    }
    
    /**
     * 查询某一个本体的所有属性
     * @return 
     */
    public StmtIterator queryIndividualProperties(String individualName) {
    	String individualNameUrl = PIZZA_NS + individualName;
    	Individual individual = model.getIndividual(individualNameUrl);
    	StmtIterator properties = null;
    	if(individual != null) {
    		properties = individual.listProperties(); // 列出该实体所有的属性
    	}
    	return properties;
    }
    
    /**
     * 查询某一个实体的主要属性
     */
    @Override
	public List<Statement> queryIndividualMainProperties(String individualName) {
    	String individualNameUrl = PIZZA_NS + individualName;
    	Individual individual = model.getIndividual(individualNameUrl);
    	// 列出所有的对象属性
		// ExtendedIterator<ObjectProperty> object = m.listObjectProperties();
    	// 列出该实体所有的属性
    	StmtIterator properties = individual.listProperties();
    	List<Statement> mainProperties = new ArrayList<Statement>();
    	while(properties.hasNext()) {
    		Statement propertyStatement = properties.next();
    		RDFNode objectNode = propertyStatement.getObject();
    		// 对象属性 返回
    		if(!objectNode.isLiteral()) {
    			mainProperties.add(propertyStatement);
    		} else if(objectNode.toString().length() < 30){
    			mainProperties.add(propertyStatement);
    		}
    	}
    	return mainProperties;
	}
    
    public String queryIndividualComment(String individualName) {
    	String individualNameUrl = PIZZA_NS + individualName;
    	Individual individual = model.getIndividual(individualNameUrl);
    	String comment = individual.getComment(null);
    	return comment;
    }
    
    /**
     * TODO 暂时还未使用到 获取所有的实体
     */
    public ExtendedIterator<Individual> getAllIndividuals() {
    	return model.listIndividuals();
    }

	
}