import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Acceptor {
    public static int max_accept;
    public static int max_received;
    public static int accept_value;

    public static int member;

    public static ServerSocket serverSocket;
    public Acceptor()
    {
        max_accept = -1;
        max_received = -1;
        accept_value = 0;
        member = 0;
    }

    public static void hear_proposal()
    {
        try {
            while (true) {
                Socket socket = serverSocket.accept(); // 等待客户端连接

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Email receivedEmail = (Email) ois.readObject(); // 从客户端接收Email对象
                if(receivedEmail.get_label().equals("prepare")) {
                    System.out.println("receive a prepare ask message from member " + (receivedEmail.get_src() - 4110));
                    System.out.println(receivedEmail.get_src());
                    Email promise;
                    if (receivedEmail.get_proposal_number() > max_received) {
                        promise = new Email("promise", member, max_accept,
                                receivedEmail.get_proposal_number(), "prepare-ok", null, accept_value);
                    } else {
                        promise = new Email("promise", member, max_accept,
                                receivedEmail.get_proposal_number(), "NACK", null,  accept_value);
                    }
                    send_response(receivedEmail.get_src(), promise);
                }

                if(receivedEmail.get_label().equals("proposal")) {
                    System.out.println("receive a accept request message from member " + (receivedEmail.get_src() - 4110));
                    Email accept;
                    if (receivedEmail.get_proposal_number() == max_received) {
                        accept_value = receivedEmail.get_accept_value();
                        max_accept = max_received;
                        accept = new Email("accept", member, max_accept, receivedEmail.get_proposal_number(),
                                null, "accept",  accept_value);
                    } else {
                        accept = new Email("accept", member, max_accept, receivedEmail.get_proposal_number(),
                                "accept-reject", null,  accept_value);
                    }
                    send_response(receivedEmail.get_src(), accept);

                }

                if (receivedEmail.get_label().equals("announce")){
                    System.out.println("Member " + receivedEmail.get_src() + " win the election! over");
                    socket.close();
                    break;
                }

                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void send_response(int address, Email email) throws IOException {
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


    public static void main(String[] args) throws IOException {
        if(args.length < 1) {
            System.out.println("Usage: make launch-acceptor port_number=x");
            return ;
        }
        member = Integer.parseInt(args[0]) + 4110;
        if(member < 4113 || member > 4119)
        {
            System.out.println("port number should between 3 and 9");
            return ;
        }
        serverSocket = new ServerSocket(member);

        hear_proposal();
    }
}