package ci.nsu.moble.main

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

data class ColorUiState(
    val red: Int = 128,
    val green: Int = 128,
    val blue: Int = 128
){
    val color: Color get() = Color(red / 255f, green / 255f, blue / 255f)
    val hexCode: String get() = "#${red.toString(16).padStart(2, '0')}${green.toString(16).padStart(2, '0')}${blue.toString(16).padStart(2, '0')}".uppercase()
}

class ColorPickerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ColorUiState())
    val uiState: StateFlow<ColorUiState> = _uiState.asStateFlow()

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