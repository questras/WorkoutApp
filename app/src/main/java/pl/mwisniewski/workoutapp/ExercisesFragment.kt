package pl.mwisniewski.workoutapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pl.mwisniewski.workoutapp.ui.ExerciseViewModel
import pl.mwisniewski.workoutapp.ui.ExercisesAdapter

@AndroidEntryPoint
class ExercisesFragment : Fragment() {
    private val exerciseViewModel: ExerciseViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_exercises, container, false)

        val exercisesAdapter = ExercisesAdapter()
        val recyclerView: RecyclerView = view!!.findViewById(R.id.exercises_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = exercisesAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                exerciseViewModel.fetchExercises()
                exerciseViewModel.uiState.collect { uiState ->
                    uiState.userMessage?.let {
                        snackbar(it.message)
                        exerciseViewModel.userMessagesShown()
                    }
                    exercisesAdapter.submitList(uiState.exerciseItems)
                }
            }
        }

        return view
    }

    private fun snackbar(message: String) {
        Snackbar.make(
            requireView(), message, Snackbar.LENGTH_LONG
        ).show()
    }
}
