package node;

public class IfStatement extends Node {
    private boolean isElse = false;
    private Block codeBlock;
    private Block elseCodeBlock;
    private Condition condition;

    public void setElse(boolean anElse) {
        isElse = anElse;
    }

    public Block getCodeBlock() {
        return codeBlock;
    }

    public void setCodeBlock(Block codeBlock) {
        this.codeBlock = codeBlock;
    }

    public Block getElseCodeBlock() {
        return elseCodeBlock;
    }

    public void setElseCodeBlock(Block elseCodeBlock) {
        this.elseCodeBlock = elseCodeBlock;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }
}
