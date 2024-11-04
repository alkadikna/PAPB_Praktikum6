package com.example.todoapp_room

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.todoapp_room.data.OfflineItemsRepository
import com.example.todoapp_room.data.Task
import com.example.todoapp_room.data.TaskDao
import com.example.todoapp_room.data.TaskDatabase
import com.example.todoapp_room.data.TasksRepository
import com.example.todoapp_room.ui.theme.TodoAPP_roomTheme

class MainActivity : ComponentActivity() {
    private val database by lazy { TaskDatabase.getDatabase(applicationContext) }
    private val itemDao by lazy { database.itemDao() }
    private val repository by lazy { OfflineItemsRepository(itemDao) }
    private val viewModel: TaskViewModel by viewModels { TaskViewModelFactory(repository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoAPP_roomTheme {
               TodoApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun TodoApp(viewModel: TaskViewModel, modifier: Modifier = Modifier) {
    var taskTitle by remember { mutableStateOf("") }
    val tasks by viewModel.getAllTasks.collectAsState(initial = emptyList())
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF3B3030),
                    titleContentColor = Color(0xFFFFF0D1)
                ),
                title = {
                    Text(
                        text = "TODO APP",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = taskTitle,
                onValueChange = { taskTitle = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.LightGray),
                maxLines = 1,
                label = { Text("Enter task") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    if (taskTitle.isNotEmpty()) {
                        viewModel.addTask(Task(title = taskTitle))
                        taskTitle = ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3B3030),
                )
            ) {
                Text("Save", color = Color(0xFFFFF0D1), fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(tasks) { task ->
                    TaskRow(
                        task = task,
                        onCheckedChange = { checked ->
                            viewModel.updateTask(task.copy(isChecked = checked))
                        },
                        onDeleteTask = {
                            viewModel.deleteTask(task)
                            Toast.makeText(context, "${task.title} has been deleted!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun TaskRow(
    task: Task,
    onCheckedChange: (Boolean) -> Unit,
    onDeleteTask: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkmarkColor = Color(0xFFFFF0D1),
                checkedColor = Color(0xFF3B3030)
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = task.title,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete Task",
            modifier = Modifier
                .clickable { onDeleteTask() }
                .padding(4.dp)
        )
    }
}


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    TodoAPP_roomTheme {
//        TodoApp()
//    }
//}