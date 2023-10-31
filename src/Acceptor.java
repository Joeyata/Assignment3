import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Acceptor {
    public static int max_accept;
    public static int max_received;
    public static String accept_value;
    public Acceptor()
    {
        this.max_accept = -1;
        this.max_received = -1;
        accept_value = "not exist";
    }

    public static boolean hear_proposal(int address)
    {
        try {
            ServerSocket serverSocket = new ServerSocket(address); // 监听端口

            while (true) {
                Socket socket = serverSocket.accept(); // 等待客户端连接
                System.out.println("客户端已连接...");

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Email receivedEmail = (Email) ois.readObject(); // 从客户端接收Email对象

                if(receivedEmail.get_label() == "prepare") {
                    if (receivedEmail.get_proposal_number() > max_received) {
                        Email promise = new Email("promise", address, max_accept,
                                receivedEmail.get_proposal_number(), "prepare_ok", null, null, accept_value);
                        send_promise(receivedEmail.get_src(), promise);
                    } else {
                        Email promise = new Email("promise", address, max_accept,
                                receivedEmail.get_proposal_number(), "NACK", null, null, accept_value);
                    }
                }

                if(receivedEmail.get_label() == "proposal") {

                }
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void send_promise(int address, Email email) throws IOException {
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

    public static void send_accept(int address, Email email) throws IOException {
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


    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("please give port number 4111-4113");
        }

        /*
        Thread m5 = new Thread(() -> hear_response(4115));
        Thread m6 = new Thread(() -> hear_response(4116));
        Thread m7 = new Thread(() -> hear_response(4117));
        Thread m8 = new Thread(() -> hear_response(4118));
        Thread m9 = new Thread(() -> hear_response(4119));

        m4.start();
        m5.start();
        m6.start();
        m7.start();
        m8.start();
        m9.start();

         */
    }
}