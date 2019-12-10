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
import kotlinx.android.synthetic.main.fragment_title.*

/**
 * A simple [Fragment] subclass.
 */
class TitleFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var levelState: LevelState

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_title, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ArrayAdapter.createFromResource(context!!,
            R.array.planets_array,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_item)
            level_spinner.adapter = adapter
        }

        level_spinner.onItemSelectedListener = this

        start_button.setOnClickListener {
            findNavController().navigate(
                TitleFragmentDirections.actionTitleFragmentToGameFragment(
                    levelState
                )
            )
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        levelState = LevelState.EASY
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(position) {
            0 -> levelState = LevelState.EASY
            1 -> levelState = LevelState.MEDIUM
            2 -> levelState = LevelState.HARD
        }
    }
}
