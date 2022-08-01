package pl.mwisniewski.workoutapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.navigation.ui.navigateUp
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.mwisniewski.workoutapp.domain.model.Category
import pl.mwisniewski.workoutapp.ui.AddExerciseRequest
import pl.mwisniewski.workoutapp.ui.ExerciseViewModel

@AndroidEntryPoint
class AddExerciseActivity : AppCompatActivity() {
    private val exerciseViewModel: ExerciseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_exercise)

        val adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, Category.values()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinner = findViewById<Spinner>(R.id.add_exercise_category_spinner)
        spinner.run { this.adapter = adapter }
    }

    fun addExerciseButtonClicked(view: View) {
        val name = findViewById<EditText>(R.id.add_exercise_name_edit).text.toString()
        val category = findViewById<Spinner>(R.id.add_exercise_category_spinner)
            .selectedItem
            .toString()

        if (name.isEmpty() or category.isEmpty()) {
            snackbar("Fields cannot be empty!").show()
        } else if (name.length > MAX_NAME_LENGTH) {
            snackbar("Name cannot be longer than ${MAX_NAME_LENGTH}!").show()
        } else {
            exerciseViewModel.addExercise(AddExerciseRequest(name, category))
            lifecycle.coroutineScope.launch(Dispatchers.IO) {
                exerciseViewModel.uiState.collect { uiState ->
                    val message = uiState.userMessage?.message
                    val intent = Intent(view.context, MainActivity::class.java).apply {
                        message?.let { putExtra(SNACKBAR_MESSAGE, it) }
                    }
                    exerciseViewModel.userMessagesShown()
                    startActivity(intent)
                }
            }
        }
    }

    private fun snackbar(message: String): Snackbar =
        Snackbar.make(
            findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        )

    companion object {
        const val MAX_NAME_LENGTH = 20
    }
}