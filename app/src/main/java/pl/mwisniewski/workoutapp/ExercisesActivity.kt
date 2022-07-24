package pl.mwisniewski.workoutapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pl.mwisniewski.workoutapp.databinding.ActivityExercisesBinding
import pl.mwisniewski.workoutapp.ui.ExerciseViewModel

@AndroidEntryPoint
class ExercisesActivity : AppCompatActivity() {
    private val exerciseViewModel: ExerciseViewModel by viewModels()

    private lateinit var binding: ActivityExercisesBinding // TODO: https://developer.android.com/topic/libraries/view-binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercises)

        lifecycle.coroutineScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                exerciseViewModel.uiState.collect { uiState ->
                    uiState.userMessages.firstOrNull()?.let {
                        // TODO: Show snackbar with user message

                        // TODO: after message is displayed, notify view model
                        exerciseViewModel.userMessagesShown()
                    }
                    // TODO("Show progress bar if it should be present")
                    // TODO("Update UI elements")
                }
            }
        }
    }
}