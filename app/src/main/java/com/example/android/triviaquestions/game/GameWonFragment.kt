package com.example.android.triviaquestions.game


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.android.triviaquestions.R
import kotlinx.android.synthetic.main.fragment_game_won.*

class GameWonFragment : Fragment() {

    private lateinit var args: GameWonFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        args = GameWonFragmentArgs.fromBundle(arguments!!)
        return inflater.inflate(R.layout.fragment_game_won, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        won_score.text = getString(R.string.won_score, args.numQuestions, args.numCorrect)

        nextMatchButton.setOnClickListener {
            findNavController().navigate(
                GameWonFragmentDirections.actionGameWonFragmentToGameFragment(args.levelState)
            )
        }
    }


}
