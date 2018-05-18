package node;

public class IfStatement extends Node {
    private Block codeBlock;
    private Expression condition;

    public IfStatement(Block codeBlock, Expression condition) {
        this.codeBlock = codeBlock;
        this.condition = condition;
        this.nodeType = NodeType.IF_STATEMENT;
    }

    public Block getCodeBlock() {
        return codeBlock;
    }

    public Expression getCondition() {
        return condition;
    }
}
