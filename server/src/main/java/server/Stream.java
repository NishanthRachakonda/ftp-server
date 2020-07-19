package server;
import java.io.*;

public class Stream {
	FileInputStream fileInput = null;

	public void fileDownload(String upFile) {
		File source = new File(upFile);

		InputStream input   = null;
		int length;
		try {
			input  = new FileInputStream(source);
			byte[] buffer = new byte[1024];
			while ((length = input.read(buffer)) > 0) {
				// os.write(buffer, 0, length);
				System.out.print(new String(buffer, 0, length));
			}
		} catch(Exception error){
			error.printStackTrace();
		} finally {
			try {
				input.close();				
			} catch (Exception error) {
				error.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		Stream stream = new Stream();
		stream.fileDownload("/home/nishanth/1.txt");
	}
}