package sia.utilerias.generador;

import java.io.OutputStream;
import java.io.Writer;

// Referenced classes of package sia.beans.utilerias:
//            Printer

class PrintWriter extends java.io.PrintWriter
    implements Printer
{

    public PrintWriter(OutputStream outputstream)
    {
        super(outputstream);
    }

    public PrintWriter(OutputStream outputstream, boolean flag)
    {
        super(outputstream, flag);
    }

    public PrintWriter(Writer writer)
    {
        super(writer);
    }

    public PrintWriter(Writer writer, boolean flag)
    {
        super(writer, flag);
    }
}