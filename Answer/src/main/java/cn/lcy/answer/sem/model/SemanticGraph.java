package cn.lcy.answer.sem.model;

import edu.stanford.nlp.graph.DirectedMultiGraph;

public class SemanticGraph extends DirectedMultiGraph<SemanticGraphVertex, SemanticGraphEdge>{

	/**
	 * default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	public boolean printGraph() {
		StringBuilder s = new StringBuilder();
	    s.append("{\n");
	    s.append("Graph:\n");
	    for(SemanticGraphVertex sourceVertex : this.getAllVertices()) {
	    	for(SemanticGraphEdge edge : this.getOutgoingEdges(sourceVertex)) {
	    		SemanticGraphVertex destVertex = new SemanticGraphVertex();
	    		  for(SemanticGraphVertex v : this.getAllVertices()) {
	    			  for(SemanticGraphEdge e: this.getIncomingEdges(v)) {
	    				  if(e.equals(edge)) {
	    					  destVertex = v;
	    				  }
	    			  }
	    		  }
	    		s.append(sourceVertex.getWord().getName() + "(" + sourceVertex.getWord().getPostag() + ")");
	    		s.append("-----"+edge.getWord().getName() +"("+edge.getWord().getPostag() + ")"+"---->");
	    		s.append(destVertex.getWord().getName() +"("+destVertex.getWord().getPostag() +")");
	    		s.append("\n");
	    	}
	    }
	    s.append('}');
	    System.out.println("语义图结构如下:"+s.toString());
	    return true;
	}
}
