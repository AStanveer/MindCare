package com.teamspring.MindCare.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessment_results")
public class AssessmentResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private Long userId = 1L; // Default dummy user
    
    @Column(name = "assessment_name")
    private String assessmentName = "DASS-21"; // Hardcoded
    
    @Column(name = "depression_score")
    private int depressionScore;
    
    @Column(name = "anxiety_score")
    private int anxietyScore;
    
    @Column(name = "stress_score")
    private int stressScore;
    
    @Column(name = "depression_level")
    private String depressionLevel;
    
    @Column(name = "anxiety_level")
    private String anxietyLevel;
    
    @Column(name = "stress_level")
    private String stressLevel;
    
    @Column(name = "answers", columnDefinition = "TEXT")
    private String answers;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    // Constructors
    public AssessmentResult() {
        this.completedAt = LocalDateTime.now();
    }
    
    public AssessmentResult(int[] answersArray) {
        this.completedAt = LocalDateTime.now();
        setAnswersFromArray(answersArray);
        calculateScores();
    }
    
    // Helper methods (same as before for answers processing and scoring)
    public void setAnswersFromArray(int[] answersArray) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < answersArray.length; i++) {
            sb.append(answersArray[i]);
            if (i < answersArray.length - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        this.answers = sb.toString();
    }
    
    public int[] getAnswersAsArray() {
        if (answers == null || answers.isEmpty()) {
            return new int[0];
        }
        String clean = answers.replace("[", "").replace("]", "");
        String[] parts = clean.split(",");
        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = Integer.parseInt(parts[i].trim());
        }
        return result;
    }
    
    private void calculateScores() {
        int[] answers = getAnswersAsArray();
        if (answers.length != 21) {
            throw new IllegalArgumentException("DASS-21 requires 21 answers");
        }
        
        // Calculate raw scores
        int rawDepression = calculateDepressionScore(answers);
        int rawAnxiety = calculateAnxietyScore(answers);
        int rawStress = calculateStressScore(answers);
        
        // Convert to DASS-42 scale (multiply by 2)
        this.depressionScore = rawDepression * 2;
        this.anxietyScore = rawAnxiety * 2;
        this.stressScore = rawStress * 2;
        
        // Determine levels
        this.depressionLevel = interpretDepression(this.depressionScore);
        this.anxietyLevel = interpretAnxiety(this.anxietyScore);
        this.stressLevel = interpretStress(this.stressScore);
    }
    
    private int calculateDepressionScore(int[] answers) {
        // Depression items: 3, 5, 10, 13, 16, 17, 21 (0-indexed: 2, 4, 9, 12, 15, 16, 20)
        int[] indices = {2, 4, 9, 12, 15, 16, 20};
        return sumIndices(answers, indices);
    }
    
    private int calculateAnxietyScore(int[] answers) {
        // Anxiety items: 2, 4, 7, 9, 15, 19, 20 (0-indexed: 1, 3, 6, 8, 14, 18, 19)
        int[] indices = {1, 3, 6, 8, 14, 18, 19};
        return sumIndices(answers, indices);
    }
    
    private int calculateStressScore(int[] answers) {
        // Stress items: 1, 6, 8, 11, 12, 14, 18 (0-indexed: 0, 5, 7, 10, 11, 13, 17)
        int[] indices = {0, 5, 7, 10, 11, 13, 17};
        return sumIndices(answers, indices);
    }
    
    private int sumIndices(int[] answers, int[] indices) {
        int sum = 0;
        for (int idx : indices) {
            sum += answers[idx];
        }
        return sum;
    }
    
    private String interpretDepression(int score) {
        if (score <= 9) return "Normal";
        if (score <= 13) return "Mild";
        if (score <= 20) return "Moderate";
        if (score <= 27) return "Severe";
        return "Extremely Severe";
    }
    
    private String interpretAnxiety(int score) {
        if (score <= 7) return "Normal";
        if (score <= 9) return "Mild";
        if (score <= 14) return "Moderate";
        if (score <= 19) return "Severe";
        return "Extremely Severe";
    }
    
    private String interpretStress(int score) {
        if (score <= 14) return "Normal";
        if (score <= 18) return "Mild";
        if (score <= 25) return "Moderate";
        if (score <= 33) return "Severe";
        return "Extremely Severe";
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getAssessmentName() { return assessmentName; }
    public void setAssessmentName(String assessmentName) { this.assessmentName = assessmentName; }
    
    public int getDepressionScore() { return depressionScore; }
    public int getAnxietyScore() { return anxietyScore; }
    public int getStressScore() { return stressScore; }
    
    public String getDepressionLevel() { return depressionLevel; }
    public String getAnxietyLevel() { return anxietyLevel; }
    public String getStressLevel() { return stressLevel; }
    
    public String getAnswers() { return answers; }
    public void setAnswers(String answers) { 
        this.answers = answers;
        calculateScores();
    }
    
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    
    // Format date for display
    public String getFormattedDate() {
        return completedAt.toLocalDate().toString();
    }
    
    public String getFormattedTime() {
        return completedAt.toLocalTime().toString().substring(0, 5); // HH:mm
    }
}