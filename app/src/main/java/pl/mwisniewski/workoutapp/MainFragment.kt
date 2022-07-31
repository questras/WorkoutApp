package pl.mwisniewski.workoutapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pl.mwisniewski.workoutapp.ui.WorkoutViewModel

@AndroidEntryPoint
class MainFragment : Fragment() {
    private val workoutViewModel: WorkoutViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            workoutViewModel.fetchWorkouts()
            workoutViewModel.uiState.collect { uiState ->
                val adapter = ArrayAdapter(
                    this@MainFragment.requireContext(),
                    android.R.layout.simple_spinner_item,
                    uiState.workoutItems.map { it.name }
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                val spinner = view.findViewById<Spinner>(R.id.start_workout_spinner)
                spinner.adapter = adapter
            }
        }

        return view
    }
}