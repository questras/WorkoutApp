package pl.mwisniewski.workoutapp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pl.mwisniewski.workoutapp.ui.WorkoutItemUiState
import pl.mwisniewski.workoutapp.ui.WorkoutViewModel
import java.util.*

@AndroidEntryPoint
class CurrentWorkoutFragment : Fragment() {
    private val workoutViewModel: WorkoutViewModel by viewModels()
    private lateinit var exercisesQueue: Queue<ExerciseQueueItem>
    private var breakTimeSeconds: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_current_workout, container, false)
        val workoutName: String = requireArguments().getString(WORKOUT_NAME_BUNDLE_ARGUMENT)!!
        println(workoutName)

        viewLifecycleOwner.lifecycleScope.launch {
            workoutViewModel.fetchWorkouts()
            workoutViewModel.uiState.collect { uiState ->
                val workout: WorkoutItemUiState? = uiState
                    .workoutItems.also { println(it) } // TODO: remove print
                    .firstOrNull { it.name == workoutName }

                if (workout != null) {
                    breakTimeSeconds = workout.breakTime
                    exercisesQueue = workout.toExercisesQueue()
                    setupNextExercise()
                }
            }
        }

        return view
    }

    private fun WorkoutItemUiState.toExercisesQueue(): Queue<ExerciseQueueItem> =
        this.exerciseSets
            .map { exerciseSet ->
                List(exerciseSet.sets) {
                    ExerciseQueueItem(
                        exerciseSet.exerciseName,
                        exerciseSet.repeats
                    )
                }
            }
            .reduce { a, b -> a + b }
            .let { LinkedList(it) }

    private fun setupNextExercise() {
        val queueItem = exercisesQueue.poll()

        if (queueItem == null) {
            endWorkout()
        } else {
            getCurrentWorkoutViewSet()
                ?.let { viewSet ->
                    viewSet.exerciseDescription.text =
                        "Do ${queueItem.repeats} repeats of ${queueItem.name}"
                    viewSet.finishedButton.setOnClickListener { setupBreak() }
                    viewSet.setVisibility(isExerciseScreen = true)
                }
        }
    }

    private fun setupBreak() {
        getCurrentWorkoutViewSet()
            ?.let { viewSet ->
                val timer = setupBreakTimer()
                viewSet.breakTimerText.text = breakTimeSeconds.toString()
                viewSet.setVisibility(isExerciseScreen = false)

                timer.start()
            }
    }

    private fun setupBreakTimer(): BreakCountDownTimer =
        BreakCountDownTimer(
            getCurrentWorkoutViewSet()!!.breakTimerText,
            { setupNextExercise() },
            breakTimeSeconds
        )

    private fun getCurrentWorkoutViewSet(): CurrentWorkoutViewSet? =
        view
            ?.findViewById<LinearLayoutCompat>(R.id.current_workout_layout)
            ?.let { layout ->
                CurrentWorkoutViewSet(
                    layout.findViewById(R.id.current_exercise_description_text),
                    layout.findViewById(R.id.exercise_finished_button),
                    layout.findViewById(R.id.break_text),
                    layout.findViewById(R.id.break_timer_text)
                )
            }


    private fun endWorkout() {
        val intent = Intent(view!!.context, MainActivity::class.java).apply {
            putExtra(SNACKBAR_MESSAGE, WORKOUT_FINISHED_MESSAGE)
        }
        startActivity(intent)
    }

    private data class ExerciseQueueItem(
        val name: String, val repeats: Int
    )

    private data class CurrentWorkoutViewSet(
        val exerciseDescription: TextView,
        val finishedButton: Button,
        val breakText: TextView,
        val breakTimerText: TextView
    ) {
        fun setVisibility(isExerciseScreen: Boolean) {
            if (isExerciseScreen) {
                exerciseDescription.visibility = View.VISIBLE
                finishedButton.visibility = View.VISIBLE
                breakText.visibility = View.GONE
                breakTimerText.visibility = View.GONE
            } else {
                breakText.visibility = View.VISIBLE
                breakTimerText.visibility = View.VISIBLE
                exerciseDescription.visibility = View.GONE
                finishedButton.visibility = View.GONE
            }
        }
    }

    private class BreakCountDownTimer(
        private val breakTimerText: TextView,
        private val onFinishFn: () -> Unit,
        breakTimeSeconds: Int
    ) : CountDownTimer((breakTimeSeconds * 1000).toLong(), 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val secondUntilFinished: Long = millisUntilFinished / 1000
            breakTimerText.text = secondUntilFinished.toString()
        }

        override fun onFinish() {
            onFinishFn()
        }
    }

    companion object {
        const val WORKOUT_FINISHED_MESSAGE = "You have finished the workout!"
    }
}