package pl.mwisniewski.workoutapp

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.coroutineScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pl.mwisniewski.workoutapp.ui.ExerciseItemUiState
import pl.mwisniewski.workoutapp.ui.ExerciseViewModel
import pl.mwisniewski.workoutapp.ui.WorkoutViewModel

@AndroidEntryPoint
class AddWorkoutActivity : AppCompatActivity() {
    private val workoutViewModel: WorkoutViewModel by viewModels()
    private val exerciseViewModel: ExerciseViewModel by viewModels()
    private var exercisesToChoose: List<String> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_workout)

        lifecycle.coroutineScope.launch {
            exerciseViewModel.fetchExercises()
            exerciseViewModel.uiState.collect { uiState ->
                exercisesToChoose = uiState.exerciseItems.map(ExerciseItemUiState::name)
            }
        }
    }

    fun addNextExerciseButtonClicked(view: View) {
        val linearLayoutForm: LinearLayoutCompat =
            this.findViewById(R.id.add_workout_activity_layout)

        val viewToAdd = prepareNewAddExerciseInWorkoutView()

        linearLayoutForm.addView(viewToAdd)
    }

    private fun prepareNewAddExerciseInWorkoutView(): View {
        val viewToAdd: LinearLayoutCompat = this.layoutInflater.inflate(
            R.layout.add_exercise_item_in_workout,
            null
        ) as LinearLayoutCompat

        viewToAdd
            .findViewById<Spinner>(R.id.add_exercise_in_workout_exercise_spinner)
            .setupWithExercises()

        return viewToAdd
    }

    private fun Spinner.setupWithExercises(): Spinner {
        val adapter = ArrayAdapter(
            this@AddWorkoutActivity,
            android.R.layout.simple_spinner_item,
            exercisesToChoose
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.adapter = adapter

        return this
    }

    private fun emptyFieldsSnackbar(): Snackbar =
        Snackbar.make(
            findViewById(android.R.id.content),
            R.string.fields_cannot_be_empty_message,
            Snackbar.LENGTH_SHORT
        )

    companion object {
        private var currentId: Int = 42000 // TODO
        private const val WORKOUT_CREATED_STRING = "Workout created!"
    }

    private data class IdHolder( // TODO
        val spinnerId: Int,
        val setsEditId: Int,
        val repeatsEditId: Int
    )
}