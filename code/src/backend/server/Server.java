package backend.server;

import backend.server.communication.CommunicationMessage;
import debug.Debugger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.SocketTimeoutException;


public interface Server {


    /**
     * Used to send data through a socket.
     *
     * @param socketWriter         The socket output stream
     * @param communicationMessage The message to send
     * @throws IOException Exception if write has failed
     */
    default void sendData(BufferedWriter socketWriter, CommunicationMessage communicationMessage) throws IOException {
        Debugger.logMessage("Server sendData", "Sending following data: " + communicationMessage.toFormattedString());
        socketWriter.write(communicationMessage.toString());
        socketWriter.flush();
    }


    /**
     * Used to receive data from a socket.
     *
     * @param socketReader The socket input reader
     * @return The data
     * @throws IOException Exception if read has failed
     */
    default CommunicationMessage readData(BufferedReader socketReader)
            throws IOException, CommunicationMessage.InvalidMessageException, SocketDisconnectedException {
        String line;

        try {
            line = socketReader.readLine();
            if (line == null) {
                throw new SocketDisconnectedException();
            }

            return new CommunicationMessage(line);
        } catch (SocketTimeoutException e) {
            System.out.println("Socket read timeout !");
        }

        return null;
    }


    class ServerInitializationFailedException extends Exception {
        public ServerInitializationFailedException() {
            super();
        }

        public ServerInitializationFailedException(String message) {
            super(message);
        }
    }

    class SocketDisconnectedException extends Exception {
        public SocketDisconnectedException() {
            super();
        }

        public SocketDisconnectedException(String message) {
            super(message);
        }
    }


}
