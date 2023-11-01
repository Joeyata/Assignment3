import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Proposer {
    public static int cnt;
    public static int max_accept_received;
    public static int accept_count;
    public static int promise_count;
    public static int member;

    public static ServerSocket serverSocket;

    public static int get_cnt() {
        return ++cnt;
    }

    public Proposer() {
        this.cnt = 0;
        this.max_accept_received = -1;
        this.accept_count = 0;
        this.promise_count = 0;
        this.member = 0;
    }


    public static void hear_response() {
        try {

            while (true) {
                Socket socket = serverSocket.accept(); // 等待客户端连接

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Email receivedEmail = (Email) ois.readObject();

                if (receivedEmail.get_label().equals("promise")) {
                    System.out.println("receive a promise message from member " + (receivedEmail.get_src() - 4110));

                    if (receivedEmail.get_promise_state().equals("prepare-ok")) {
                        promise_count++;
                        max_accept_received = Math.max(max_accept_received, receivedEmail.get_promise());
                    }
                    if (promise_count == 4) {
                        Email proposal;
                        if (max_accept_received == -1) {
                            proposal = new Email("proposal", member, 0,
                                    cnt, null, null, member - 4110, null);
                        } else {
                            proposal = new Email("proposal", member, 0,
                                    max_accept_received, null, null, member - 4110, null);
                        }
                        send_proposal(4114, proposal);
                        send_proposal(4115, proposal);
                        send_proposal(4116, proposal);
                        send_proposal(4117, proposal);
                        send_proposal(4118, proposal);
                        send_proposal(4119, proposal);
                    }

                }


                if (receivedEmail.get_label().equals("accept")) {
                    System.out.println("receive an accept message from member " + (receivedEmail.get_src() - 4110));
                    if (receivedEmail.get_accept_state().equals("accept")) {
                        accept_count++;
                    }

                }
                if (accept_count == 4) {
                    System.out.println("Member " + receivedEmail.get_proposal_value() + " elected!");
                }

                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void send_proposal(int address, Email email) throws IOException {
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


    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length < 1) {
            System.out.println("please give port number 4111-4113");
        }

        member = Integer.parseInt(args[0]);
        serverSocket = new ServerSocket(member);
        Email email = new Email("prepare", member, 0, get_cnt(), null, null,
                member - 4110, null);
        System.out.println("Member" + (member - 4110) + " prepare " + cnt + " and send");
        try {
            send_proposal(4114, email);
            send_proposal(4115, email);
            send_proposal(4116, email);
            send_proposal(4117, email);
            send_proposal(4118, email);
            send_proposal(4119, email);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        hear_response();
    }
}

