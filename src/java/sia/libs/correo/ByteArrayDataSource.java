package sia.libs.correo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileNotFoundException;
import javax.activation.DataSource;

class ByteArrayDataSource implements DataSource {
  
  private byte[] bytes;
  private String contentType, name;
  
  public ByteArrayDataSource(byte[] bytes, String contentType, String name) {
    this.bytes= bytes;
    this.contentType= contentType== null? "application/octet-stream": contentType;
    this.name= name;
  }
  
  public String getContentType() {
    return contentType;
  }
  
  public InputStream getInputStream() {
    return new ByteArrayInputStream(bytes);
  }
  
  public String getName() {
    return name;
  }
  
  public OutputStream getOutputStream() throws IOException {
    throw new FileNotFoundException();
  }
}
