package com.example.puctime.models

data class Clockin (
    val horarioInicio: String = "",
    val horarioTermino: String = "",
    val nome: String = "",
    val diaDaSemana: String = "",
    val dataCriacaoAponamento: String = "",
    val id: String = "",
    val status: String = ""
) {

    private var _id = ""

    init{
        gerarClockinId()
    }

    private fun gerarClockinId(){
        val data = toMap()
        _id = data.hashCode().toString()
    }

    fun toMap(): Map<String, Any> {
        val map = HashMap<String, Any>()
        map["diaDaSemana"] = diaDaSemana
        map["nome"] = nome
        map["horarioInicio"] = horarioInicio
        map["horarioTermino"] = horarioTermino
        map["dataCriacaoAponamento"] = dataCriacaoAponamento
        map["id"] = _id
        map["status"] = status
        return map
    }
}
