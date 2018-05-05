package node;

public class RepeatIfStatement extends Node {
    private Block codeBlock;
    private Condition condition;

    public Block getCodeBlock() {
        return codeBlock;
    }

    public void setCodeBlock(Block codeBlock) {
        this.codeBlock = codeBlock;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }
}
