package Abstract;
public abstract class AbstractProcessor<T> {
    /* ATTRIBUTES */
    protected T parser;

    /* CONSTRUCTOR */
    public AbstractProcessor(String projectPath) {
        setParser(projectPath);
    }

    /* METHODS */
    public T getParser() {
        return parser;
    }

    public abstract void setParser(String projectPath);
}