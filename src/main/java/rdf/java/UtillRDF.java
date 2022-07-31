package rdf.java;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class UtillRDF {
    List<String> list = new ArrayList<>();
   public List<String> connection(String filename , String query) throws FileNotFoundException {

       Model model= ModelFactory.createDefaultModel();
       model.read(new FileInputStream(filename),null,"TTL");
       Query q = QueryFactory.create(query);
       QueryExecution qexec = QueryExecutionFactory.create(q, model);
       ResultSet resultSet = qexec.execSelect();
       // ResultSetFormatter.out( resultSet, query);
       List vars= resultSet.getResultVars();
       while (resultSet.hasNext()){
           QuerySolution qs = resultSet.nextSolution();
           // System.out.println("solutions_______");
           for (int i = 0; i <vars.size() ; i++) {
               String var = vars.get(i).toString();
               RDFNode node = qs.get(var);
               //System.out.println(var + " " + node.toString());
              list.add(node.toString());
           }
       }

       qexec.close();
       model.close();
return list;
   }
}
