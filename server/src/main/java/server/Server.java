package server;
import java.io.*;
import java.net.*;

public class Server { 
	private Socket socket           = null;
	private ServerSocket server     = null;
	private DataInputStream input   = null; 
	private DataOutputStream output = null; 

	public Server(final int port) throws IOException {
		server = new ServerSocket(port);
	}

	public void serve() throws IOException {
		while (true) {
			try {
				socket = server.accept();
				System.out.println("Connected to a client: " + socket);

				input = new DataInputStream(socket.getInputStream());
				output = new DataOutputStream(socket.getOutputStream());
				System.out.println("Assigning new thread for this client");

				final Thread runner = new ClientHandler(socket, input, output);
				runner.start();
			} catch (final Exception error) {
				server.close();
				error.printStackTrace();
			}
		}
	}

	public static void main(final String[] args) throws IOException {
		if(args.length != 1){
			System.err.println("Usage: java -jar file.jar <port>");
			System.exit(1);
		}
		final Server server = new Server(Integer.parseInt(args[0]));
		server.serve();
	}
}

class ClientHandler extends Thread {
	final DataInputStream input;
	final DataOutputStream output;
	final Socket socket;
	Parse parser;
	String current_directory = "/mnt/data/";

	public ClientHandler(final Socket socket, final DataInputStream input, final DataOutputStream output) {
		this.input = input;
		this.output = output;
		this.socket = socket;
		parser = new Parse(output, current_directory);
	}

	public String[] parseCommands(final String command) {
		final String[] parameters = command.split("\\s+");
		return parameters;
	}

	@Override
	public void run() {
		String received;
		String[] arguments;

		while (true) {
			try {
				final String directory = new String("client>" + current_directory + "$ ");
				output.write(directory.getBytes(), 0, directory.length());

				received = input.readUTF();
				arguments = parseCommands(received);

				if (arguments[0].equals("COPY"))
					parser.fileDownload(arguments);
				else if (arguments[0].equals("LIST"))
					parser.listFiles(arguments);
				else if (arguments[0].equals("DIR"))
					current_directory = parser.changeDirectory(arguments);
				else if (arguments[0].equals("ZIP"))
					parser.zipDirectory(arguments);
				else if (arguments[0].equals("CLOSE")) {
					System.out.println("Client " + this.socket + " sends exit...");
					System.out.println("Closing this connection.");
					this.socket.close();
					System.out.println("Connection closed");
					break;
				} else
					output.writeUTF("Invalid input\n");
				this.output.write("END".getBytes());
				input.readUTF();

			} catch (final IOException io_error) {
				io_error.printStackTrace();
			} catch (final Exception error) {
				error.printStackTrace();
			}
		}

		try {
			this.input.close();
			this.output.close();
		} catch (final IOException error) {
			error.printStackTrace(); 
		} 
	} 
} 
