package ci.nsu.moble.main

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

data class ColorUiState( //изначальные цвета (по умолчанию)
    val red: Int = 128,
    val green: Int = 128,
    val blue: Int = 128
){
    val color: Color get() = Color(red / 255f, green / 255f, blue / 255f) //преобразовывает в другой формат для работы с цветами
    val hexCode: String get() = "#${red.toString(16).padStart(2, '0')}${green.toString(16).padStart(2, '0')}${blue.toString(16).padStart(2, '0')}".uppercase()
    //изменяет из RGB в HEX
}

class ColorPickerViewModel : ViewModel() {
    //поток данных, который можно наблюдать
    private val _uiState = MutableStateFlow(ColorUiState())
    //MutableStateFlow - изменяемый поток (внутри ViewModel)
    val uiState: StateFlow<ColorUiState> = _uiState.asStateFlow()
    //StateFlow - неизменяемый поток (для UI)

    //методы изменения состояния
    //update - обновляет состояние
    //it.copy - создает копию с измененным компонентом
    fun onRedChanged(newValue: Float)
    {
        _uiState.update { it.copy(red = newValue.toInt()) }
    }
    fun onGreenChanged(newValue: Float)
    {
        _uiState.update { it.copy(green = newValue.toInt()) }
    }
    fun onBlueChanged(newValue: Float)
    {
        _uiState.update { it.copy(blue = newValue.toInt()) }
    }
    //генерирует случайные значения
    fun generateRandomColor(){
        _uiState.update {
            ColorUiState(
                red = Random.nextInt(0, 256),
                green = Random.nextInt(0, 256),
                blue = Random.nextInt(0, 256)
            )
        }
    }
}