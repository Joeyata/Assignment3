import java.util.*;
import java.io.Serializable;

public class Email implements Serializable{
    private String label;
    // prepare promise proposal accept announce
    private int src;
    //source port, or the winner when label = "announce"
    private int promise;
    // include the highest previous proposal number accepted by this acceptor
    private int proposal_number;
    // the proposer number choose
    private String promise_state;
    //"prepare-ok" or "NACK"
    private String accept_state;
    // "accept" "accept-reject"

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
    public String get_label() {
        return this.label;
    }
    public int get_src() {
        return this.src;
    }
    public int get_promise() {
        return this.promise;
    }
    public int get_proposal_number() {
        return this.proposal_number;
    }
    public String get_promise_state() {
        return this.promise_state;
    }
    public String get_accept_state() {
        return this.accept_state;
    }
    public int get_accept_value() {
        return this.accept_value;
    }
}
