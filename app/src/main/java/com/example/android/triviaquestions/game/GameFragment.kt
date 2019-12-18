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
import androidx.navigation.fragment.findNavController
import com.example.android.checkbuttontest.Question
import com.example.android.checkbuttontest.questions
import com.example.android.triviaquestions.*
import com.example.android.triviaquestions.state.GameState
import com.example.android.triviaquestions.state.LevelState
import kotlinx.android.synthetic.main.fragment_game.*
import kotlin.math.min

class GameFragment : Fragment() {

    private lateinit var currentQuestion: Question
    private lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private var numberOfQuestions = 0
    private var gameState = GameState.ACTIVE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val levelState = GameFragmentArgs.fromBundle(arguments!!).levelState
        numberOfQuestions = checkLevelState(levelState)

        randomizeQuestions()

        submitButton.setOnClickListener { checkAnswer(view, levelState) }
    }

    private fun checkAnswer(view: View, levelState: LevelState) {
        when (gameState) {
            GameState.ACTIVE -> {
                val checkedId = questionRadioGroup.checkedRadioButtonId
                if (-1 != checkedId) {
                    val answerIndex = checkSelectedRadioButton(checkedId)
                    if (answers[answerIndex] == currentQuestion.answers[0]) {
                        questionIndex++
                        checkEndOfTest(view, levelState)
                    } else {
                        wrongAnswer()
                    }
                }
            }
            GameState.FINISHED -> refreshGameFragment(levelState)
        }
    }

    private fun checkSelectedRadioButton(checkedId: Int): Int {
        return when (checkedId) {
            R.id.firstAnswerRadioButton -> 0
            R.id.secondAnswerRadioButton -> 1
            R.id.thirdAnswerRadioButton -> 2
            else -> 3
        }
    }

    private fun checkEndOfTest(view: View, levelState: LevelState) {
        if (questionIndex < numberOfQuestions) {
            setQuestion()
        } else {
            goToWonFragment(view, levelState)
        }
    }

    private fun wrongAnswer() {
        Toast.makeText(context, getString(R.string.wrong_answer), Toast.LENGTH_LONG).show()
        gameState = GameState.FINISHED
        highlightCorrectAnswer()
        submitButton.text = getString(R.string.restart_button)
    }

    private fun highlightCorrectAnswer() {
        val color = ContextCompat.getColor(context!!, R.color.colorAccent)
        when (currentQuestion.answers[0]) {
            firstAnswerRadioButton.text -> firstAnswerRadioButton.setBackgroundColor(color)
            secondAnswerRadioButton.text -> secondAnswerRadioButton.setBackgroundColor(color)
            thirdAnswerRadioButton.text -> thirdAnswerRadioButton.setBackgroundColor(color)
            else -> fourthAnswerRadioButton.setBackgroundColor(color)
        }
    }

    private fun refreshGameFragment(levelState: LevelState) =
        findNavController().navigate(GameFragmentDirections.actionGameFragmentSelf(levelState))

    private fun goToWonFragment(view: View, levelState: LevelState) {
        view.findNavController().navigate(
            GameFragmentDirections.actionGameFragmentToGameWonFragment(
                numberOfQuestions,
                questionIndex,
                levelState
            )
        )
    }

    private fun checkLevelState(levelState: LevelState): Int {
        return when (levelState) {
            LevelState.EASY -> 3
            LevelState.MEDIUM -> 5
            LevelState.HARD -> 9
        }
    }

    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        answers = currentQuestion.answers.toMutableList()
        answers.shuffle()
        updateView()
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.title_android_trivia_question, questionIndex + 1, numberOfQuestions)
    }

    private fun randomizeQuestions() {
        questions.shuffle()
        questionIndex = 0
        setQuestion()
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
