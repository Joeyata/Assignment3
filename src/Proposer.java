import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Proposer {
    public int cnt;
    public int max_accept_received;
    public int get_cnt()
    {
        return this.cnt++;
    }
    public Proposer()
    {
        this.cnt = 0;
        this.max_accept_received = -1;
    }

    public synchronized boolean hear_promise(int address)
    {
        try {
            ServerSocket serverSocket = new ServerSocket(address); // 监听端口

            while (true) {
                Socket socket = serverSocket.accept(); // 等待客户端连接
                System.out.println("客户端已连接...");

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Email receivedEmail = (Email) ois.readObject();
                if(receivedEmail.get_label() == "promise")
                {

                }

                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean hear_accept(int address)
    {
        try {
            ServerSocket serverSocket = new ServerSocket(address); // 监听端口

            while (true) {
                Socket socket = serverSocket.accept(); // 等待客户端连接


                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Email receivedEmail = (Email) ois.readObject();

                if(receivedEmail.get_label() == "accept")
                {
                    return true;
                }

                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void send_proposal(int address, Email email) throws IOException {
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

    public synchronized boolean send_prepare(int address, Email email) throws IOException {
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

    public synchronized void begin_proposal(int port_number) throws IOException {
        try {
            Email email = new Email("prepare", port_number, 0, get_cnt(), null, null, null, null);
            Thread m4 = new Thread(() -> {
                try {
                    send_proposal(4114, email);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            Thread m5 = new Thread(() -> {
                try {
                    send_proposal(4115, email);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            Thread m6 = new Thread(() -> {
                try {
                    send_proposal(4116, email);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        if(args.length < 1) {
            System.out.println("please give port number 4111-4113");
        }
        Proposer proposer = new Proposer();
        Email email = new Email("1",2, 3, args[0], null, null, null);
        //send_proposal(Integer.parseInt(args[0]), email);
        /*
        Thread m4 = new Thread(() -> begin_proposal(4114));
        Thread m5 = new Thread(() -> begin_proposal(4115));
        Thread m6 = new Thread(() -> begin_proposal(4116));
        Thread m7 = new Thread(() -> begin_proposal(4117));
        Thread m8 = new Thread(() -> begin_proposal(4118));
        Thread m9 = new Thread(() -> begin_proposal(4119));

        m4.start();
        m5.start();
        m6.start();
        m7.start();
        m8.start();
        m9.start();

         */
    }
}