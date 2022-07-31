package model.poet;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;

import javax.lang.model.element.Modifier;

public class ClassesPropertyFromRDF{
    // this is just printing name od classes
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
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

        String q = prefixes +"SELECT ?class ?property WHERE{\n" +
                "?class ^rdfs:domain ?property\t\t   \n" +
                "\n" +
                "}";
        Dataset dataset = DatasetFactory.createTxnMem();
        RDFConnection conn = RDFConnectionFactory.connect(dataset);
        conn.load("example.ttl");
        QueryExecution qExec = conn.query(q);
        ResultSet rs = qExec.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            //Resource literal = qs.getResource("?class ");
            Resource literals = qs.getResource("?property ");
            //System.out.println("Subject: " + literal.toString());
           System.out.println("prop: " + literals.toString());

        }
        qExec.close();
        conn.close();

//        for (int i = 0; i <list.size() ; i++){
//            TypeSpec helloWorld = TypeSpec.classBuilder(list.get(i))
//                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
//                    //.addMethod(main)
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
    }
}