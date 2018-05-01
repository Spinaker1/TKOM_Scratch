package node;

public class IfStatement extends Node {
    private boolean isElse = false;

    public void setElse(boolean anElse) {
        isElse = anElse;
    }
}
