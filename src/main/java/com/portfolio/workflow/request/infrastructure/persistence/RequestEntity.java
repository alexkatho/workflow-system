package com.portfolio.workflow.request.infrastructure.persistence;

import java.time.LocalDateTime;
import java.util.UUID;

import com.portfolio.workflow.request.domain.model.RequestStatus;
import com.portfolio.workflow.request.domain.model.RequestType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA-Entity für fachliche Requests.
 */
@Entity
@Table(name = "requests", schema = "workflow")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private RequestType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RequestStatus status;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @Column(name = "decided_by")
    private UUID decidedBy;

    @Column(name = "decision_comment", length = 500)
    private String decisionComment;

    @Column(name = "decided_at")
    private LocalDateTime decidedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (status == null) {
            status = RequestStatus.PENDING;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}