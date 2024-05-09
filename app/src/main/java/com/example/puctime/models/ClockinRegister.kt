package com.example.puctime.models

data class ClockinRegister(
    val nome: String = "",
    val diaDaSemana: String = "",
    val timestamp: String = "",
    val id: String = ""
) {

    private var _id = ""

    init{
        gerarIdRegistroClockin()
    }

    private fun gerarIdRegistroClockin(){
        val data = toMap()
        _id = data.hashCode().toString()
    }

    fun toMap(): Map<String, Any> {
        val map = HashMap<String, Any>()
        map["nome"] = nome
        map["diaDaSemana"] = diaDaSemana
        map["timestamp"] = timestamp
        map["id"] = _id
        return map
    }
}
