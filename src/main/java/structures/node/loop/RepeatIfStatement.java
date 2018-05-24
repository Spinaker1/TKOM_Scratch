package structures.node.loop;

import structures.node.assignable.expression.Expression;
import structures.node.block.Block;
import structures.node.Node;
import structures.node.NodeType;

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
