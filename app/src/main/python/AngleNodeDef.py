'''
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
'''

NOSE = 0
LEFT_EYE = 2
RIGHT_EYE = 5
LEFT_SHOULDER = 11
RIGHT_SHOULDER = 12
LEFT_ELBOW = 13
RIGHT_ELBOW = 14
LEFT_WRIST = 15
RIGHT_WRIST = 16
LEFT_INDEX = 19
RIGHT_INDEX = 20
LEFT_HIP = 23
RIGHT_HIP = 24
LEFT_KNEE = 25
RIGHT_KNEE = 26
LEFT_ANKLE = 27
RIGHT_ANKLE = 28
LEFT_HEEL=29
RIGHT_HEEL=30
LEFT_FOOT_INDEX = 31
RIGHT_FOOT_INDEX = 32

WARRIOR_II_ANGLE = {
    "LEFT_SHOULDER": [LEFT_ELBOW, LEFT_SHOULDER, LEFT_HIP],
    "RIGHT_SHOULDER": [RIGHT_ELBOW, RIGHT_SHOULDER, RIGHT_HIP],
    "LEFT_ELBOW": [LEFT_SHOULDER, LEFT_ELBOW, LEFT_WRIST],
    "RIGHT_ELBOW": [RIGHT_SHOULDER, RIGHT_ELBOW, RIGHT_WRIST],
    "LEFT_HIP": [RIGHT_HIP, LEFT_HIP, LEFT_KNEE],
    "RIGHT_HIP": [LEFT_HIP, RIGHT_HIP, RIGHT_KNEE],
    "LEFT_KNEE": [LEFT_HIP, LEFT_KNEE, LEFT_ANKLE],
    "RIGHT_KNEE": [RIGHT_HIP, RIGHT_KNEE, RIGHT_ANKLE],
    "RIGHT_ANKLE": [RIGHT_KNEE, RIGHT_ANKLE, RIGHT_FOOT_INDEX],
}

TREE_ANGLE = {
    "LEFT_SHOULDER": [LEFT_ELBOW, LEFT_SHOULDER, LEFT_HIP],
    "RIGHT_SHOULDER": [RIGHT_ELBOW, RIGHT_SHOULDER, RIGHT_HIP],
    "LEFT_ELBOW": [LEFT_SHOULDER, LEFT_ELBOW, LEFT_WRIST],
    "RIGHT_ELBOW": [RIGHT_SHOULDER, RIGHT_ELBOW, RIGHT_WRIST],
    "LEFT_HIP": [RIGHT_HIP, LEFT_HIP, LEFT_KNEE],
    "RIGHT_HIP": [LEFT_HIP, RIGHT_HIP, RIGHT_KNEE],
    "LEFT_KNEE": [LEFT_HIP, LEFT_KNEE, LEFT_ANKLE],
    "RIGHT_KNEE": [RIGHT_HIP, RIGHT_KNEE, RIGHT_ANKLE],
}

REVERSE_PLANK_ANGLE = {
    "LEFT_SHOULDER": [LEFT_ELBOW, LEFT_SHOULDER, LEFT_HIP],
    "RIGHT_SHOULDER": [RIGHT_ELBOW, RIGHT_SHOULDER, RIGHT_HIP],
    "LEFT_ELBOW": [LEFT_SHOULDER, LEFT_ELBOW, LEFT_WRIST],
    "RIGHT_ELBOW": [RIGHT_SHOULDER, RIGHT_ELBOW, RIGHT_WRIST],
    "LEFT_HIP": [LEFT_SHOULDER, LEFT_HIP, LEFT_KNEE],
    "RIGHT_HIP": [RIGHT_SHOULDER, RIGHT_HIP, RIGHT_KNEE],
    "LEFT_KNEE": [LEFT_HIP, LEFT_KNEE, LEFT_ANKLE],
    "RIGHT_KNEE": [RIGHT_HIP, RIGHT_KNEE, RIGHT_ANKLE],
    "LEFT_WRIST": [LEFT_ELBOW, LEFT_WRIST, LEFT_INDEX],
    "RIGHT_WRIST": [RIGHT_ELBOW, RIGHT_WRIST, RIGHT_INDEX],
}

