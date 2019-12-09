package com.example.android.triviaquestions.game


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.example.android.checkbuttontest.Question
import com.example.android.checkbuttontest.questions
import com.example.android.triviaquestions.*
import com.example.android.triviaquestions.state.GameState
import com.example.android.triviaquestions.state.LevelState
import kotlinx.android.synthetic.main.fragment_game.*
import kotlin.math.min

class GameFragment : Fragment() {

    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private var numQuestions = 0
    private var gameState = GameState.ACTIVE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args =
            GameFragmentArgs.fromBundle(
                arguments!!
            )
        val levelState = args.levelState
        val numberOfQuestions = when (levelState) {
            LevelState.EASY -> 3
            LevelState.MEDIUM -> 5
            LevelState.HARD -> 9
        }
        numQuestions = min((questions.size + 1) / 2, numberOfQuestions)

        randomizeQuestions()

        submitButton.setOnClickListener {
            when (gameState) {
                GameState.ACTIVE -> {
                    val checkedId = questionRadioGroup.checkedRadioButtonId
                    if (-1 != checkedId) {
                        var answerIndex = 0
                        when (checkedId) {
                            R.id.secondAnswerRadioButton -> answerIndex = 1
                            R.id.thirdAnswerRadioButton -> answerIndex = 2
                            R.id.fourthAnswerRadioButton -> answerIndex = 3
                        }
                        if (answers[answerIndex] == currentQuestion.answers[0]) {
                            questionIndex++

                            if (questionIndex < numQuestions) {
                                Toast.makeText(context, "Right! It is true.", Toast.LENGTH_LONG).show()
                                setQuestion()
                            } else {
                                view.findNavController()
                                    .navigate(
                                        GameFragmentDirections.actionGameFragmentToGameWonFragment(
                                            numQuestions,
                                            questionIndex,
                                            levelState
                                        )
                                    )
                            }
                        } else {
                            Toast.makeText(context, "Wrong! It is false.", Toast.LENGTH_LONG).show()
                            gameState =
                                GameState.FINISHED
                            val color = ContextCompat.getColor(context!!,
                                R.color.colorAccent
                            )
                            when (currentQuestion.answers[0]) {
                                firstAnswerRadioButton.text -> firstAnswerRadioButton.setBackgroundColor(color)
                                secondAnswerRadioButton.text -> secondAnswerRadioButton.setBackgroundColor(color)
                                thirdAnswerRadioButton.text -> thirdAnswerRadioButton.setBackgroundColor(color)
                                else -> fourthAnswerRadioButton.setBackgroundColor(color)
                            }
                            submitButton.text = getString(R.string.restart_button)
                        }
                    }
                }
                GameState.FINISHED -> {
                    view.findNavController().navigate(
                        GameFragmentDirections.actionGameFragmentSelf(
                            levelState
                        )
                    )
                }
            }

        }
    }

    private fun randomizeQuestions() {
        questions.shuffle()
        questionIndex = 0
        setQuestion()
    }

    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        answers = currentQuestion.answers.toMutableList()
        answers.shuffle()
        updateView()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_android_trivia_question, questionIndex + 1, numQuestions)
    }

    private fun updateView() {
        setText()
        setAnswers()
    }

    private fun setText() {
        questionText.text = currentQuestion.text
    }

    private fun setAnswers() {
        firstAnswerRadioButton.text = answers[0]
        secondAnswerRadioButton.text = answers[1]
        thirdAnswerRadioButton.text = answers[2]
        fourthAnswerRadioButton.text = answers[3]
    }
}
