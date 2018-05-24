package structures.node.loop;

import structures.node.Node;
import structures.node.NodeType;
import structures.node.block.Block;

public class RepeatStatement extends Node {
    private Block codeBlock;
    private int repeatingCount;

    public RepeatStatement(Block codeBlock, int repeatingCount) {
        this.codeBlock = codeBlock;
        this.repeatingCount = repeatingCount;
        this.nodeType = NodeType.REPEAT_STATEMENT;
    }

    public Block getCodeBlock() {
        return codeBlock;
    }

    public int getRepeatingCount() {
        return repeatingCount;
    }
}
