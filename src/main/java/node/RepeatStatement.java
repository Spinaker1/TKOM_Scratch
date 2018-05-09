package node;

public class RepeatStatement extends Node {
    private Block codeBlock;
    private int repeatingCount;

    public RepeatStatement(Block codeBlock, int repeatingCount) {
        this.codeBlock = codeBlock;
        this.repeatingCount = repeatingCount;
    }

    public Block getCodeBlock() {
        return codeBlock;
    }

    public int getRepeatingCount() {
        return repeatingCount;
    }
}
