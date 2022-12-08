package com.akashdev.kotlinx

import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

val ChipGroup.checkedChipText: String?
    get() = findViewById<Chip>(checkedChipId)?.text?.toString()

val ChipGroup.checkedChipTag: String?
    get() = findViewById<Chip>(checkedChipId)?.tag?.toString()

val ChipGroup.checkedChip: Chip?
    get() = findViewById<Chip>(checkedChipId)
