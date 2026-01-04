package com.teamspring.MindCare.dto;


public class FeatureUsageDto {
    private int totalUsers ;
    private int featureAccessCount;
    private Integer totalSelfCareUsage ;
    
    private Integer totalAssessmentUsage ;
    private Integer totalPeerSupportUsage ;
    private Integer totalResourcesUsage ;
    public FeatureUsageDto(int totalUsers, int featureAccessCount, Integer totalSelfCareUsage, Integer totalAssessmentUsage,
            Integer totalPeerSupportUsage, Integer totalResourcesUsage) {
        this.totalUsers = totalUsers;
        this.featureAccessCount = featureAccessCount;
        this.totalSelfCareUsage = totalSelfCareUsage;
        this.totalAssessmentUsage = totalAssessmentUsage;
        this.totalPeerSupportUsage = totalPeerSupportUsage;
        this.totalResourcesUsage = totalResourcesUsage;
    }
    public int getTotalUsers() {
        return totalUsers;
    }
    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }
    public int getFeatureAccessCount() {
        return featureAccessCount;
    }
    public void setFeatureAccessCount(int featureAccessCount) {
        this.featureAccessCount = featureAccessCount;
    }
    public Integer getTotalSelfCareUsage() {
        return totalSelfCareUsage;
    }
    public void setTotalSelfCareUsage(Integer totalSelfCareUsage) {
        this.totalSelfCareUsage = totalSelfCareUsage;
    }
    public Integer getTotalAssessmentUsage() {
        return totalAssessmentUsage;
    }
    public void setTotalAssessmentUsage(Integer totalAssessmentUsage) {
        this.totalAssessmentUsage = totalAssessmentUsage;
    }
    public Integer getTotalPeerSupportUsage() {
        return totalPeerSupportUsage;
    }
    public void setTotalPeerSupportUsage(Integer totalPeerSupportUsage) {
        this.totalPeerSupportUsage = totalPeerSupportUsage;
    }
    public Integer getTotalResourcesUsage() {
        return totalResourcesUsage;
    }
    public void setTotalResourcesUsage(Integer totalResourcesUsage) {
        this.totalResourcesUsage = totalResourcesUsage;
    }
    
}
