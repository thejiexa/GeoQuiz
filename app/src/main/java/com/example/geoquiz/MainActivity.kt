package com.example.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import kotlin.math.roundToInt

private const val TAG = "MainActivity"

private val questionBank = listOf(
    Question(R.string.question_australia, true),
    Question(R.string.question_oceans, true),
    Question(R.string.question_mideast, false),
    Question(R.string.question_africa, false),
    Question(R.string.question_americas, true),
    Question(R.string.question_asia, true)
)
private var currentIndex = 0
private var answers = IntArray(questionBank.size)
private var checked = IntArray(questionBank.size)

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton:Button
    private lateinit var falseButton:Button
    //    private lateinit var nextButton:Button
    //    private lateinit var prevButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var nextImageButton:ImageButton
    private lateinit var prevImageButton:ImageButton

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateState(){
        // LAB 3
        if (checked[currentIndex] == 1){
            falseButton.isEnabled = false
            trueButton.isEnabled = false
        }
        else {
            falseButton.isEnabled = true
            trueButton.isEnabled = true
        }

        if (checked.sum() == answers.size){
            val res = "${answers.sum()} of ${answers.size}: ${(answers.sum().toDouble() / answers.size * 100).roundToInt()} %"
            Toast.makeText(this, res, Toast.LENGTH_LONG).show()

            currentIndex = 0
            checked = IntArray(questionBank.size)
            answers = IntArray(questionBank.size)

            falseButton.isEnabled = true
            trueButton.isEnabled = true
        }
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean){
        val correctAnswer = questionBank[currentIndex].answer
        val messageResId = if (correctAnswer == userAnswer) R.string.correct_toast
        else R.string.incorrect_toast

        // LAB 3
        if (correctAnswer == userAnswer)
            answers[currentIndex]++
        checked[currentIndex]++

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextImageButton = findViewById(R.id.next_button)
        prevImageButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener {
            checkAnswer(true)
            updateState()
            updateQuestion()
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
            updateState()
            updateQuestion()
        }

        questionTextView.setOnClickListener {
            if (currentIndex < questionBank.size - 1)
                currentIndex = (currentIndex + 1) % questionBank.size

            updateState()
            updateQuestion()
        }

        nextImageButton.setOnClickListener{
            if (currentIndex < questionBank.size - 1)
                currentIndex = (currentIndex + 1) % questionBank.size

            updateState()
            updateQuestion()
        }

        prevImageButton.setOnClickListener{
            if (currentIndex > 0)
               currentIndex = (currentIndex - 1) % questionBank.size

            updateState()
            updateQuestion()
        }

        updateState()
        updateQuestion()

        Log.d(TAG, "Current question index: $currentIndex")
        try {
            val question = questionBank[currentIndex]
        } catch (ex: ArrayIndexOutOfBoundsException) {
            Log.e(TAG, "Index was out of bounds", ex)
        }

    }
}