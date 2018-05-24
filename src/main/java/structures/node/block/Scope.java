package structures.node.block;

import structures.node.assignable.variable.Variable;

import java.util.HashMap;
import java.util.Map;

public class Scope {
    private Map<String, Variable> variables;

    public Scope(Scope parentScope) {
        if (parentScope != null) {
            this.variables = new HashMap<>(parentScope.variables);
        }
        else {
            this.variables = new HashMap<>();
        }
    }

    public boolean containsVariable(String variableName) {
        return variables.containsKey(variableName);
    }

    public Variable getVariable(String variableName) {
        return variables.get(variableName);
    }

    public void putVariable(Variable variable) {
        variables.put(variable.getName(),variable);
    }
}
