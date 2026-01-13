package com.teamspring.MindCare.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "feature_usage", uniqueConstraints = {
    @UniqueConstraint(columnNames = "user_id")
})
public class FeatureUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "`self-care`", insertable = true, updatable = true, nullable = false)
    private Integer selfCare = 0;
    
    @Column(name = "assessments", insertable = true, updatable = true, nullable = false)
    private Integer assessments = 0;
    
    @Column(name = "`peer-support`", insertable = true, updatable = true, nullable = false)
    private Integer peerSupport = 0;

    @Column(name = "resources", insertable = true, updatable = true, nullable = false)
    private Integer resources = 0;
    
    @Column(name = "counselling", insertable = true, updatable = true, nullable = false)
    private Integer counselling = 0;
    



    public FeatureUsage() {
        initializeDefaults();
    }
    
    @PrePersist
    private void initializeDefaults() {
        if (this.selfCare == null) this.selfCare = 0;
        if (this.assessments == null) this.assessments = 0;
        if (this.peerSupport == null) this.peerSupport = 0;
        if (this.resources == null) this.resources = 0;
        if (this.counselling == null) this.counselling = 0;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getSelfCare() {
        return selfCare != null ? selfCare : 0;
    }

    public void setSelfCare(Integer selfCare) {
        this.selfCare = selfCare != null ? selfCare : 0;
    }

    public Integer getAssessments() {
        return assessments != null ? assessments : 0;
    }

    public void setAssessments(Integer assessments) {
        this.assessments = assessments != null ? assessments : 0;
    }

    public Integer getPeerSupport() {
        return peerSupport != null ? peerSupport : 0;
    }

    public void setPeerSupport(Integer peerSupport) {
        this.peerSupport = peerSupport != null ? peerSupport : 0;
    }

    public Integer getResources() {
        return resources != null ? resources : 0;
    }

    public void setResources(Integer resources) {
        this.resources = resources != null ? resources : 0;
    }

    public Integer getCounselling() {
        return counselling != null ? counselling : 0;
    }

    public void setCounselling(Integer counselling) {
        this.counselling = counselling != null ? counselling : 0;
    }
}

