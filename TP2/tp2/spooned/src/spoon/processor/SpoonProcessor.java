package spoon.processor;
import Abstract.AbstractProcessor;
import spoon.parser.SpoonParser;
public class SpoonProcessor extends AbstractProcessor<SpoonParser> {
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