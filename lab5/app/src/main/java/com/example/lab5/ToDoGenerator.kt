package com.example.lab5

object ToDoGenerator {
    fun generate(count: Int) : List<ToDo> =
        (0..count).map { index ->
            ToDo(
                name = "Дело $index",
                isDone = listOf(true, false).random()
            )
    }
}