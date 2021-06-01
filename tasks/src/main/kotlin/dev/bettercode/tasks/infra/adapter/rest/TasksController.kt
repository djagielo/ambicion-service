@file:Suppress("unused")

package dev.bettercode.tasks.infra.adapter.rest

import dev.bettercode.tasks.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*

@RestController
class TasksController(val tasksFacade: TasksFacade) {

    @GetMapping("/tasks/{id}")
    internal fun getById(@PathVariable id: UUID): ResponseEntity<TaskDto> {
        return tasksFacade.get(TaskId(id))?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @PostMapping("/tasks")
    internal fun create(@RequestBody taskRequest: TaskRequest): ResponseEntity<TaskDto> {
        val createdTask = tasksFacade.add(taskRequest.toTaskDto())
        return ResponseEntity.created(URI.create("tasks/${createdTask.id.uuid}"))
            .body(createdTask)
    }

    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    internal fun delete(@PathVariable id: UUID) {
        tasksFacade.delete(TaskId(id))
    }


    @GetMapping("/projects")
    internal fun getAllProjects(): Page<ProjectDto> {
        return tasksFacade.getProjects()
    }

    @GetMapping("/projects/{id}")
    internal fun getProject(@PathVariable id: UUID): ResponseEntity<ProjectDto> {
        return tasksFacade.getProject(ProjectId(id))?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @GetMapping("/projects/{id}/tasks")
    internal fun getTasksForProject(@PathVariable id: UUID): ResponseEntity<Page<TaskDto>> {
        return tasksFacade.getProject(ProjectId(id))?.let {
            ResponseEntity.ok(tasksFacade.getTasksForProject(PageRequest.of(0, 100), it.id))
        } ?: ResponseEntity.notFound().build()
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception) {
        println(ex)
    }
}