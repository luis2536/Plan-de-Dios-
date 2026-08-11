// app/src/main/java/com/example/data/model/UserRoleAndLanguage.kt
package com.example.data.model

enum class UserRole(val displayNameEs: String, val displayNameEn: String, val defaultIncome: Double) {
    HOMBRE("Él (Hombre)", "Him (Man)", 260.0),
    MUJER("Ella (Mujer)", "Her (Woman)", 0.0),
    PAREJA("Conjunto (Pareja)", "Joint (Couple)", 260.0)
}

enum class AppLanguage(val code: String, val label: String) {
    SPANISH("es", "Español (LA)"),
    ENGLISH("en", "English")
}
