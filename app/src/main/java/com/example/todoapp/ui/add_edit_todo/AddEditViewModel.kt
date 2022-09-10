package com.example.todoapp.ui.add_edit_todo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Todo
import com.example.todoapp.data.TodoRepository
import com.example.todoapp.utils.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val repository: TodoRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var todo by mutableStateOf<Todo?>(null)
        private set

    var title by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    init {
        val todoId = savedStateHandle.get<Int>("todoId")!!
        if (todoId != -1) {
            viewModelScope.launch {
               repository.getTodoById(todoId)?.let { todo ->
                   title = todo.title
                   description = todo.description ?: ""
                   this@AddEditViewModel.todo = todo
               }
            }
        }
    }

    fun onEvent(event: AddEditTodoUiEvent) {
        when(event) {
          is AddEditTodoUiEvent.OnTitleChange -> {
              title = event.title
          }

          is AddEditTodoUiEvent.OnDescriptionChange -> {
              description = event.description
          }

          is AddEditTodoUiEvent.OnSaveTodoClick -> {
               viewModelScope.launch {
                   if (title.isBlank()) {
                       sendUiEvent(UIEvent.ShowSnackbar(
                           message = "The title can't be empty"
                       ))
                       return@launch
                   }
                   repository.insertTodo(
                       Todo(
                           title = title,
                           description = description,
                           isDone = todo?.isDone ?: false,
                           id = todo?.id
                       )
                   )
                   sendUiEvent(UIEvent.PopBackStack)
               }
          }
        }
    }

    private fun sendUiEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}