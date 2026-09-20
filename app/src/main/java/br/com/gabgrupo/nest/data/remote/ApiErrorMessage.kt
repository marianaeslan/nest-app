package br.com.gabgrupo.nest.data.remote

object ApiErrorMessage {
    fun forStatus(code: Int, resource: String): String = when (code) {
        400 -> "Dados inválidos para $resource."
        401 -> "Sua sessão expirou. Faça login novamente."
        403 -> "Você não tem permissão para esta ação."
        404 -> "$resource não encontrado."
        409 -> "Não foi possível concluir $resource: conflito de dados."
        500 -> "O servidor encontrou um erro ao processar $resource."
        else -> "Erro ao processar $resource. Código: $code"
    }
}
