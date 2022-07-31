package rdf.java;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.shared.PrefixMapping;

import javax.lang.model.element.Modifier;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String fn="example.ttl";
        String prefixes = StrUtils.strjoinNL
                ("PREFIX ex: <http://example.org/>"
                        , "PREFIX owl: <http://www.w3.org/2002/07/owl#> "
                        , "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                        , "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                        , "PREFIX sh: <http://www.w3.org/ns/shacl#>"
                        , "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
                        , "PREFIX shapes: <https://github.com/International-Data-Spaces-Association/InformationModel/tree/master/testing/> "
                        , ""
                );
        String getclassName = prefixes +"SELECT ?classname WHERE {\n" +
                "  ?class a owl:Class.\n" +
                "  BIND(replace(replace(str(?class), \"^.*[/#]([^/#]+)/?\", \"$1\"), \"\\\\.|\\\\-\", \"_\")as ?classname)\n" +
                "}\n";
        String getProperties = prefixes+ "SELECT ?prop WHERE {\n" +
                "\n" +
                "  {\n" +
                "\t ?property a owl:DatatypeProperty.\n" +
                "\t   OPTIONAL{\n" +
                "\t\t ?property\t rdfs:range ?pRang.\n" +
                "\t\t }\n" +
                "\t\t  BIND(if( !BOUND(?pRang)|| isBlank(?pRang), \"RdfResource\", ?pRang) AS ?range)\n" +
                "  }\n" +
                "\tUNION\n" +
                "\t{  ?property  a owl:ObjectProperty.\n" +
                "\t   OPTIONAL{\n" +
                "\t\t ?property\t rdfs:range ?pRang.\n" +
                "\t\t }\n" +
                "\t BIND(if( !BOUND(?pRang)|| isBlank(?pRang), \"RdfResource\", ?pRang) AS ?range)\n" +
                "\t}\n" +
                "  BIND(replace(replace(str(?property), \"^.*[/#]([^/#]+)/?\", \"$1\"), \"\\\\.|\\\\-\", \"_\")as ?prop)\n" +
                "  BIND(replace(replace(str(?range), \"^.*[/#]([^/#]+)/?\", \"$1\"), \"\\\\.|\\\\-\", \"_\")as ?propRange)\n" +
                "  }";

        UtillRDF utillRDF = new UtillRDF();
        List<String> classes = utillRDF.connection(fn, getclassName);
        List<String> properties = utillRDF.connection(fn, getProperties);
        //properties.stream().forEach(s -> System.out.println(s));
        List<ClassNameAndProperty> clsp= new ArrayList<>();
        for (int i = 0; i < classes.size(); i++) {
            clsp.add(new ClassNameAndProperty(classes.get(i), properties.get(i)));
        }
        clsp.stream().forEach(s -> System.out.println(s));



//        for (int i = 0; i <props.size() ; i++){
//            TypeSpec helloWorld = TypeSpec.classBuilder(props.get(i))
//                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
//                    //.addMethod(main)
//                    .addField(String.class, "greeting", Modifier.PRIVATE, Modifier.FINAL)
//                    .build();
//
//            JavaFile javaFile = JavaFile.builder("com.example", helloWorld)
//                    .addFileComment("Hi HelloWorld class ")
//                    .build();
//
//            try {
//                javaFile.writeTo(Paths.get("./src/main/java"));
//            }
//            catch (IOException ex){
//                System.out.println("exception oh no ..!" + ex.getMessage());
//            }
//        }


//        String q1 = prefixes + " SELECT  ?class ?name WHERE {\n"
//                + "  ?class a owl:Class		   \n"
//                + "BIND(replace(replace(str(?class), \"^.*[/#]([^/#]+)/?\", \"$1\"), \"\\\\.|\\\\-\", \"_\")as ?name)\n"
//                + "		   \n" + "}";
//        String fn="example.ttl";
//        Model model= ModelFactory.createDefaultModel();
//        model.read(new FileInputStream(fn),null,"TTL");
//
////        Dataset dataset = DatasetFactory.createTxnMem();
////        RDFConnection conn = RDFConnectionFactory.connect(dataset);
////        conn.load("example.ttl");
//  Query query = QueryFactory.create(q1);
//  QueryExecution qexec = QueryExecutionFactory.create(query, model);
//  ResultSet resultSet = qexec.execSelect();
//  PrefixMapping pm= query.getPrefixMapping();
// // ResultSetFormatter.out( resultSet, query);
//  List vars= resultSet.getResultVars();
//  while (resultSet.hasNext()){
//      QuerySolution qs = resultSet.nextSolution();
//      System.out.println("solutions_______");
//      for (int i = 0; i <vars.size() ; i++) {
//          String var= vars.get(i).toString();
//          RDFNode node=qs.get(var);
//          System.out.println(var +" "+ node.toString());
//          list.add(node.toString());
//          String txt;
//          if(node.isURIResource()){
//              txt= pm.shortForm(node.asNode().getURI());
//          }else {
//              txt= node.toString();
//
//          }
//          System.out.println("indise for loop " + var +"\t"+ txt);
//
//      }
//
//  }
// // list.stream().forEach(s-> System.out.println("list" + s));
////  MyOutputStream myOutputStream = new MyOutputStream();
////  ResultSetFormatter.out(myOutputStream, resultSet, query);
////  String sparqleResults= myOutputStream.getString();

    }
}
