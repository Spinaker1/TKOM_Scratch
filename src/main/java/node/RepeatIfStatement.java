package node;

public class RepeatIfStatement extends Node {
    private Block codeBlock;
    private Condition condition;

    public RepeatIfStatement(Block codeBlock, Condition condition) {
        this.codeBlock = codeBlock;
        this.condition = condition;
        this.nodeType = NodeType.REPEAT_IF_STATEMENT;
    }

    public Block getCodeBlock() {
        return codeBlock;
    }

    public Condition getCondition() {
        return condition;
    }
}
