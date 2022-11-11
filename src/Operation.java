public class Operation {
    private avltree.Op opcode;
    private Integer val1;
    private Integer val2;

    public Operation(avltree.Op opcode, Integer val1, Integer val2) {
        this.opcode = opcode;
        this.val1 = val1;
        this.val2 = val2;
    }

    public Operation(avltree.Op opcode, Integer val1) {
        this.opcode = opcode;
        this.val1 = val1;
    }

    public Operation(avltree.Op opcode) {
        this.opcode = opcode;
    }

    public avltree.Op getOpcode() {
        return this.opcode;
    }

    public Integer getVal1() {
        return this.val1;
    }

    public Integer getVal2() {
        return this.val2;
    }

    public String toString() {
        String str = opcode.toString();
        if (val1 != null) {
            str = str + " " + val1;
        }
        if (val2 != null) {
            str = str + " " + val2;
        }

        return str;
    }
}