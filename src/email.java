import java.util.*;
import java.io.Serializable;

public class Email implements Serializable{
    private String label;
    // prepare promise proposal accept announce
    private int src;
    //source port
    private int promise;
    // include the highest previous proposal number accepted by this acceptor, only valid when label = 1.
    private int proposal_number;
    // the proposer number choose, valid when label = 0 or label = 2.
    private String promise_state;
    //"prepare-ok" or "NACK", valid when label = 1
    private String accept_state;

    private int accept_value;
    //the value of proposal_number represent


    public Email(String l, int sr, int pro, int pn, String ps, String as, int av) {
        this.label = l;
        this.src = sr;
        this.promise = pro;
        this.proposal_number = pn;
        this.promise_state = ps;
        this.accept_state = as;

        this.accept_value = av;
    }
    public synchronized String get_label() {
        return this.label;
    }
    public synchronized int get_src() {
        return this.src;
    }
    public synchronized int get_promise() {
        return this.promise;
    }
    public synchronized int get_proposal_number() {
        return this.proposal_number;
    }
    public synchronized String get_promise_state() {
        return this.promise_state;
    }
    public synchronized String get_accept_state() {
        return this.accept_state;
    }
    public synchronized int get_accept_value() {
        return this.accept_value;
    }
}
