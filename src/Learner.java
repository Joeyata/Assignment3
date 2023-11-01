import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Learner{

    public static void learner_announce(int address, Email email)
    {
        try {
            Socket acceptor = new Socket("localhost", address);
            ObjectOutputStream oos = new ObjectOutputStream(acceptor.getOutputStream());
            oos.writeObject(email);
            oos.close();
            acceptor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(4120);
        while (true) {
            Socket socket = serverSocket.accept(); // 等待客户端连接

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Email receivedEmail = (Email) ois.readObject();

            if(receivedEmail.get_label().equals("announce"))
            {
                learner_announce(4111, receivedEmail);
                learner_announce(4112, receivedEmail);
                learner_announce(4113, receivedEmail);
                learner_announce(4114, receivedEmail);
                learner_announce(4115, receivedEmail);
                learner_announce(4116, receivedEmail);
                learner_announce(4117, receivedEmail);
                learner_announce(4118, receivedEmail);
                learner_announce(4119, receivedEmail);
                System.out.println("Member " + receivedEmail.get_src() + " win the election! over");
                break;
            }
        }


    }
}