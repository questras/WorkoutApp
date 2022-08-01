package pl.mwisniewski.workoutapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.coroutineScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.mwisniewski.workoutapp.ui.*

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

    fun addWorkoutButtonClicked(view: View) {
        val name = findViewById<EditText>(R.id.add_workout_name_edit).text.toString()
        val breakTime = findViewById<EditText>(R.id.add_workout_break_edit).text.toString()

        if (name.isEmpty() or breakTime.isEmpty()) {
            snackbar("Fields cannot be empty!").show()
            return
        } else if (name.length > MAX_NAME_LENGTH) {
            snackbar("Name cannot be longer than ${MAX_NAME_LENGTH}!").show()
            return
        }

        val exercises = mutableListOf<AddWorkoutExerciseSetRequest>()
        var currentView: LinearLayoutCompat? = findViewById(R.id.add_exercise_item_in_workout)
        while (currentView != null) {
            exercises.add(mapViewToRequest(currentView))
            currentView.id = currentView.id + 1

            currentView = findViewById(R.id.add_exercise_item_in_workout)
        }

        if (exercises.isEmpty()) {
            snackbar("Add at least one exercise!").show()
        } else {
            workoutViewModel.addWorkout(AddWorkoutRequest(name, breakTime.toInt(), exercises))
            lifecycle.coroutineScope.launch(Dispatchers.IO) {
                workoutViewModel.uiState.collect { uiState ->
                    val message = uiState.userMessage?.message
                    val intent = Intent(view.context, MainActivity::class.java).apply {
                        message?.let { putExtra(SNACKBAR_MESSAGE, it) }
                    }
                    workoutViewModel.userMessagesShown()
                    startActivity(intent)
                }
            }
        }
    }

    private fun mapViewToRequest(view: LinearLayoutCompat): AddWorkoutExerciseSetRequest {
        val name = view
            .findViewById<Spinner>(R.id.add_exercise_in_workout_exercise_spinner)
            .selectedItem
            .toString()
        val sets = view
            .findViewById<EditText>(R.id.add_exercise_in_workout_sets_edit)
            .text.toString()
            .let { if (it.isEmpty()) SETS_DEFAULT else it.toInt() }
        val repeats = view
            .findViewById<EditText>(R.id.add_exercise_in_workout_repeats_edit)
            .text.toString()
            .let { if (it.isEmpty()) REPEATS_DEFAULT else it.toInt() }

        return AddWorkoutExerciseSetRequest(name, sets, repeats)
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

    private fun snackbar(message: String): Snackbar =
        Snackbar.make(
            findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        )

    companion object {
        const val MAX_NAME_LENGTH = 20
        private const val SETS_DEFAULT = 1
        private const val REPEATS_DEFAULT = 8
    }
}