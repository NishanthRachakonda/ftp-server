package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client { 
	Scanner scan              = null;
	Socket socket             = null;
	InetAddress ip_address    = null;
	BufferedInputStream input = null;
	DataOutputStream output   = null;
	FileOutputStream stream   = null;
	byte[] buffer             = new byte[1024];
	int port;
	String ip4_address;
  
	public Client(final String ip4_address, final int port) throws IOException {
		this.scan = new Scanner(System.in);
		ip_address = InetAddress.getByName(ip4_address);
		this.port = port;
	}

	public void connect() {
		try {
			socket = new Socket(ip_address, this.port);
			final BufferedInputStream input = new BufferedInputStream(socket.getInputStream());
			final DataOutputStream output = new DataOutputStream(socket.getOutputStream());

			while (true) {
				int length = input.read(buffer);
				String command;
				String[] arguments;

				System.out.print(new String(buffer, 0, length));
				command = this.scan.nextLine();
				output.writeUTF(command);
				arguments = command.split(" ");

				if (arguments[0].equals("CLOSE")) {
					System.out.println("Closing this connection : " + socket);
					socket.close();
					System.out.println("Connection closed");
					break;
				} else if(arguments[0].equals("COPY"))
					stream =  new FileOutputStream(arguments[2]);
				while ((length = input.read(buffer)) > 0) {
					final String received = new String(buffer, 0, length);
					final int length_str = received.length();
					if (length_str >= 3 && received.substring(length_str - 3, length_str).equals("END")) {
						output.writeUTF("END");
						if (arguments[0].equals("COPY")){
							stream.write(buffer, 0, received.substring(0, length_str - 3).getBytes().length);
							stream.close();
						}else{
							System.out.print(new String(buffer, 0, 
														  received.substring(0, 
														  length_str-3).getBytes().length));
						}
						break;
					} else if (arguments[0].equals("COPY"))
						stream.write(buffer, 0, length);
					else{
						System.out.print(new String(buffer, 0, length));
					}
				}
			}
			input.close();
			output.close();
		} catch (final IOException io_error) {
			io_error.printStackTrace();
		} catch (final Exception error) {
			error.printStackTrace();
		} finally {
			try {
				scan.close();
			} catch (final Exception error) {
				error.printStackTrace();
			}
		}
	}
	public static void main(final String[] args) throws IOException {
		if(args.length != 2){
			System.err.println("Usage: java -jar file.jar <ip_address> <port>");
			System.exit(1);
		}
		final Client client = new Client(new String(args[0]), Integer.parseInt(args[1]));
		client.connect();
	}
} 