PLANK_ANGLE = {
    "LEFT_SHOULDER": [LEFT_ELBOW, LEFT_SHOULDER, LEFT_HIP],
    "RIGHT_SHOULDER": [RIGHT_ELBOW, RIGHT_SHOULDER, RIGHT_HIP],
    "LEFT_ELBOW": [LEFT_SHOULDER, LEFT_ELBOW, LEFT_WRIST],
    "RIGHT_ELBOW": [RIGHT_SHOULDER, RIGHT_ELBOW, RIGHT_WRIST],
    "LEFT_HIP": [LEFT_SHOULDER, LEFT_HIP, LEFT_KNEE],
    "RIGHT_HIP": [RIGHT_SHOULDER, RIGHT_HIP, RIGHT_KNEE],
    "LEFT_KNEE": [LEFT_HIP, LEFT_KNEE, LEFT_ANKLE],
    "RIGHT_KNEE": [RIGHT_HIP, RIGHT_KNEE, RIGHT_ANKLE],
    "LEFT_ANKLE": [LEFT_KNEE, LEFT_ANKLE, LEFT_FOOT_INDEX],
    "RIGHT_ANKLE": [RIGHT_KNEE, RIGHT_ANKLE, RIGHT_FOOT_INDEX],
}

CHILDS_ANGLE = {
    "LEFT_ELBOW": [LEFT_SHOULDER, LEFT_ELBOW, LEFT_WRIST],
    "RIGHT_ELBOW": [RIGHT_SHOULDER, RIGHT_ELBOW, RIGHT_WRIST],
    "LEFT_SHOULDER": [LEFT_ELBOW, LEFT_SHOULDER, LEFT_HIP],
    "RIGHT_SHOULDER": [RIGHT_ELBOW, RIGHT_SHOULDER, RIGHT_HIP],

    "LEFT_HIP": [LEFT_SHOULDER, LEFT_HIP, LEFT_KNEE],
    "RIGHT_HIP": [RIGHT_SHOULDER, RIGHT_HIP, RIGHT_KNEE],
    "LEFT_KNEE": [LEFT_HIP, LEFT_KNEE, LEFT_ANKLE],
    "RIGHT_KNEE": [RIGHT_HIP, RIGHT_KNEE, RIGHT_ANKLE],
}
DOWNWARDDOG_ANGLE = {
    "LEFT_ELBOW": [LEFT_SHOULDER, LEFT_ELBOW, LEFT_WRIST],
    "RIGHT_ELBOW": [RIGHT_SHOULDER, RIGHT_ELBOW, RIGHT_WRIST],
    "LEFT_SHOULDER": [LEFT_ELBOW, LEFT_SHOULDER, LEFT_HIP],
    "RIGHT_SHOULDER": [RIGHT_ELBOW, RIGHT_SHOULDER, RIGHT_HIP],

    "LEFT_HIP": [LEFT_SHOULDER, LEFT_HIP, LEFT_KNEE],
    "RIGHT_HIP": [RIGHT_SHOULDER, RIGHT_HIP, RIGHT_KNEE],
    "LEFT_KNEE": [LEFT_HIP, LEFT_KNEE, LEFT_ANKLE],
    "RIGHT_KNEE": [RIGHT_HIP, RIGHT_KNEE, RIGHT_ANKLE],

    "LEFT_ANKLE": [LEFT_KNEE, LEFT_ANKLE, LEFT_HEEL],
    "RIGHT_ANKLE": [RIGHT_KNEE, RIGHT_ANKLE, RIGHT_HEEL],
    "LEFT_WRIST": [LEFT_ELBOW, LEFT_WRIST, LEFT_INDEX],
    "RIGHT_WRIST": [RIGHT_ELBOW, RIGHT_WRIST, RIGHT_INDEX],
}
LOWLUNGE_ANGLE = {
    "LEFT_ELBOW": [LEFT_SHOULDER, LEFT_ELBOW,LEFT_WRIST],
    "RIGHT_ELBOW": [RIGHT_SHOULDER, RIGHT_ELBOW, RIGHT_WRIST],
    "LEFT_SHOULDER": [LEFT_ELBOW, LEFT_SHOULDER, LEFT_HIP],
    "RIGHT_SHOULDER": [RIGHT_ELBOW, RIGHT_SHOULDER, RIGHT_HIP],
    
    "LEFT_HIP": [LEFT_SHOULDER, LEFT_HIP, LEFT_KNEE],
    "RIGHT_HIP": [RIGHT_SHOULDER, RIGHT_HIP, RIGHT_KNEE],
    "LEFT_KNEE": [LEFT_HIP, LEFT_KNEE, LEFT_ANKLE],
    "RIGHT_KNEE": [RIGHT_HIP, RIGHT_KNEE, RIGHT_ANKLE],
}
SEATEDFORWARDBEND_ANGLE = {
    "LEFT_SHOULDER": [LEFT_ELBOW, LEFT_SHOULDER, LEFT_HIP],
    "RIGHT_SHOULDER": [RIGHT_ELBOW, RIGHT_SHOULDER, RIGHT_HIP],
    "LEFT_HIP": [LEFT_SHOULDER, LEFT_HIP, LEFT_KNEE],
    "RIGHT_HIP": [RIGHT_SHOULDER, RIGHT_HIP, RIGHT_KNEE],
    "LEFT_KNEE": [LEFT_HIP, LEFT_KNEE, LEFT_ANKLE],
    "RIGHT_KNEE": [RIGHT_HIP, RIGHT_KNEE, RIGHT_ANKLE],
    "LEFT_ANKLE": [LEFT_KNEE, LEFT_ANKLE, LEFT_FOOT_INDEX],
    "RIGHT_ANKLE": [RIGHT_KNEE, RIGHT_ANKLE, RIGHT_FOOT_INDEX],
}
BRIDGE_ANGLE = {
    "LEFT_ELBOW": [ LEFT_SHOULDER, LEFT_ELBOW, LEFT_WRIST],
    "RIGHT_ELBOW": [ RIGHT_SHOULDER, RIGHT_ELBOW, RIGHT_WRIST],
    "LEFT_SHOULDER": [LEFT_ELBOW, LEFT_SHOULDER, LEFT_HIP],
    "RIGHT_SHOULDER": [RIGHT_ELBOW, RIGHT_SHOULDER, RIGHT_HIP],
    "LEFT_HIP": [LEFT_SHOULDER, LEFT_HIP, LEFT_KNEE],
    "RIGHT_HIP": [RIGHT_SHOULDER, RIGHT_HIP, RIGHT_KNEE],
    "LEFT_KNEE": [LEFT_HIP, LEFT_KNEE, LEFT_ANKLE],
    "RIGHT_KNEE": [RIGHT_HIP, RIGHT_KNEE, RIGHT_ANKLE],
    "LEFT_ANKLE": [LEFT_KNEE, LEFT_ANKLE, LEFT_FOOT_INDEX],
    "RIGHT_ANKLE": [RIGHT_KNEE, RIGHT_ANKLE, RIGHT_FOOT_INDEX],
}
PYRAMID_ANGLE = {
    "LEFT_ELBOW": [ LEFT_SHOULDER, LEFT_ELBOW, LEFT_WRIST],
    "RIGHT_ELBOW": [ RIGHT_SHOULDER, RIGHT_ELBOW, RIGHT_WRIST],
    "LEFT_SHOULDER": [LEFT_ELBOW, LEFT_SHOULDER, LEFT_HIP],
    "RIGHT_SHOULDER": [RIGHT_ELBOW, RIGHT_SHOULDER, RIGHT_HIP],
    "LEFT_HIP": [LEFT_SHOULDER, LEFT_HIP, LEFT_KNEE],
    "RIGHT_HIP": [RIGHT_SHOULDER, RIGHT_HIP, RIGHT_KNEE],
    "LEFT_KNEE": [LEFT_HIP, LEFT_KNEE, LEFT_ANKLE],
    "RIGHT_KNEE": [RIGHT_HIP, RIGHT_KNEE, RIGHT_ANKLE],
    "LEFT_ANKLE": [LEFT_KNEE, LEFT_ANKLE, LEFT_FOOT_INDEX],
    "RIGHT_ANKLE": [RIGHT_KNEE, RIGHT_ANKLE, RIGHT_FOOT_INDEX],
    "LEG_ANKLE":[RIGHT_KNEE, LEFT_HIP, LEFT_KNEE]
}