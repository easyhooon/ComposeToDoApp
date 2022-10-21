package com.kenshi.composetodoapp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenshi.composetodoapp.data.models.Priority
import com.kenshi.composetodoapp.data.models.ToDoTask
import com.kenshi.composetodoapp.data.repositories.DataStoreRepository
import com.kenshi.composetodoapp.data.repositories.ToDoRepository
import com.kenshi.composetodoapp.util.Action
import com.kenshi.composetodoapp.util.Constants.MAX_TITLE_LENGTH
import com.kenshi.composetodoapp.util.RequestState
import com.kenshi.composetodoapp.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: ToDoRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    // ctrl + b 로도 어디서 사용 하고 있는지 확인할 수 있다.
    // by 키워드로 선언한 변수엔 value 가 아닌 직접 접근이 가능 하다 id.value (x) -> id
    // 대입도 마찬가지
    var action by mutableStateOf(Action.NO_ACTION)
        //setter is private can't change value some class not sharedViewModel
        private set

    var id by mutableStateOf(0)
        private set

    var title by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    // 위에 변수들 원래 전부 이런 형태 였음
    // var priority:MutableState<Priority> = mutableStateOf(Priority.LOW)
    var priority by mutableStateOf(Priority.LOW)
        private set

    //    val searchAppBarState: MutableState<SearchAppBarState> =
//        mutableStateOf(SearchAppBarState.CLOSED)
//
//    val searchTextState: MutableState<String> = mutableStateOf("")
    var searchAppBarState by mutableStateOf(SearchAppBarState.CLOSED)
        private set

    var searchTextState by mutableStateOf("")
        private set

    private val _allTasks =
        MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<ToDoTask>>> = _allTasks

    private val _searchedTasks =
        MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val searchedTasks: StateFlow<RequestState<List<ToDoTask>>> = _searchedTasks

    private val _sortState =
        MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
    val sortState: StateFlow<RequestState<Priority>> = _sortState

    private val _selectedTask: MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<ToDoTask?> = _selectedTask

    // init block 위에다 변수를 선언 해야지 init block 내부 함수가 호출 되기 전에 초기화가 이루어진다.
    init {
        getAllTasks()
        readSortState()
    }

    fun searchDatabase(searchQuery: String) {
        _searchedTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                // %or% -> Finds any values that have "or" in any position
                repository.searchDatabase(searchQuery = "%$searchQuery%")
                    .collect { searchedTasks ->
                        _searchedTasks.value = RequestState.Success(searchedTasks)
                    }
            }
        } catch (e: Exception) {
            _searchedTasks.value = RequestState.Error(e)
        }
        searchAppBarState = SearchAppBarState.TRIGGERED
    }

    val lowPriorityTasks: StateFlow<List<ToDoTask>> =
        repository.sortByLowPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    val highPriorityTasks: StateFlow<List<ToDoTask>> =
        repository.sortByHighPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    private fun readSortState() {
        _sortState.value = RequestState.Loading
        try {
            viewModelScope.launch {
                dataStoreRepository.readSortState
                    // convert String to Priority
                    .map { Priority.valueOf(it) }
                    .collect {
                        _sortState.value = RequestState.Success(it)
                    }
            }
        } catch (e: Exception) {
            _sortState.value = RequestState.Error(e)
        }
    }

    fun persistSortingState(priority: Priority) {
        viewModelScope.launch {
            dataStoreRepository.persistSortState(priority = priority)
        }
    }

    private fun getAllTasks() {
        _allTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.getAllTasks.collect {
                    _allTasks.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _allTasks.value = RequestState.Error(e)
        }
    }


    fun getSelectedTask(taskId: Int) = viewModelScope.launch {
        repository.getSelectedTask(taskId = taskId).collect { task ->
            _selectedTask.value = task
        }
    }

    private fun addTask() {
        viewModelScope.launch() {
            val toDoTask = ToDoTask(
                title = title,
                description = description,
                priority = priority
            )
            repository.addTask(toDoTask = toDoTask)
        }
        searchAppBarState = SearchAppBarState.CLOSED
    }

    private fun updateTask() = viewModelScope.launch {
        val toDoTask = ToDoTask(
            id = id,
            title = title,
            description = description,
            priority = priority
        )
        repository.updateTask(toDoTask = toDoTask)
    }

    private fun deleteTask() = viewModelScope.launch {
        val toDoTask = ToDoTask(
            id = id,
            title = title,
            description = description,
            priority = priority
        )
        repository.deleteTask(toDoTask = toDoTask)
    }

    private fun deleteAllTask() = viewModelScope.launch {
        repository.deleteAllTasks()
    }

    fun handleDatabaseActions(action: Action) {
        when (action) {
            Action.ADD -> {
                addTask()
            }

            Action.UPDATE -> updateTask()

            Action.DELETE -> deleteTask()

            Action.DELETE_ALL -> deleteAllTask()

            Action.UNDO -> addTask()

            else -> {}
        }
    }

    fun updateTaskFields(selectedTask: ToDoTask?) {
        if (selectedTask != null) {
            id = selectedTask.id
            title = selectedTask.title
            description = selectedTask.description
            priority = selectedTask.priority
        } else {
            //default value
            id = 0
            title = ""
            description = ""
            priority = Priority.LOW
        }
    }

    fun updateTitle(newTitle: String) {
        if (newTitle.length < MAX_TITLE_LENGTH) {
            title = newTitle
        }
    }

    fun updateDescription(newDescription: String) {
        description = newDescription
    }

    fun updatePriority(newPriority: Priority) {
        priority = newPriority
    }

    // to update acton value only in sharedViewModel
    fun updateAction(newAction: Action) {
        action = newAction
    }

    fun updateSearchAppBarState(newState: SearchAppBarState) {
        searchAppBarState = newState
    }

    fun updateSearchText(newText: String) {
        searchTextState = newText
    }

    fun validateFields(): Boolean {
        return title.isNotEmpty() && description.isNotEmpty()
    }
}