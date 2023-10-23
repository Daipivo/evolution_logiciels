package spoon.processor;

import Abstract.Processor;
import spoon.parser.SpoonParser;

public class SpoonProcessor extends Processor<SpoonParser> {
    public SpoonProcessor(String projectPath) {
        super(projectPath);
    }

    public void setParser(String projectPath) {
        parser = new SpoonParser(projectPath);
    }

    public void setParser(SpoonParser parser) {
        this.parser = parser;
    }

}

