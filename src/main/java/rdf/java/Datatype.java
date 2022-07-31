package rdf.java;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public enum Datatype {
    xsdshort("short"),
    xsdunsignedByte("unsignedByte"),
    xsdint("int"),
    xsdunsignedShort("unsignedShort"),
    xsdinteger("integer"),
    xsdnonPositiveInteger("nonPositiveInteger"),
    xsdnegativeInteger("negativeInteger"),
    xsdnonNegativeInteger("nonNegativeInteger"),
    xsdpositiveInteger("positiveInteger"),
    xsdlong("long"),
    xsdunsignedInt("unsignedInt"),
    xsdunsignedLong("unsignedLong"),
    xsddecimal("decimal"),
    xsdfloat("float"),
    xsddouble("double"),
    xsdbyte("byte"),
    xsdstring("string"),
    xsddateTime("dateTime"),
    unknown("");

    private String _name = "";
    //Constructeur
    Datatype(String name){
        this._name = name;
    }

    public String getIRI() {
        return "http://www.w3.org/2001/XMLSchema#integer"+_name;
    }

    public String toString(){
        return getIRI();
    }

    public static Datatype find(String iri){
        for (Datatype type : Datatype.values()) {
            if(type.getIRI().equals(iri)){
                return type;
            }
        }
        return Datatype.unknown;
    }
    private static Date toDateFormat(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
        return sdf.parse(date);
    }
}