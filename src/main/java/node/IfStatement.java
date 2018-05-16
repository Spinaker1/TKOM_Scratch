package node;

public class IfStatement extends Node {
    private Block codeBlock;
    private Condition condition;

    public IfStatement(Block codeBlock, Condition condition) {
        this.codeBlock = codeBlock;
        this.condition = condition;
        this.nodeType = NodeType.IF_STATEMENT;
    }

    public Block getCodeBlock() {
        return codeBlock;
    }

    public Condition getCondition() {
        return condition;
    }
}
