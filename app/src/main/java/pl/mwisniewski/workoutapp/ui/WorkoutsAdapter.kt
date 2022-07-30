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

class WorkoutsAdapter :
    ListAdapter<WorkoutItemUiState, WorkoutsAdapter.WorkoutViewHolder>(WorkoutDiffCallback) {
    class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var currentWorkout: WorkoutItemUiState? = null
        private val workoutTextView: TextView = itemView.findViewById(R.id.workout_item_text)
        private val deleteWorkoutButton: ImageButton =
            itemView.findViewById(R.id.workout_item_delete_button)

        fun bind(workout: WorkoutItemUiState) {
            currentWorkout = workout
            workoutTextView.text = "${workout.name} with break ${workout.breakTime} seconds"
            deleteWorkoutButton.setOnClickListener { workout.onDelete() }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.workout_item, parent, false)

        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = getItem(position)
        holder.bind(workout)
    }
}

object WorkoutDiffCallback : DiffUtil.ItemCallback<WorkoutItemUiState>() {
    override fun areItemsTheSame(oldItem: WorkoutItemUiState,
                                 newItem: WorkoutItemUiState): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: WorkoutItemUiState,
                                    newItem: WorkoutItemUiState): Boolean {
        return oldItem.name == newItem.name
    }
}