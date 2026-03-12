package ci.nsu.moble.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.KeyEventDispatcher
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import ci.nsu.moble.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) { //вызывается при создании Activity. настройка интерфейса
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() //для рисования под системными панелями
        setContent {
            PracticeTheme { //тема приложения
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding -> //базовая тема
                    MainScreenActivity( //вызов Composable, рисует UI главного экрана
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
// TODO:  here is to open the second activity
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondScreen(text: String, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Second Screen") },
                navigationIcon = {
                    Button(onClick = { onBack() }) {
                        Text("Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Received text:")
            Text(text)
        }
    }
}
@Composable
fun MainScreenActivity(modifier: Modifier = Modifier) { //рисует UI
    var text by remember { mutableStateOf("") } //состояние Compose, mutableStateOf изменяет состояние
    val context = LocalContext.current //создать Intent, запустить Activity

    Column( //размещает предметы вертикально
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TODO:  нужно добавить  TextField
        TextField( //поле ввода текста
            value = text,
            onValueChange = {text = it},
            label = {Text("Enter text")}
        )
        Button( //открывает SecondActivity
            onClick = {
                //TODO:  нужно добавить кнопку которая по клику открывает второе активити через интент
                val intent = Intent(context, SecondActivity::class.java) //сообщает системе открыть другой экран
                intent.putExtra("text_data", text) //передача данных
                context.startActivity(intent) //запуск SecondActivity
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Open SecondActivity")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PracticeTheme {
        MainScreenActivity()
    }
}