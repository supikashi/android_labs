package com.example.lab5

import kotlin.math.absoluteValue
import kotlin.random.Random

object ToDoGenerator {
    fun generate(count: Int) : List<ToDo> =
        (0..count).map { index ->
            ToDo(
                name = "Дело ${Random.nextInt().absoluteValue % 100}",
                isDone = listOf(true, false).random()
            )
    }
}