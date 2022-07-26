package pl.mwisniewski.workoutapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import pl.mwisniewski.workoutapp.domain.model.Category

class AddExerciseActivity : AppCompatActivity() {
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
}