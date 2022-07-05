package pl.mwisniewski.workoutapp.domain.model

enum class Category {
    UPPER_BODY, LOWER_BODY, CARDIO, ABS, STRETCHING;

    override fun toString(): String =
        when (this) {
            UPPER_BODY -> "upper body"
            LOWER_BODY -> "lower body"
            CARDIO -> "cardio"
            ABS -> "abs"
            STRETCHING -> "stretching"
        }
}
