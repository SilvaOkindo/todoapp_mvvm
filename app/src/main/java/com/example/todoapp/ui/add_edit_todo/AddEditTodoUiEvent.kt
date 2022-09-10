package com.example.todoapp.ui.add_edit_todo

sealed class AddEditTodoUiEvent {
    data class OnTitleChange(val title: String) : AddEditTodoUiEvent()
    data class OnDescriptionChange(val description: String) : AddEditTodoUiEvent()
    object OnSaveTodoClick : AddEditTodoUiEvent()
}
