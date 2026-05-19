package com.example.calculation.data.local

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromDoubleList(value: String?): List<Double>? {
        return value?.split(",")?.mapNotNull { it.toDoubleOrNull() }
    }

    @TypeConverter
    fun toDoubleList(list: List<Double>?): String? {
        return list?.joinToString(",")
    }
}