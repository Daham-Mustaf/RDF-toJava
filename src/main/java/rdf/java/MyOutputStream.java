package rdf.java;

import java.io.IOException;
import java.io.OutputStream;

public class MyOutputStream extends OutputStream {


    private StringBuffer sb;

    public MyOutputStream() {
        this.sb = new StringBuffer();
    }
    @Override
    public void write(int character) throws IOException {
        sb.append((char) character);

    }
    public String getString() {
        return sb.toString();
    }


}
