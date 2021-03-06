package pl.mwisniewski.workoutapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

const val SNACKBAR_MESSAGE = "pl.mwisniewski.workoutapp.SNACKBAR_MESSAGE"
const val WORKOUT_NAME_BUNDLE_ARGUMENT = "workoutName"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        findViewById<NavigationView>(R.id.nav_view)
            .setupWithNavController(navController)

        showSnackbarIfNeeded()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun goToAddExerciseActivity(view: View) {
        val intent = Intent(this, AddExerciseActivity::class.java)
        startActivity(intent)
    }

    fun goToAddWorkoutActivity(view: View) {
        val intent = Intent(this, AddWorkoutActivity::class.java)
        startActivity(intent)
    }

    fun goToCurrentWorkoutFragment(view: View) {
        val workoutName = findViewById<Spinner>(R.id.start_workout_spinner).selectedItem.toString()
        if (workoutName.isNotEmpty()) {
            val navController = findNavController(R.id.nav_host_fragment)
            val bundle = bundleOf(WORKOUT_NAME_BUNDLE_ARGUMENT to workoutName)
            navController.navigate(R.id.currentWorkoutFragment, bundle)
        }
    }

    private fun showSnackbarIfNeeded() {
        val snackbarMessage = intent.getStringExtra(SNACKBAR_MESSAGE)

        snackbarMessage?.let {
            Snackbar
                .make(findViewById(android.R.id.content), snackbarMessage, Snackbar.LENGTH_LONG)
                .show()
        }
    }
}