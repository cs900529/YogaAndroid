import toolkit
import yogaFileGetter

# 計算角度:給 python 用
def calculate_angle(pose, point3d):
    angle_def = yogaFileGetter.get_angle_def(pose)
    angle_dict = initialAngleDict(angle_def)

    for key, value in angle_def.items():
        angle = toolkit.computeAngle(list(toolkit.getLandmarks(point3d[value[0]])),
                                     list(toolkit.getLandmarks(point3d[value[1]])),
                                     list(toolkit.getLandmarks(point3d[value[2]])))
        angle_dict[key] = round(angle, 1)

    return angle_dict

# 計算角度:給 Andriod Studio 用
def calculate_angle_in_andriodStudio( pose, point):
    print("score, r_point3d", point)

    angle_def = yogaFileGetter.get_angle_def(pose)
    angle_dict = initialAngleDict(angle_def)

    point3d = []
    for i in range(point.size()):
        ang = []
        for j in range(4):
            ang.append(point.get(i).get(j))
        point3d.append(ang)

    print("score,point3d", point3d)

    for key,value in angle_def.items():
        if float(point3d[value[0]][3]) < toolkit.MIN_DETECT_VISIBILITY and float(point3d[value[1]][3]) <toolkit.MIN_DETECT_VISIBILITY and float(point3d[value[2]][3]) <toolkit.MIN_DETECT_VISIBILITY :
           angle_dict[key] = -1
        else:
            # if (type == 'Reverse Plank')  or (self.type == 'Plank') or (self.type == "Child's pose"):
            #     angle = toolkit.computeAngle(point3d[value[0]][:2], point3d[value[1]][:2], point3d[value[2]][:2])
            # else:
            angle = toolkit.computeAngle(point3d[value[0]], point3d[value[1]], point3d[value[2]])
            angle_dict[key] = angle

    print("angle_dict", angle_dict)
    return angle_dict

def initialAngleDict( angle_def):
    dict = {}
    index = 0
    for key, _ in angle_def.items():
        dict[key] = 0
        index += 1
    return dict