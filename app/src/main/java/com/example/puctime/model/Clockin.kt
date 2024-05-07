package com.example.puctime.model

data class Clockin (
    val horarioInicio: String = "",
    val horarioTermino: String = "",
    val nome: String = "",
    val diaDaSemana: String = "",
) {

    fun toMap(): Map<String, Any> {
        val map = HashMap<String, Any>()
        map["horarioInicio"] = horarioInicio
        map["horarioTermino"] = horarioTermino
        map["nome"] = nome
        map["diaDaSemana"] = diaDaSemana
        return map
    }
}
