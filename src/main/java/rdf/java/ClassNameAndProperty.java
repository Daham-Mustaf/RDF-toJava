package rdf.java;

import java.util.List;

public class ClassNameAndProperty {
    private String className;
    private String classfields;

    public ClassNameAndProperty(String className, String classfields) {
        this.className = className;
        this.classfields = classfields;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassfields() {
        return classfields;
    }

    public void setClassfields(String classfields) {
        this.classfields = classfields;
    }

    @Override
    public String toString() {
        return "ClassNameAndProperty{" +
                "className='" + className + '\'' +
                ", classfields='" + classfields + '\'' +
                '}';
    }
}
