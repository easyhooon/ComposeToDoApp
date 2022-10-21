package com.kenshi.composetodoapp.data.models

import androidx.compose.ui.graphics.Color
import com.kenshi.composetodoapp.ui.theme.HighPriorityColor
import com.kenshi.composetodoapp.ui.theme.LowPriorityColor
import com.kenshi.composetodoapp.ui.theme.MediumPriorityColor
import com.kenshi.composetodoapp.ui.theme.NonePriority

enum class Priority(val color: Color) {
    HIGH(HighPriorityColor),
    MEDIUM(MediumPriorityColor),
    LOW(LowPriorityColor),
    NONE(NonePriority)
}