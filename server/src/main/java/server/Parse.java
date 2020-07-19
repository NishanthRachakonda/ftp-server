package server;
import java.io.*;
import java.nio.file.Paths;
import java.util.zip.*;

class Parse {
	final DataOutputStream output; 
	String current_directory;

    public Parse(final DataOutputStream output, final String current_directory) {
        this.output = output;
        this.current_directory = current_directory;
    }

    public void fileDownload(final String[] arguments) {
        String upFile;
        if (arguments.length == 3)
            upFile = current_directory + arguments[1];
        else
            return;

        final File source = new File(upFile);

        InputStream stream = null;
        int length;
        try {
            stream = new FileInputStream(source);
            final byte[] buffer = new byte[256];
            while ((length = stream.read(buffer)) > 0)
                this.output.write(buffer, 0, length);
        } catch (final Exception error) {
            error.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (final Exception error) {
                error.printStackTrace();
            }
        }
    }

    public void listFiles(final String[] arguments) {
        String upFile;
        if (arguments.length == 2)
            upFile = current_directory + arguments[1];
        else
            upFile = current_directory;

        final File source = new File(upFile);
        String[] files_list;

        try {
            files_list = source.list();
            for (final String file_path : files_list) 
                this.output.write((file_path+"\n").getBytes());
        } catch (final Exception error) {
            error.printStackTrace();
        }
    }

    public String changeDirectory(final String[] arguments) {
        String directory;
        if (arguments.length == 2) {
            directory = new String(current_directory + arguments[1]);
            if (arguments[1].equals("../") || arguments[1].equals(".."))
                directory = Paths.get(current_directory).getParent().toString();
            else if(arguments[1].equals("./") || arguments[1].equals("."))
                return current_directory;
            if (directory.charAt(directory.length() - 1) != '/')
                directory = directory + '/';
        } else
            return current_directory;

        final File directory_path = new File(directory);
        if (directory_path.exists() && directory_path.isDirectory())
            current_directory = directory;
        return current_directory;
    }

    public void zipDirectory(final String[] arguments) {
        try {
            final FileOutputStream fos = new FileOutputStream(current_directory + arguments[2]);
            final ZipOutputStream zipOut = new ZipOutputStream(fos);
            final File fileToZip = new File(current_directory + arguments[1]);
            Zip.zipFile(fileToZip, fileToZip.getName(), zipOut);
        } catch (final Exception error) {
            error.printStackTrace();
        }

    }
}