package com.example.geoquiz

import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    var currentIndex = 0
    var answers = IntArray(questionBank.size)
    var checked = IntArray(questionBank.size)
    var isCheater = BooleanArray(questionBank.size)

    fun default(){
        currentIndex = 0
        answers = IntArray(questionBank.size)
        checked = IntArray(questionBank.size)
        isCheater = BooleanArray(questionBank.size)
    }

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        if (currentIndex < questionBank.size - 1)
            currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveBack(){
        if (currentIndex > 0)
            currentIndex = (currentIndex - 1) % questionBank.size
    }
}