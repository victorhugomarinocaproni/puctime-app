package com.example.puctime.ui.models

data class Clockin (
    val horarioInicio: String = "",
    val horarioTermino: String = "",
    val nome: String = "",
    val diaDaSemana: String = "",
    val dataCriacaoAponamento: String = "",
    val id: String = ""
) {

    private var hash = ""

    init{
        gerarClockinId()
    }

    private fun gerarClockinId(){
        val data = toMap()
        hash = data.hashCode().toString()
    }

    fun toMap(): Map<String, Any> {
        val map = HashMap<String, Any>()
        map["diaDaSemana"] = diaDaSemana
        map["nome"] = nome
        map["horarioInicio"] = horarioInicio
        map["horarioTermino"] = horarioTermino
        map["dataCriacaoAponamento"] = dataCriacaoAponamento
        map["id"] = hash
        return map
    }
}
