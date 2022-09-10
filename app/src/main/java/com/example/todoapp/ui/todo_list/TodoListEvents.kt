package com.example.todoapp.ui.todo_list

import com.example.todoapp.data.Todo

sealed class TodoListEvents {
    data class OnDeleteTodoClick(val todo: Todo) : TodoListEvents()
    data class OnDoneChange(val todo: Todo, val isDone: Boolean): TodoListEvents()
    object OnUndoDeleteClick: TodoListEvents()
    data class OnTodoClick(val todo: Todo): TodoListEvents()
    object OnAddTodoClick: TodoListEvents()

}
