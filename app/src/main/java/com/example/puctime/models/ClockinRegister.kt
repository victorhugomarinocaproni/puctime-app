package com.example.puctime.models

data class ClockinRegister(
    val nome: String = "",
    val diaDaSemana: String = "",
    val entrada: String = "",
    val saida: String = "",
    val idTaskReferencia: String = ""
) {

    fun toMap(): Map<String, Any> {
        val map = HashMap<String, Any>()
        map["nome"] = nome
        map["diaDaSemana"] = diaDaSemana
        map["entrada"] = entrada
        map["saida"] = saida
        map["idTaskReferencia"] = idTaskReferencia
        return map
    }
}
