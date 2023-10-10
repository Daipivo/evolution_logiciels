package utils;

import java.util.List;

public class ApplicationStatistics {
    private int numberOfClasses;
    private int numberOfLinesOfCode;
    private int totalNumberOfMethods;
    private int totalNumberOfPackages;
    private double averageMethodsPerClass;
    private double averageLinesOfCodePerMethod;
    private double averageAttributesPerClass;
    private List<String> classesWithMostMethods;
    private List<String> classesWithMostAttributes;
    private List<String> classesWithBothAttributesAndMethods;
    private List<String> classesWithMoreThanXMethods;
    private List<String> methodsWithMostLinesOfCode;
    private int maxParametersInMethods;

    public int getNumberOfClasses() {
        return numberOfClasses;
    }

    public void setNumberOfClasses(int numberOfClasses) {
        this.numberOfClasses = numberOfClasses;
    }

    public int getNumberOfLinesOfCode() {
        return numberOfLinesOfCode;
    }

    public void setNumberOfLinesOfCode(int numberOfLinesOfCode) {
        this.numberOfLinesOfCode = numberOfLinesOfCode;
    }

    public int getTotalNumberOfMethods() {
        return totalNumberOfMethods;
    }

    public void setTotalNumberOfMethods(int totalNumberOfMethods) {
        this.totalNumberOfMethods = totalNumberOfMethods;
    }

    public int getTotalNumberOfPackages() {
        return totalNumberOfPackages;
    }

    public void setTotalNumberOfPackages(int totalNumberOfPackages) {
        this.totalNumberOfPackages = totalNumberOfPackages;
    }

    public double getAverageMethodsPerClass() {
        return averageMethodsPerClass;
    }

    public void setAverageMethodsPerClass(double averageMethodsPerClass) {
        this.averageMethodsPerClass = averageMethodsPerClass;
    }

    public double getAverageLinesOfCodePerMethod() {
        return averageLinesOfCodePerMethod;
    }

    public void setAverageLinesOfCodePerMethod(double averageLinesOfCodePerMethod) {
        this.averageLinesOfCodePerMethod = averageLinesOfCodePerMethod;
    }

    public double getAverageAttributesPerClass() {
        return averageAttributesPerClass;
    }

    public void setAverageAttributesPerClass(double averageAttributesPerClass) {
        this.averageAttributesPerClass = averageAttributesPerClass;
    }

    public List<String> getClassesWithMostMethods() {
        return classesWithMostMethods;
    }

    public void setClassesWithMostMethods(List<String> classesWithMostMethods) {
        this.classesWithMostMethods = classesWithMostMethods;
    }

    public List<String> getClassesWithMostAttributes() {
        return classesWithMostAttributes;
    }

    public void setClassesWithMostAttributes(List<String> classesWithMostAttributes) {
        this.classesWithMostAttributes = classesWithMostAttributes;
    }

    public List<String> getClassesWithBothAttributesAndMethods() {
        return classesWithBothAttributesAndMethods;
    }

    public void setClassesWithBothAttributesAndMethods(List<String> classesWithBothAttributesAndMethods) {
        this.classesWithBothAttributesAndMethods = classesWithBothAttributesAndMethods;
    }

    public List<String> getClassesWithMoreThanXMethods() {
        return classesWithMoreThanXMethods;
    }

    public void setClassesWithMoreThanXMethods(List<String> classesWithMoreThanXMethods) {
        this.classesWithMoreThanXMethods = classesWithMoreThanXMethods;
    }

    public List<String> getMethodsWithMostLinesOfCode() {
        return methodsWithMostLinesOfCode;
    }

    public void setMethodsWithMostLinesOfCode(List<String> methodsWithMostLinesOfCode) {
        this.methodsWithMostLinesOfCode = methodsWithMostLinesOfCode;
    }

    public int getMaxParametersInMethods() {
        return maxParametersInMethods;
    }

    public void setMaxParametersInMethods(int maxParametersInMethods) {
        this.maxParametersInMethods = maxParametersInMethods;
    }
}
