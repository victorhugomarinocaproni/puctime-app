package com.example.puctime.ui.models

data class Clockin (
    val horarioInicio: String = "",
    val horarioTermino: String = "",
    val nome: String = "",
    val diaDaSemana: String = "",
    val dataCriacaoAponamento: String = "",
) {

    fun toMap(): Map<String, Any> {
        val map = HashMap<String, Any>()
        map["diaDaSemana"] = diaDaSemana
        map["nome"] = nome
        map["horarioInicio"] = horarioInicio
        map["horarioTermino"] = horarioTermino
        map["dataCriacaoAponamento"] = dataCriacaoAponamento
        return map
    }
}
