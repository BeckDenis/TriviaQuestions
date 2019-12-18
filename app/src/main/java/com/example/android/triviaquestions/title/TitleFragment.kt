package com.example.android.triviaquestions.title


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.example.android.triviaquestions.R
import com.example.android.triviaquestions.state.LevelState
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_title.*

/**
 * A simple [Fragment] subclass.
 */
class TitleFragment : Fragment() {
    private var levelState = LevelState.EASY

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_title, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chip_level.setOnCheckedChangeListener { _, checkedId: Int ->
            when(checkedId) {
                chip_easy.id -> levelState = LevelState.EASY
                chip_medium.id -> levelState = LevelState.MEDIUM
                chip_hard.id -> levelState = LevelState.HARD
            }
        }

        start_button.setOnClickListener {
            findNavController().navigate(
                TitleFragmentDirections.actionTitleFragmentToGameFragment(
                    levelState
                )
            )
        }
    }
}
