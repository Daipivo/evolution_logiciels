package org.example.Spoon;

import org.example.Parser.Parser;
import spoon.Launcher;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtMethod;

public class SpoonParser extends Parser<Launcher> {
    public SpoonParser(String projectPath) {
        super(projectPath);
    }

    public void setLauncher(String sourceOutputPath, String binaryOutputPath,
                            boolean autoImports, boolean commentsEnabled) {
        parser = new Launcher(); // create launcher
        parser.addInputResource(getProjectSrcPath()); // set project source path
        //parser.getEnvironment().setSourceClasspath(new String[] {getProjectBinPath()}); // set project classpath
        parser.setSourceOutputDirectory(sourceOutputPath); // set generated source code directory path
        parser.setBinaryOutputDirectory(binaryOutputPath); // set generated binary code directory path
        parser.getEnvironment().setAutoImports(autoImports); // set auto imports
        parser.getEnvironment().setCommentEnabled(commentsEnabled); // set comments enabled
    }
    public void configure() {
        setLauncher(projectPath+"/spooned/src/", projectPath+"/spooned/bin/", true, true);
    }

    public void addProcessor(AbstractProcessor<CtMethod<?>> processor) {
        parser.addProcessor(processor);
    }

    public void run() {
        parser.run();
    }
}
