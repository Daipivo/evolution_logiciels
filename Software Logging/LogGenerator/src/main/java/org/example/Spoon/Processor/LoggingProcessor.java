package org.example.Spoon.Processor;

import org.example.Spoon.SpoonProcessor;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.CtType;

public class LoggingProcessor extends SpoonProcessor {

    public LoggingProcessor(String projectPath) {
        super(projectPath);
    }

    @Override
    public void setParser(String projectPath) {
        super.setParser(projectPath);
        parser.addProcessor(new AbstractProcessor<CtMethod<?>>() {
            @Override
            public void process(CtMethod<?> method) {
                if (isControllerMethod(method)) {
                    String logStatementCode = createLogStatement(method);
                    CtCodeSnippetStatement logStatement = getFactory().createCodeSnippetStatement(logStatementCode);
                    if (method.getBody() != null) {
                        method.getBody().insertBegin(logStatement);
                    }
                }
            }

            private boolean isControllerMethod(CtMethod<?> method) {
                CtType<?> declaringType = method.getDeclaringType();
                return declaringType != null && declaringType.getQualifiedName().equals("com.example.TPLogging.controller.ProductController");
            }

            private String createLogStatement(CtMethod<?> method) {
                String generalActionType = getGeneralActionType(method);
                String specificActionType = getSpecificActionType(method);
                String productIdSnippet = getProductIdSnippet(method);
                return "logger.info(\"{\\\"timestamp\\\": \\\"{}\\\", \\\"action\\\": \\\"" + generalActionType
                        + "\\\", \\\"event\\\": \\\"" + specificActionType + "\\\", \\\"userId\\\": \\\"{}\\\""
                        + productIdSnippet + "}\", new java.util.Date(), userId);";
            }

            private String getGeneralActionType(CtMethod<?> method) {
                if (method.getAnnotations().stream().anyMatch(a -> a.getType().getSimpleName().equals("GetMapping"))) {
                    return "read";
                } else {
                    return "write";
                }
            }

            private String getSpecificActionType(CtMethod<?> method) {
                if (method.getAnnotations().stream().anyMatch(a -> a.getType().getSimpleName().equals("GetMapping"))) {
                    // Vérifiez si la méthode a un seul paramètre qui pourrait être un 'productId'
                    System.out.println(method.getParameters().get(0).getSimpleName());
                    if (method.getParameters().get(0).getSimpleName().equals("productId")) {
                        return "getById";
                    } else {
                        return "getAll";
                    }
                } else if (method.getAnnotations().stream().anyMatch(a -> a.getType().getSimpleName().equals("PostMapping"))) {
                    return "add";
                } else if (method.getAnnotations().stream().anyMatch(a -> a.getType().getSimpleName().equals("PutMapping"))) {
                    return "update";
                } else if (method.getAnnotations().stream().anyMatch(a -> a.getType().getSimpleName().equals("DeleteMapping"))) {
                    return "delete";
                }
                return "unknown";
            }


            private String getProductIdSnippet(CtMethod<?> method) {
                for (CtParameter<?> param : method.getParameters()) {
                    if (param.getSimpleName().equals("productId")) {
                        // Retourner une chaîne de format qui sera remplacée par la valeur de productId lors de l'exécution
                        return ", \\\"Id\\\": \\\"\" + productId + \"\\\"";
                    }
                }
                return "";
            }
        });
    }
}
