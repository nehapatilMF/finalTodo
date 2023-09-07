package com.example.todoapp

enum class TaskStatus(val value: Int) {
    TODO(0),
    IN_PROGRESS(1),
    DONE(2)
}


/**
 *     val currentStatus = TaskStatus.IN_PROGRESS
 *
 *     // To get the numeric value associated with a status
 *     val numericValue = currentStatus.value
 */