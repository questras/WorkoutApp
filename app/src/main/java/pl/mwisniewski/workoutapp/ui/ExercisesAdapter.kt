package pl.mwisniewski.workoutapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.mwisniewski.workoutapp.R

class ExercisesAdapter :
    ListAdapter<ExerciseItemUiState, ExercisesAdapter.ExerciseViewHolder>(ExerciseDiffCallback) {

    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var currentExercise: ExerciseItemUiState? = null
        private val exerciseNameView: TextView = itemView.findViewById(R.id.exercise_item_text)
        private val deleteExerciseButton: ImageButton =
            itemView.findViewById(R.id.exercise_item_delete_button)

        fun bind(exercise: ExerciseItemUiState) {
            currentExercise = exercise
            exerciseNameView.text = "${exercise.name} (${exercise.category})"
            deleteExerciseButton.setOnClickListener { exercise.onDelete() }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.exercise_item, parent, false)

        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = getItem(position)
        holder.bind(exercise)
    }
}

object ExerciseDiffCallback : DiffUtil.ItemCallback<ExerciseItemUiState>() {
    override fun areItemsTheSame(oldItem: ExerciseItemUiState,
                                 newItem: ExerciseItemUiState): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ExerciseItemUiState,
                                    newItem: ExerciseItemUiState): Boolean {
        return oldItem.name == newItem.name
    }
}