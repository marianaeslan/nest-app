package br.com.gabgrupo.nest.data.model

enum class UserRole {
    OPERATOR,
    MANAGER,
    LEADER
}

enum class IdeaStatus {
    PENDING,
    PRIORITIZED,
    APPROVED,
    REJECTED
}

enum class ProjectStatus {
    PLANNING,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}

enum class ProjectStage {
    IDEATION,
    VALIDATION,
    PLANNING,
    EXECUTION,
    MONITORING,
    COMPLETED
}

