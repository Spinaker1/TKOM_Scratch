package node;

public class RepeatIfStatement extends Node {
    private Block codeBlock;
    private Expression condition;

    public RepeatIfStatement(Block codeBlock, Expression condition) {
        this.codeBlock = codeBlock;
        this.condition = condition;
        this.nodeType = NodeType.REPEAT_IF_STATEMENT;
    }

    public Block getCodeBlock() {
        return codeBlock;
    }

    public Expression getCondition() {
        return condition;
    }
}
