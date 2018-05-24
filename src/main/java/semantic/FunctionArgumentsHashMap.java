package semantic;

import node.VariableType;
import token.FunctionType;

import java.util.HashMap;
import java.util.Map;

public class FunctionArgumentsHashMap {
        public static final Map<FunctionType, VariableType[]> FUNCTION_ARGUMENTS = new HashMap<FunctionType, VariableType[]>() {{
            put(FunctionType.CHANGE_COLOR, new VariableType[]{VariableType.INT,VariableType.INT,VariableType.INT});
            put(FunctionType.GO, new VariableType[]{VariableType.INT,VariableType.INT});
            put(FunctionType.GO_LEFT, new VariableType[]{VariableType.INT});
            put(FunctionType.GO_RIGHT, new VariableType[]{VariableType.INT});
            put(FunctionType.GO_UP, new VariableType[]{VariableType.INT});
            put(FunctionType.GO_DOWN, new VariableType[]{VariableType.INT});
            put(FunctionType.ROTATE_LEFT, new VariableType[]{VariableType.INT});
            put(FunctionType.ROTATE_RIGHT, new VariableType[]{VariableType.INT});
            put(FunctionType.CHANGE_SIZE, new VariableType[]{VariableType.INT});
            put(FunctionType.WAIT, new VariableType[]{VariableType.INT});
            put(FunctionType.TALK, new VariableType[]{VariableType.STRING});
            put(FunctionType.GET_X, new VariableType[]{});
            put(FunctionType.GET_Y, new VariableType[]{});
            put(FunctionType.GET_ROTATION, new VariableType[]{});
            put(FunctionType.GO_TO_MOUSE, new VariableType[]{});
        }};
}
