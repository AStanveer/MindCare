package com.teamspring.MindCare.model;

public class AssessmentResult {

    private final int depressionScore;
    private final int anxietyScore;
    private final int stressScore;

    private final String depressionLevel;
    private final String anxietyLevel;
    private final String stressLevel;

    public AssessmentResult(int dep, int anx, int str) {
        this.depressionScore = dep;
        this.anxietyScore = anx;
        this.stressScore = str;

        this.depressionLevel = interpretDep(dep);
        this.anxietyLevel = interpretAnx(anx);
        this.stressLevel = interpretStr(str);
    }

    public int getDepressionScore() { return depressionScore; }
    public int getAnxietyScore() { return anxietyScore; }
    public int getStressScore() { return stressScore; }

    public String getDepressionLevel() { return depressionLevel; }
    public String getAnxietyLevel() { return anxietyLevel; }
    public String getStressLevel() { return stressLevel; }

    private String interpretDep(int s) {
        if (s <= 9) return "Normal";
        if (s <= 13) return "Mild";
        if (s <= 20) return "Moderate";
        if (s <= 27) return "Severe";
        return "Extremely Severe";
    }

    private String interpretAnx(int s) {
        if (s <= 7) return "Normal";
        if (s <= 9) return "Mild";
        if (s <= 14) return "Moderate";
        if (s <= 19) return "Severe";
        return "Extremely Severe";
    }

    private String interpretStr(int s) {
        if (s <= 14) return "Normal";
        if (s <= 18) return "Mild";
        if (s <= 25) return "Moderate";
        if (s <= 33) return "Severe";
        return "Extremely Severe";
    }
}