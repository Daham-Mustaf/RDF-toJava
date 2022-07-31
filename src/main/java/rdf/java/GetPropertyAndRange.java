package rdf.java;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;

import javax.lang.model.element.Modifier;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GetPropertyAndRange {
    public static void main(String[] args) throws IOException {
        List<String> list = new ArrayList<>();
        List<String> proplist = new ArrayList<>();
        List<String> proprange = new ArrayList<>();

        UtillRDF utillRDF = new UtillRDF();
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

        String propandRange = prefixes +"SELECT ?prop ?propRange WHERE {\n" +
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

        String fn="example.ttl";
        String getClassName = prefixes +"SELECT ?nameC WHERE{\n" +
                "?class a owl:Class.\t\t   \n" +
                "BIND(replace(replace(str(?class), \"^.*[/#]([^/#]+)/?\", \"$1\"), \"\\\\.|\\\\-\", \"_\")as ?nameC)\n" +
                "\t\t   \n" +
                "}";
        List<String> classname = utillRDF.connection(fn, getClassName);
        try {
            Model model= ModelFactory.createDefaultModel();
            model.read(new FileInputStream(fn),null,"TTL");
            Query query = QueryFactory.create(propandRange);
            QueryExecution qexec = QueryExecutionFactory.create(query, model);
            ResultSet resultSet = qexec.execSelect();
            // ResultSetFormatter.out( resultSet, query);
            List vars= resultSet.getResultVars();
            while (resultSet.hasNext()) {
                QuerySolution row = resultSet.next();
                proplist.add(row.get("?prop").toString());
                proprange.add(row.get("?propRange").toString());
                RDFNode propRange = row.get("?propRange");
                RDFNode prop = row.get("?prop");
              System.out.println(" propRange:"+ propRange);
              System.out.println(" ?prop:"+ prop.toString());
            }

            qexec.close();
            model.close();
        } catch (QueryParseException e){
            System.out.println(" query exception "+ e.getMessage());
            System.out.println("line "+e.getLine()+ " Column "+ e.getColumn());
        }

        for (int i = 0; i <classname.size() ; i++){
            FieldSpec android = FieldSpec.builder(String.class, proplist.get(i))
                    .addModifiers(Modifier.PRIVATE)
                    .build();

            TypeSpec helloWorld = TypeSpec.classBuilder(classname.get(i))
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addField(android)
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


