package spoon.filter;

import spoon.legacy.NameFilter;
import spoon.reflect.declaration.CtMethod;

public class CallFilter extends NameFilter<CtMethod<?>> {
    private final String methodName;
    private final String targetClassName;

    public CallFilter(String methodName, String targetClassName) {
        super(String.valueOf(CtMethod.class));
        this.methodName = methodName;
        this.targetClassName = targetClassName;
    }

    @Override
    public boolean matches(CtMethod<?> element) {
        return element.toString().contains(targetClassName + "." + methodName);
    }
}
