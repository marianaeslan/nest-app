package br.com.gabgrupo.nest.data.model

enum class UserRole {
    OPERATOR,
    MANAGER,
    LEADER
}

enum class IdeaStatus(val label: String) {
    PENDING("Em análise"),
    PRIORITIZED("Priorizada"),
    APPROVED("Aprovada"),
    REJECTED("Arquivada")
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
