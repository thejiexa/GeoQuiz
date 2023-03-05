<p align = "center">МИНИСТЕРСТВО НАУКИ И ВЫСШЕГО ОБРАЗОВАНИЯ<br>
РОССИЙСКОЙ ФЕДЕРАЦИИ<br>
ФЕДЕРАЛЬНОЕ ГОСУДАРСТВЕННОЕ БЮДЖЕТНОЕ<br>
ОБРАЗОВАТЕЛЬНОЕ УЧРЕЖДЕНИЕ ВЫСШЕГО ОБРАЗОВАНИЯ<br>
«САХАЛИНСКИЙ ГОСУДАРСТВЕННЫЙ УНИВЕРСИТЕТ»</p>
<br><br><br><br><br><br>
<p align = "center">Институт естественных наук и техносферной безопасности<br>Кафедра информатики<br>Григораш Алексей Владимирович</p>
<br><br><br>
<p align = "center">Лабораторная работа № 2<br>«<strong>Android и модель MVC</strong>»<br>01.03.02 Прикладная математика и информатика</p>
<br><br><br><br><br><br><br><br><br><br><br><br>
<p align = "right">Научный руководитель<br>
Соболев Евгений Игоревич</p>
<br><br><br>
<p align = "center">г. Южно-Сахалинск<br>2023 г.</p>
<br><br><br><br><br><br><br><br>

## Введение:
Android Studio — интегрированная среда разработки производства Google, с помощью которой разработчикам становятся доступны инструменты для создания приложений на платформе Android OS

MVC расшифровывается как «модель-представление-контроллер» (от англ. model-view-controller). Это способ организации кода, который предполагает выделение блоков, отвечающих за решение разных задач. Один блок отвечает за данные приложения, другой отвечает за внешний вид, а третий контролирует работу приложения.

## Задачи:
1. Добавление слушателя для TextView 
Кнопка NEXT удобна, но было бы неплохо, если бы пользователь мог переходить к следующему вопросу простым нажатием на виджет TextView. 

    Подсказка. Для TextView можно использовать слушателя View.OnClickListener, который использовался с Button, потому что класс TextView также является производным от View.

2. Добавление кнопки возврата
Добавьте кнопку для возвращения к предыдущему вопросу. Пользовательский интерфейс должен выглядеть примерно так, как показано на рис. 2.13. 

3. От Button к ImageButton 
Возможно, пользовательский интерфейс будет смотреться еще лучше, если на кнопках будут отображаться только значки, как на рис. 2.14.

    Для этого оба виджета должны относиться к типу ImageButton (вместо обычного Button). Виджет ImageButton является производным от ImageView, в отличие от виджета Button, производного от TextView. Диаграммы их наследования изображены на рис. 2.15. Атрибуты text и drawable кнопки NEXT можно заменить одним атрибутом ImageView:


    <~~Button~~ ImageButton<br>
    android:id="@+id/next_button"<br>
    android:layout_width="wrap_content"<br>
    android:layout_height="wrap_content"<br>
    ~~android:text="@string/next_button"~~<br>
    ~~android:drawableEnd="@drawable/arrow_right"~~<br>
    ~~android:drawablePadding="4dp"~~<br>
    android:src="@drawable/arrow_right"/><br>
    <br>

<!-- ![1](https://trueimages.ru/img/9b/fe/e30c9f36.png)
![1](https://trueimages.ru/img/0b/46/220c9f36.png) -->
<img align = "center" src="https://trueimages.ru/img/9b/fe/e30c9f36.png" width="500" >
<img align = "center" src="https://trueimages.ru/img/0b/46/220c9f36.png" width="500" >

    Конечно, вам также придется внести изменения в MainActivity, чтобы этот класс работал с ImageButton. После того как вы замените эти кнопки на кнопки ImageButton, Android Studio выдаст предупреждение об отсутствии атрибута android:contentDescription. Этот атрибут обеспечивает доступность контента для читателей с ослабленным зрением. Строка, заданная этому атрибуту, читается экранным диктором (при включении соответствующих настроек в системе пользователя). Наконец, добавьте атрибут android:contentDescription в каждый элемент ImageButton.
## Решение:

MainActivity.kt:
```kotlin
package com.example.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton:Button
    private lateinit var falseButton:Button
//    private lateinit var nextButton:Button
//    private lateinit var prevButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var nextButton:ImageButton
    private lateinit var prevButton:ImageButton

    private var currentIndex = 0

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean){
        val correctAnswer = questionBank[currentIndex].answer
        val messageResId = if (correctAnswer == userAnswer) R.string.correct_toast
        else R.string.incorrect_toast

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }

        questionTextView.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        nextButton.setOnClickListener{
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        prevButton.setOnClickListener{
            currentIndex = if (currentIndex == 0) questionBank.size - 1
            else (currentIndex - 1) % questionBank.size
            updateQuestion()
        }

        updateQuestion()

    }
}
```
activity_main.xml:
```kotlin
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:orientation="vertical"
    tools:context=".MainActivity">

    <TextView
        android:id="@+id/question_text_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:padding="24dp"
        android:gravity="center"
        tools:text="@string/question_australia" />

    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal">

        <Button
            android:id="@+id/true_button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:layout_margin="2dp"
            android:text="@string/true_button" />

        <Button
            android:id="@+id/false_button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:layout_margin="2dp"
            android:text="@string/false_button" />

    </LinearLayout>

    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal">

        <ImageButton
            android:id="@+id/prev_button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@drawable/arrow_left"
            android:contentDescription="@string/prev_button" />

        <ImageButton
            android:id="@+id/next_button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@drawable/arrow_right"
            android:contentDescription="@string/next_button"/>

<!--        <Button-->
<!--            android:layout_margin="2dp"-->
<!--            android:id="@+id/prev_button"-->
<!--            android:layout_width="wrap_content"-->
<!--            android:layout_height="wrap_content"-->
<!--            android:text="@string/prev_button"-->
<!--            android:drawableLeft ="@drawable/arrow_left"-->
<!--            android:drawablePadding="4dp" />-->

<!--        <Button-->
<!--            android:layout_margin="2dp"-->
<!--            android:id="@+id/next_button"-->
<!--            android:layout_width="wrap_content"-->
<!--            android:layout_height="wrap_content"-->
<!--            android:text="@string/next_button"-->
<!--            android:drawableEnd="@drawable/arrow_right"-->
<!--            android:drawablePadding="4dp" />-->

    </LinearLayout>

</LinearLayout>
```
## Вывод:
В ходе выполнения задач изучил основы Model-View-Controller (MVC, «Модель-Представление-Контроллер») 