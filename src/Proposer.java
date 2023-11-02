import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.*;

public class Proposer {
    public static int cnt;
    public static int max_accept_received;
    public static int accept_count;
    public static int promise_count;
    public static int proposal_value;
    public static int member;
    public static ServerSocket serverSocket;
    public static int stop;

    public static int get_cnt() {
        return ++cnt;
    }

    public Proposer() {
        cnt = 0;
        max_accept_received = -1;
        accept_count = 0;
        promise_count = 0;
        member = 0;
        proposal_value = member - 4010;
        stop = 0;
    }


    public static void hear_response() {
        try {
            long startTime = System.currentTimeMillis();
            while (true) {
                long executionTime = System.currentTimeMillis() - startTime;
                if(executionTime > 20000) break;  // The last proposal is not accepted in 20s, start the next one
                Socket socket = serverSocket.accept();
                Random random = new Random();
                int randomNumber = 0; // member 1 respond email immediately
                if (member == 4112) randomNumber = random.nextInt(3000) + 2000; //member 2 very busy, respond in 2-5s
                if (member == 4113) randomNumber = random.nextInt(3000); // member 3 normal busy, respond in 0-3s
                Thread.sleep(randomNumber);
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Email receivedEmail = (Email) ois.readObject();

                if (receivedEmail.get_label().equals("promise")) {
                    System.out.println("receive a promise message from member " +
                            (receivedEmail.get_src() - 4110) + " state: " + receivedEmail.get_promise_state());

                    if (receivedEmail.get_promise_state().equals("prepare-ok")) {
                        promise_count++;
                        if (promise_count <= 4) {
                            if (receivedEmail.get_promise() == -1) {
                                proposal_value = cnt;
                            } else {
                                max_accept_received = Math.max(max_accept_received, receivedEmail.get_promise());
                                proposal_value = max_accept_received;
                            }
                        }
                        if (promise_count == 4) {
                            Email proposal = new Email("proposal", member, 0,
                                    proposal_value, null, null, proposal_value);
                            System.out.println("Send proposal value " + proposal_value + " to all acceptors");

                            send_proposal(4114, proposal);
                            send_proposal(4115, proposal);
                            send_proposal(4116, proposal);
                            send_proposal(4117, proposal);
                            send_proposal(4118, proposal);
                            send_proposal(4119, proposal);
                        }
                    }

                }

                if (receivedEmail.get_label().equals("accept")) {
                    System.out.println("receive an accept message from member "
                            + (receivedEmail.get_src() - 4110) + " state: " + receivedEmail.get_accept_state());
                    if (receivedEmail.get_accept_state().equals("accept")) {
                        accept_count++;
                        if (accept_count == 4) {
                            Email email = new Email("announce", receivedEmail.get_accept_value(), 0, 0, null, null, 0);
                            report_learner(email);
                        }
                    }

                }

                if (receivedEmail.get_label().equals("announce")) {
                    System.out.println("Member " + receivedEmail.get_src() + " win the election! over");
                    socket.close();
                    stop = 1;
                    break;
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

    public static void report_learner(Email email) {
        try {
            Socket acceptor = new Socket("localhost", 4120);
            ObjectOutputStream oos = new ObjectOutputStream(acceptor.getOutputStream());
            oos.writeObject(email);
            oos.close();
            acceptor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void new_proposal() throws IOException {
        serverSocket = new ServerSocket(member);
        Email email = new Email("prepare", member, 0, get_cnt(), null, null, 0);
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
        serverSocket.close();
        if(stop == 0) new_proposal(); //the last proposal is failed, start a new proposal with a new value
    }


    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: make launch-acceptor member=x");
            return;
        }
        member = Integer.parseInt(args[0]) + 4110;
        if (member < 4111 || member > 4113) {
            System.out.println("port number should between 1 and 3");
            return;
        }
        new_proposal();
    }
}

