package com.example.yoga.yogatoolkit

class AngleNodeDef {
     object Constants {
        /*
        11. left_shoulder
        12. right_shoulder
        13. left_elbow
        14. right_elbow
        15. left_wrist
        16. right_wrist
        17. left_pinky
        18. right_pinky
        19. left_index (中指?)
        20. right_index
        21. left_thunb
        22. right_thunb
        23. left_hip (骨盆?)
        24. right_hip
        25. left_knee
        26. right_knee
        27. left_ankle
        28. right_ankle
        29. left_heel (腳跟)
        30. right_heel
        31. left_foot_index
        32. right_foot_index (腳中指)
        */

        const val NOSE = 0
        const val LEFT_EYE = 2
        const val RIGHT_EYE = 5
        const val LEFT_SHOULDER = 11
        const val RIGHT_SHOULDER = 12
        const val LEFT_ELBOW = 13
        const val RIGHT_ELBOW = 14
        const val LEFT_WRIST = 15
        const val RIGHT_WRIST = 16
        const val LEFT_INDEX = 19
        const val RIGHT_INDEX = 20
        const val LEFT_HIP = 23
        const val RIGHT_HIP = 24
        const val LEFT_KNEE = 25
        const val RIGHT_KNEE = 26
        const val LEFT_ANKLE = 27
        const val RIGHT_ANKLE = 28
        const val LEFT_HEEL = 29
        const val RIGHT_HEEL = 30
        const val LEFT_FOOT_INDEX = 31
        const val RIGHT_FOOT_INDEX = 32
    }
    companion object {
        val WARRIOR_II_ANGLE = mapOf(
                "LEFT_SHOULDER" to listOf(
                        Constants.LEFT_ELBOW,
                        Constants.LEFT_SHOULDER,
                        Constants.LEFT_HIP
                ),
                "RIGHT_SHOULDER" to listOf(
                        Constants.RIGHT_ELBOW,
                        Constants.RIGHT_SHOULDER,
                        Constants.RIGHT_HIP
                ),
                "LEFT_ELBOW" to listOf(
                        Constants.LEFT_SHOULDER,
                        Constants.LEFT_ELBOW,
                        Constants.LEFT_WRIST
                ),
                "RIGHT_ELBOW" to listOf(
                        Constants.RIGHT_SHOULDER,
                        Constants.RIGHT_ELBOW,
                        Constants.RIGHT_WRIST
                ),
                "LEFT_HIP" to listOf(Constants.RIGHT_HIP, Constants.LEFT_HIP, Constants.LEFT_KNEE),
                "RIGHT_HIP" to listOf(
                        Constants.LEFT_HIP,
                        Constants.RIGHT_HIP,
                        Constants.RIGHT_KNEE
                ),
                "LEFT_KNEE" to listOf(
                        Constants.LEFT_HIP,
                        Constants.LEFT_KNEE,
                        Constants.LEFT_ANKLE
                ),
                "RIGHT_KNEE" to listOf(
                        Constants.RIGHT_HIP,
                        Constants.RIGHT_KNEE,
                        Constants.RIGHT_ANKLE
                ),
                "RIGHT_ANKLE" to listOf(
                        Constants.RIGHT_KNEE,
                        Constants.RIGHT_ANKLE,
                        Constants.RIGHT_FOOT_INDEX
                )
        )
        val TREE_ANGLE = mapOf(
                "LEFT_SHOULDER" to listOf(
                        Constants.LEFT_ELBOW,
                        Constants.LEFT_SHOULDER,
                        Constants.LEFT_HIP
                ),
                "RIGHT_SHOULDER" to listOf(
                        Constants.RIGHT_ELBOW,
                        Constants.RIGHT_SHOULDER,
                        Constants.RIGHT_HIP
                ),
                "LEFT_ELBOW" to listOf(
                        Constants.LEFT_SHOULDER,
                        Constants.LEFT_ELBOW,
                        Constants.LEFT_WRIST
                ),
                "RIGHT_ELBOW" to listOf(
                        Constants.RIGHT_SHOULDER,
                        Constants.RIGHT_ELBOW,
                        Constants.RIGHT_WRIST
                ),
                "LEFT_HIP" to listOf(Constants.RIGHT_HIP, Constants.LEFT_HIP, Constants.LEFT_KNEE),
                "RIGHT_HIP" to listOf(
                        Constants.LEFT_HIP,
                        Constants.RIGHT_HIP,
                        Constants.RIGHT_KNEE
                ),
                "LEFT_KNEE" to listOf(
                        Constants.LEFT_HIP,
                        Constants.LEFT_KNEE,
                        Constants.LEFT_ANKLE
                ),
                "RIGHT_KNEE" to listOf(
                        Constants.RIGHT_HIP,
                        Constants.RIGHT_KNEE,
                        Constants.RIGHT_ANKLE
                ),
        )


        val REVERSE_PLANK_ANGLE = mapOf(
                "LEFT_SHOULDER" to listOf(
                        Constants.LEFT_ELBOW,
                        Constants.LEFT_SHOULDER,
                        Constants.LEFT_HIP
                ),
                "RIGHT_SHOULDER" to listOf(
                        Constants.RIGHT_ELBOW,
                        Constants.RIGHT_SHOULDER,
                        Constants.RIGHT_HIP
                ),
                "LEFT_ELBOW" to listOf(
                        Constants.LEFT_SHOULDER,
                        Constants.LEFT_ELBOW,
                        Constants.LEFT_WRIST
                ),
                "RIGHT_ELBOW" to listOf(
                        Constants.RIGHT_SHOULDER,
                        Constants.RIGHT_ELBOW,
                        Constants.RIGHT_WRIST
                ),
                "LEFT_HIP" to listOf(
                        Constants.LEFT_SHOULDER,
                        Constants.LEFT_HIP,
                        Constants.LEFT_KNEE
                ),
                "RIGHT_HIP" to listOf(
                        Constants.RIGHT_SHOULDER,
                        Constants.RIGHT_HIP,
                        Constants.RIGHT_KNEE
                ),
                "LEFT_KNEE" to listOf(
                        Constants.LEFT_HIP,
                        Constants.LEFT_KNEE,
                        Constants.LEFT_ANKLE
                ),
                "RIGHT_KNEE" to listOf(
                        Constants.RIGHT_HIP,
                        Constants.RIGHT_KNEE,
                        Constants.RIGHT_ANKLE
                ),
                "LEFT_WRIST" to listOf(
                        Constants.LEFT_ELBOW,
                        Constants.LEFT_WRIST,
                        Constants.LEFT_INDEX
                ),
                "RIGHT_WRIST" to listOf(
                        Constants.RIGHT_ELBOW,
                        Constants.RIGHT_WRIST,
                        Constants.RIGHT_INDEX
                ),
        )

        val PLANK_ANGLE = mapOf(
                "LEFT_SHOULDER" to listOf(
                        Constants.LEFT_ELBOW,
                        Constants.LEFT_SHOULDER,
                        Constants.LEFT_HIP
                ),
                "RIGHT_SHOULDER" to listOf(
                        Constants.RIGHT_ELBOW,
                        Constants.RIGHT_SHOULDER,
                        Constants.RIGHT_HIP
                ),
                "LEFT_ELBOW" to listOf(
                        Constants.LEFT_SHOULDER,
                        Constants.LEFT_ELBOW,
                        Constants.LEFT_WRIST
                ),
                "RIGHT_ELBOW" to listOf(
                        Constants.RIGHT_SHOULDER,
                        Constants.RIGHT_ELBOW,
                        Constants.RIGHT_WRIST
                ),
                "LEFT_HIP" to listOf(
                        Constants.LEFT_SHOULDER,
                        Constants.LEFT_HIP,
                        Constants.LEFT_KNEE
                ),
                "RIGHT_HIP" to listOf(
                        Constants.RIGHT_SHOULDER,
                        Constants.RIGHT_HIP,
                        Constants.RIGHT_KNEE
                ),
                "LEFT_KNEE" to listOf(
                        Constants.LEFT_HIP,
                        Constants.LEFT_KNEE,
                        Constants.LEFT_ANKLE
                ),
                "RIGHT_KNEE" to listOf(
                        Constants.RIGHT_HIP,
                        Constants.RIGHT_KNEE,
                        Constants.RIGHT_ANKLE
                ),
                "LEFT_ANKLE" to listOf(
                        Constants.LEFT_KNEE,
                        Constants.LEFT_ANKLE,
                        Constants.LEFT_FOOT_INDEX
                ),
                "RIGHT_ANKLE" to listOf(
                        Constants.RIGHT_KNEE,
                        Constants.RIGHT_ANKLE,
                        Constants.RIGHT_FOOT_INDEX
                ),
        )

        val CHILDS_ANGLE = mapOf(
                "LEFT_ELBOW" to listOf(
                        Constants.LEFT_SHOULDER,
                        Constants.LEFT_ELBOW,
                        Constants.LEFT_WRIST
                ),
                "RIGHT_ELBOW" to listOf(
                        Constants.RIGHT_SHOULDER,
                        Constants.RIGHT_ELBOW,
                        Constants.RIGHT_WRIST
                ),
                "LEFT_SHOULDER" to listOf(
                        Constants.LEFT_ELBOW,
                        Constants.LEFT_SHOULDER,
                        Constants.LEFT_HIP
                ),
                "RIGHT_SHOULDER" to listOf(
                        Constants.RIGHT_ELBOW,
                        Constants.RIGHT_SHOULDER,
                        Constants.RIGHT_HIP
                ),

                "LEFT_HIP" to listOf(
                        Constants.LEFT_SHOULDER,
                        Constants.LEFT_HIP,
                        Constants.LEFT_KNEE
                ),
                "RIGHT_HIP" to listOf(
                        Constants.RIGHT_SHOULDER,
                        Constants.RIGHT_HIP,
                        Constants.RIGHT_KNEE
                ),
                "LEFT_KNEE" to listOf(
                        Constants.LEFT_HIP,
                        Constants.LEFT_KNEE,
                        Constants.LEFT_ANKLE
                ),
                "RIGHT_KNEE" to listOf(
                        Constants.RIGHT_HIP,
                        Constants.RIGHT_KNEE,
                        Constants.RIGHT_ANKLE
                ),
        )
        val DOWNWARDDOG_ANGLE = mapOf(
                "LEFT_ELBOW" to listOf(
                        Constants.LEFT_SHOULDER,
                        Constants.LEFT_ELBOW,
                        Constants.LEFT_WRIST
                ),
                "RIGHT_ELBOW" to listOf(
                        Constants.RIGHT_SHOULDER,
                        Constants.RIGHT_ELBOW,
                        Constants.RIGHT_WRIST
                ),
                "LEFT_SHOULDER" to listOf(
                        Constants.LEFT_ELBOW,
                        Constants.LEFT_SHOULDER,
                        Constants.LEFT_HIP
                ),
                "RIGHT_SHOULDER" to listOf(
                        Constants.RIGHT_ELBOW,
                        Constants.RIGHT_SHOULDER,
                        Constants.RIGHT_HIP
                ),

                "LEFT_HIP" to listOf(
                        Constants.LEFT_SHOULDER,
                        Constants.LEFT_HIP,
                        Constants.LEFT_KNEE
                ),
                "RIGHT_HIP" to listOf(
                        Constants.RIGHT_SHOULDER,
                        Constants.RIGHT_HIP,
                        Constants.RIGHT_KNEE
                ),
                "LEFT_KNEE" to listOf(
                        Constants.LEFT_HIP,
                        Constants.LEFT_KNEE,
                        Constants.LEFT_ANKLE
                ),
                "RIGHT_KNEE" to listOf(
                        Constants.RIGHT_HIP,
                        Constants.RIGHT_KNEE,
                        Constants.RIGHT_ANKLE
                ),

                "LEFT_ANKLE" to listOf(
                        Constants.LEFT_KNEE,
                        Constants.LEFT_ANKLE,
                        Constants.LEFT_HEEL
                ),
                "RIGHT_ANKLE" to listOf(
                        Constants.RIGHT_KNEE,
                        Constants.RIGHT_ANKLE,
                        Constants.RIGHT_HEEL
                ),
        )
        val LOWLUNGE_ANGLE = mapOf(
                "LEFT_ELBOW" to listOf(
                        Constants.LEFT_SHOULDER,
                        Constants.LEFT_ELBOW,
                        Constants.LEFT_WRIST
                ),
                "RIGHT_ELBOW" to listOf(
                        Constants.RIGHT_SHOULDER,
                        Constants.RIGHT_ELBOW,
                        Constants.RIGHT_WRIST
                ),
                "LEFT_SHOULDER" to listOf(
                        Constants.LEFT_ELBOW,
                        Constants.LEFT_SHOULDER,
                        Constants.LEFT_HIP
                ),
                "RIGHT_SHOULDER" to listOf(
                        Constants.RIGHT_ELBOW,
                        Constants.RIGHT_SHOULDER,
                        Constants.RIGHT_HIP
                ),

                "LEFT_HIP" to listOf(
                        Constants.LEFT_SHOULDER,
                        Constants.LEFT_HIP,
                        Constants.LEFT_KNEE
                ),
                "RIGHT_HIP" to listOf(
                        Constants.RIGHT_SHOULDER,
                        Constants.RIGHT_HIP,
                        Constants.RIGHT_KNEE
                ),
                "LEFT_KNEE" to listOf(
                        Constants.LEFT_HIP,
                        Constants.LEFT_KNEE,
                        Constants.LEFT_ANKLE
                ),
                "RIGHT_KNEE" to listOf(
                        Constants.RIGHT_HIP,
                        Constants.RIGHT_KNEE,
                        Constants.RIGHT_ANKLE
                ),
        )
        val SEATEDFORWARDBEND_ANGLE = mapOf(
                "LEFT_SHOULDER" to listOf(
                        Constants.LEFT_ELBOW,
                        Constants.LEFT_SHOULDER,
                        Constants.LEFT_HIP
                ),
                "RIGHT_SHOULDER" to listOf(
                        Constants.RIGHT_ELBOW,
                        Constants.RIGHT_SHOULDER,
                        Constants.RIGHT_HIP
                ),
                "LEFT_HIP" to listOf(
                        Constants.LEFT_SHOULDER,
                        Constants.LEFT_HIP,
                        Constants.LEFT_KNEE
                ),
                "RIGHT_HIP" to listOf(
                        Constants.RIGHT_SHOULDER,
                        Constants.RIGHT_HIP,
                        Constants.RIGHT_KNEE
                ),
                "LEFT_KNEE" to listOf(
                        Constants.LEFT_HIP,
                        Constants.LEFT_KNEE,
                        Constants.LEFT_ANKLE
                ),
                "RIGHT_KNEE" to listOf(
                        Constants.RIGHT_HIP,
                        Constants.RIGHT_KNEE,
                        Constants.RIGHT_ANKLE
                ),
                "LEFT_ANKLE" to listOf(
                        Constants.LEFT_KNEE,
                        Constants.LEFT_ANKLE,
                        Constants.LEFT_FOOT_INDEX
                ),
                "RIGHT_ANKLE" to listOf(
                        Constants.RIGHT_KNEE,
                        Constants.RIGHT_ANKLE,
                        Constants.RIGHT_FOOT_INDEX
                ),
        )
        val BRIDGE_ANGLE = mapOf(
                "LEFT_ELBOW" to listOf(
                        Constants.LEFT_SHOULDER,
                        Constants.LEFT_ELBOW,
                        Constants.LEFT_WRIST
                ),
                "RIGHT_ELBOW" to listOf(
                        Constants.RIGHT_SHOULDER,
                        Constants.RIGHT_ELBOW,
                        Constants.RIGHT_WRIST
                ),
                "LEFT_SHOULDER" to listOf(
                        Constants.LEFT_ELBOW,
                        Constants.LEFT_SHOULDER,
                        Constants.LEFT_HIP
                ),
                "RIGHT_SHOULDER" to listOf(
                        Constants.RIGHT_ELBOW,
                        Constants.RIGHT_SHOULDER,
                        Constants.RIGHT_HIP
                ),
                "LEFT_HIP" to listOf(
                        Constants.LEFT_SHOULDER,
                        Constants.LEFT_HIP,
                        Constants.LEFT_KNEE
                ),
                "RIGHT_HIP" to listOf(
                        Constants.RIGHT_SHOULDER,
                        Constants.RIGHT_HIP,
                        Constants.RIGHT_KNEE
                ),
                "LEFT_KNEE" to listOf(
                        Constants.LEFT_HIP,
                        Constants.LEFT_KNEE,
                        Constants.LEFT_ANKLE
                ),
                "RIGHT_KNEE" to listOf(
                        Constants.RIGHT_HIP,
                        Constants.RIGHT_KNEE,
                        Constants.RIGHT_ANKLE
                ),
                "LEFT_ANKLE" to listOf(
                        Constants.LEFT_KNEE,
                        Constants.LEFT_ANKLE,
                        Constants.LEFT_FOOT_INDEX
                ),
                "RIGHT_ANKLE" to listOf(
                        Constants.RIGHT_KNEE,
                        Constants.RIGHT_ANKLE,
                        Constants.RIGHT_FOOT_INDEX
                ),
        )
        val PYRAMID_ANGLE = mapOf(
                "LEFT_ELBOW" to listOf(
                        Constants.LEFT_SHOULDER,
                        Constants.LEFT_ELBOW,
                        Constants.LEFT_WRIST
                ),
                "RIGHT_ELBOW" to listOf(
                        Constants.RIGHT_SHOULDER,
                        Constants.RIGHT_ELBOW,
                        Constants.RIGHT_WRIST
                ),
                "LEFT_SHOULDER" to listOf(
                        Constants.LEFT_ELBOW,
                        Constants.LEFT_SHOULDER,
                        Constants.LEFT_HIP
                ),
                "RIGHT_SHOULDER" to listOf(
                        Constants.RIGHT_ELBOW,
                        Constants.RIGHT_SHOULDER,
                        Constants.RIGHT_HIP
                ),
                "LEFT_HIP" to listOf(
                        Constants.LEFT_SHOULDER,
                        Constants.LEFT_HIP,
                        Constants.LEFT_KNEE
                ),
                "RIGHT_HIP" to listOf(
                        Constants.RIGHT_SHOULDER,
                        Constants.RIGHT_HIP,
                        Constants.RIGHT_KNEE
                ),
                "LEFT_KNEE" to listOf(
                        Constants.LEFT_HIP,
                        Constants.LEFT_KNEE,
                        Constants.LEFT_ANKLE
                ),
                "RIGHT_KNEE" to listOf(
                        Constants.RIGHT_HIP,
                        Constants.RIGHT_KNEE,
                        Constants.RIGHT_ANKLE
                ),
                "LEFT_ANKLE" to listOf(
                        Constants.LEFT_KNEE,
                        Constants.LEFT_ANKLE,
                        Constants.LEFT_FOOT_INDEX
                ),
                "RIGHT_ANKLE" to listOf(
                        Constants.RIGHT_KNEE,
                        Constants.RIGHT_ANKLE,
                        Constants.RIGHT_FOOT_INDEX
                ),
                "LEG_ANKLE" to listOf(Constants.RIGHT_KNEE, Constants.LEFT_HIP, Constants.LEFT_KNEE)
        )

    }
}