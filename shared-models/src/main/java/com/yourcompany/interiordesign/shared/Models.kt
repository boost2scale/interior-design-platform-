package com.yourcompany.interiordesign.shared

data class SharedTenant(
    val id: String = "",
    val name: String = "Interior Design Studio",
    val brandColorHex: String = "#C59B27",
    val active: Boolean = true
)

data class SharedFeatureFlag(
    val key: String = "",
    val name: String = "",
    val defaultEnabled: Boolean = true
)
