package com.application.alphacapital.superapp.finplan.model

data class RiskProfileQuestionsModel(
    var message: String = "",
    var risk_profiler_questions: MutableList<RiskProfilerQuestion> = mutableListOf(),
    var success: Int = 0
) {
    data class RiskProfilerQuestion(
        var answers: MutableList<Answer> = mutableListOf(),
        var question: String = "",
        var question_id: String = "",
        var is_selected: String = "",
    ) {
        data class Answer(
            var answer: String = "",
            var answer_id: String= "",
            var is_selected: Boolean = false,
        )
    }
}