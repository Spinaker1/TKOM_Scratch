package node;

public class IfStatement extends Node {
    private Block codeBlock;
    private Block elseCodeBlock;
    private Condition condition;

    public IfStatement(Block codeBlock, Block elseCodeBlock, Condition condition) {
        this.codeBlock = codeBlock;
        this.elseCodeBlock = elseCodeBlock;
        this.condition = condition;
    }

    public Block getCodeBlock() {
        return codeBlock;
    }

    public Condition getCondition() {
        return condition;
    }
}
