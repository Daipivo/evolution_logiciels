package Abstract;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class Parsers<T> {
    /* ATTRIBUTES */
    protected String projectPath;
    protected String projectSrcPath;
    protected String projectBinPath;
    protected T parser;

    /* CONSTRUCTOR */
    public Parsers(String projectPath) {
        setProjectPaths(projectPath);
        configure();
    }

    /* METHODS */
    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getProjectSrcPath() {
        return projectSrcPath;
    }

    private void setProjectSrcPath(String projectSrcPath) {
        this.projectSrcPath = projectSrcPath;
    }

    public String getProjectBinPath() {
        return projectBinPath;
    }

    private void setProjectBinPath(String projectBinPath) {
        this.projectBinPath = projectBinPath;
    }

    private void setProjectPaths(String projectPath) {
        setProjectPath(projectPath);
        setProjectSrcPath(projectPath+ File.separator+"src"+File.separator);
        setProjectBinPath(projectPath+File.separator+"bin"+File.separator);
    }

    public T getParser() {
        return parser;
    }

    public List<File> listJavaFiles(String filePath){
        File folder = new File(filePath);
        List<File> javaFiles = new ArrayList<>();
        String fileName = "";

        for (File file: folder.listFiles()) {
            fileName = file.getName();

            if (file.isDirectory())
                javaFiles.addAll(listJavaFiles(file.getAbsolutePath()));
            else if (fileName.endsWith(".java"))
                javaFiles.add(file);
        }

        return javaFiles;
    }

    public List<File> listJavaProjectFiles(){
        return listJavaFiles(getProjectSrcPath());
    }

    public abstract void configure();
}

