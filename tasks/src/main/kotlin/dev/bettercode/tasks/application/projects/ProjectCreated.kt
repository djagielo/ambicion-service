package dev.bettercode.tasks.application.projects

import dev.bettercode.tasks.ProjectId
import dev.bettercode.tasks.shared.DomainEvent

data class ProjectCreated(val projectId: ProjectId): DomainEvent() {
}
