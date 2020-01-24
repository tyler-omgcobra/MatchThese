package org.omgcobra.matchthese.dao

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.math.RoundingMode

class AppTypeConverters {
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?) = value?.toDouble()

    @TypeConverter
    fun doubleToBigDecimal(double: Double?) = double?.let { BigDecimal(it).setScale(5, RoundingMode.HALF_UP) }
}