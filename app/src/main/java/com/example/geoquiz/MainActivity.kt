package com.example.geoquiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlin.math.roundToInt

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var trueButton:Button
    private lateinit var falseButton:Button
    private lateinit var nextButton:ImageButton
    private lateinit var prevButton:ImageButton
    private lateinit var cheatButton: Button
    private lateinit var cheatTextView: TextView


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

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
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
        if (quizViewModel.checked[quizViewModel.currentIndex] == 1){
            falseButton.isEnabled = false
            trueButton.isEnabled = false
        }
        else {
            falseButton.isEnabled = true
            trueButton.isEnabled = true
        }

        if (quizViewModel.checked.sum() == quizViewModel.answers.size){
            val res = "${quizViewModel.answers.sum()} of ${quizViewModel.answers.size}: ${(quizViewModel.answers.sum().toDouble() / quizViewModel.answers.size * 100).roundToInt()} %\n${quizViewModel.isCheater.count() {it}} cheats used"
            Toast.makeText(this, res, Toast.LENGTH_LONG).show()

            quizViewModel.default()

            falseButton.isEnabled = true
            trueButton.isEnabled = true
            cheatButton.isEnabled = true
        }

        cheatTextView.text = "${quizViewModel.isCheater.count() {it}} of 3 cheats used"

        if(quizViewModel.isCheater.count() {it} > 2)
            cheatButton.isEnabled = false
    }

    private fun updateQuestion() {
        //val questionTextResId = questionBank[currentIndex].textResId
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean){
        //val correctAnswer = questionBank[currentIndex].answer
//        val messageResId = if (correctAnswer == userAnswer) R.string.correct_toast
//        else R.string.incorrect_toast

        val correctAnswer: Boolean = quizViewModel.currentQuestionAnswer

        val messageResId = when {
            quizViewModel.isCheater[quizViewModel.currentIndex] -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        if (correctAnswer == userAnswer)
            quizViewModel.answers[quizViewModel.currentIndex]++

        quizViewModel.checked[quizViewModel.currentIndex]++

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }


    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)


        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        val provider: ViewModelProvider = ViewModelProviders.of(this)
        val quizViewModel = provider.get(QuizViewModel::class.java)
        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")


        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        cheatButton = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)
        cheatTextView = findViewById(R.id.cheats_used_text_view)

        val apiLevelTextView: TextView = findViewById(R.id.api_level_text_view)
        apiLevelTextView.text = "API Level ${android.os.Build.VERSION.SDK_INT}"


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
            quizViewModel.moveToNext()
            updateState()
            updateQuestion()
        }

        nextButton.setOnClickListener{
            quizViewModel.moveToNext()
            updateState()
            updateQuestion()
        }

        prevButton.setOnClickListener{
            quizViewModel.moveBack()
            updateState()
            updateQuestion()
        }

        cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)

            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }

        updateState()
        updateQuestion()

        Log.d(TAG, "Current question index: $currentIndex")
        try {
            val question = quizViewModel.currentQuestionText
        } catch (ex: ArrayIndexOutOfBoundsException) {
            Log.e(TAG, "Index was out of bounds", ex)
        }

    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater[quizViewModel.currentIndex] =
                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }
}


