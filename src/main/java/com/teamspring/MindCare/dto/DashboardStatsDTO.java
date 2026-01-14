package com.teamspring.MindCare.dto;

public class DashboardStatsDTO {
    private long totalSessionsWeek;
    private long activeStudents;
    private long pendingRequests;
    private long resourcesPublished;

    public DashboardStatsDTO(long totalSessionsWeek, long activeStudents, long pendingRequests, long resourcesPublished) {
        this.totalSessionsWeek = totalSessionsWeek;
        this.activeStudents = activeStudents;
        this.pendingRequests = pendingRequests;
        this.resourcesPublished = resourcesPublished;
    }

    // Getters
    public long getTotalSessionsWeek() { return totalSessionsWeek; }
    public long getActiveStudents() { return activeStudents; }
    public long getPendingRequests() { return pendingRequests; }
    public long getResourcesPublished() { return resourcesPublished; }
}