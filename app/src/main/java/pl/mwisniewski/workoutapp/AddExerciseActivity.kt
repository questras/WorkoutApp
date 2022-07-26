package pl.mwisniewski.workoutapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
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
        val description = findViewById<EditText>(R.id.add_exercise_description_edit).text.toString()
        val category = findViewById<Spinner>(R.id.add_exercise_category_spinner)
            .selectedItem
            .toString()

        if (name.isEmpty() or description.isEmpty() or category.isEmpty()) {
            emptyFieldsSnackbar().show()
        } else {
            // TODO: what if exercise already exists?
            exerciseViewModel.addExercise(AddExerciseRequest(name, description, category))
            val intent = Intent(view.context, MainActivity::class.java).apply {
                putExtra(SNACKBAR_MESSAGE, EXERCISE_CREATED_STRING)
            }
            startActivity(intent)
        }
    }

    private fun emptyFieldsSnackbar(): Snackbar =
        Snackbar.make(
            findViewById(android.R.id.content),
            R.string.fields_cannot_be_empty_message,
            Snackbar.LENGTH_SHORT
        )

    companion object {
        private const val EXERCISE_CREATED_STRING = "Exercise created!"
    }
}