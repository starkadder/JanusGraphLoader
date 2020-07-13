package net.starkadder.utilities;

/**
 * My own exception class
 */
public class StarkadderException extends Exception {

    public StarkadderException(String s, Exception e) {
        super(s,e);
    }
    public StarkadderException(Exception e) {
        super(e);
    }
    public StarkadderException(String s) {
        super(s);
    }

}