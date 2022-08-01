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
import pl.mwisniewski.workoutapp.ui.WorkoutViewModel
import pl.mwisniewski.workoutapp.ui.WorkoutsAdapter

@AndroidEntryPoint
class WorkoutsFragment : Fragment() {
    private val workoutViewModel: WorkoutViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_workouts, container, false)

        val workoutsAdapter = WorkoutsAdapter()
        val recyclerView: RecyclerView = view!!.findViewById(R.id.workouts_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = workoutsAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                workoutViewModel.fetchWorkouts()
                workoutViewModel.uiState.collect { uiState ->
                    uiState.userMessage?.let {
                        snackbar(it.message)
                        workoutViewModel.userMessagesShown()
                    }
                    workoutsAdapter.submitList(uiState.workoutItems)
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