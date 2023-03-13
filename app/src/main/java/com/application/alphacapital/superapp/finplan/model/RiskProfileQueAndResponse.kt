package com.alphafinancialplanning.model

data class RiskProfileQueAndResponse(
    val message: String,
    val risk_profiler_questions: MutableList<RiskProfilerQuestion>,
    val success: Int
) {
    data class RiskProfilerQuestion(
        var answers: MutableList<Answer>,
        val question: String,
        val question_id: String
    ) {
        data class Answer(
            val answer: String = "",
            val answer_id: String = "",
            var selected:Boolean = false
        )
    }
}