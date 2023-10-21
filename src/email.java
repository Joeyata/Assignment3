import java.util.*;
public class email {
    private int label;
    // proposal_prepare = 0, response_promise = 1, proposal_ask = 2, response_accept = 3
    private Vector<Integer> promise;
    // include all the previous proposal number accepted by this acceptor, only valid when label = 1.
    private int proposal_number;
    // the value proposer choose, valid when label = 0 or label = 2.
    private String promise_state;
    //"prepare-ok" or "NACK", valid when label = 1
    private String accept_state;
    //"accept-ok" or "accpet-reject", valid when label = 3

    public email(int l, Vector<Integer> pro, int pn, String ps, String as) {
        this.label = l;
        this.promise = pro;
        this.proposal_number = pn;
        this.promise_state = ps;
        this.accept_state = as;
    }
    public synchronized int get_label() {
        return this.label;
    }
    public synchronized Vector<Integer> get_promise() {
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
}
