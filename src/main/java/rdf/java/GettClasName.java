package rdf.java;

import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.shared.PrefixMapping;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.rdf.model.Literal;

import javax.lang.model.element.Modifier;

public class GettClasName {
    public static void main(String[] args) throws IOException {
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

        String getClassName = prefixes +"SELECT ?nameC WHERE{\n" +
                "?class a owl:Class.\t\t   \n" +
                "BIND(replace(replace(str(?class), \"^.*[/#]([^/#]+)/?\", \"$1\"), \"\\\\.|\\\\-\", \"_\")as ?nameC)\n" +
                "\t\t   \n" +
                "}";

        String fn="example.ttl";
        Model model= ModelFactory.createDefaultModel();
        model.read(new FileInputStream(fn),null,"TTL");
        Query query = QueryFactory.create(getClassName);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ResultSet resultSet = qexec.execSelect();
        //ResultSetFormatter.out( resultSet, query);
        List vars= resultSet.getResultVars();
        while (resultSet.hasNext()){
            QuerySolution qs = resultSet.nextSolution();
            System.out.println("solutions_______");
            for (int i = 0; i <vars.size() ; i++) {
                String var = vars.get(i).toString();
                RDFNode node = qs.get(var);
//                System.out.println(var + " " + node.toString());
                list.add(node.toString());
            }
        }
        list.stream().forEach(s-> System.out.println(s));
        qexec.close();
        model.close();
        for (int i = 0; i <list.size() ; i++){
            TypeSpec helloWorld = TypeSpec.classBuilder(list.get(i))
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    //.addMethod(main)
                    .build();

            JavaFile javaFile = JavaFile.builder("com.example", helloWorld)
                    .addFileComment("Hi HelloWorld class ")
                    .build();

            try {
                javaFile.writeTo(Paths.get("./src/main/java"));
            }
            catch (IOException ex){
                System.out.println("exception oh no ..!" + ex.getMessage());
            }
        }

    }
}
