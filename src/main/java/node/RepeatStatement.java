package node;

public class RepeatStatement extends Node {
    private Block codeBlock;

    public Block getCodeBlock() {
        return codeBlock;
    }

    public void setCodeBlock(Block codeBlock) {
        this.codeBlock = codeBlock;
    }
}
