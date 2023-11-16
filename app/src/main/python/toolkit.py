
import json
import math as m
import AngleNodeDef




def readSampleJsonFile(path):
    """read joint angle sample json file
    
    Args:
        path (str): json file path

    Returns:
        sample angle in json(dict) format
        (if process error return None)
    """
    try:
        with open(path, 'r') as file:
            sample_angle = json.load(file)
            return sample_angle
    except:
        return None

def writeSampleJsonFile(angle_array, angle_def, path):
    """write sample joint angle in json file
    
    Args:
        angle_array (numpy array): sample angle array
        angle_def (list): joint points defined by AngleNodeDef.py
        path (str): json file storage path

    Returns:
        No return
    """
    data = {}
    index = 0
    for key,_ in angle_def.items():
        data[key] = angle_array[index]
        index+=1
    print(data)
    with open(path, 'w') as file:
        json.dump(data, file, indent=4)

def computeAngle(point1, centerPoint, point2):
    """compute joint poins angle
        
    Args:
        point1 (list): joint points contains x,y,z
        centerPoint (list): joint points contains x,y,z
        point2 (list): joint points contains x,y,z
        
        centerPoint--->point1 = vector1
        centerPoint--->point2 = vector2
        use vector1 & vector2 compute angle
        
    Returns:
        degree (float)
    """
    p1_x, pc_x, p2_x = point1[0], centerPoint[0], point2[0]
    p1_y, pc_y, p2_y = point1[1], centerPoint[1], point2[1] 

    if len(point1) == len(centerPoint) == len(point2) == 3:
        # 3 dim
        p1_z, pc_z, p2_z = point1[2], centerPoint[2], point2[2]
    else:
        # 2 dim
        p1_z, pc_z, p2_z = 0,0,0

    # vector
    x1,y1,z1 = (p1_x-pc_x),(p1_y-pc_y),(p1_z-pc_z)
    x2,y2,z2 = (p2_x-pc_x),(p2_y-pc_y),(p2_z-pc_z)

    # angle
    cos_b = (x1*x2 + y1*y2 + z1*z2) / (m.sqrt(x1**2 + y1**2 + z1**2) *(m.sqrt(x2**2 + y2**2 + z2**2)))
    B = m.degrees(m.acos(cos_b))
    return B

def treePoseRule(roi, tips, sample_angle_dict, angle_dict, point3d, mat):
    """tree pose rule 
        
    Args:
        roi (list): region of interesting joint for tree pose
        tips (str): tips
        sample_angle_dict (dict): sample angle dict
        angle_dict (dict): angle dict
        point3d (mediapipe): mediapipe detect result
        
    Returns:
        roi (dict)
        tips (str)
    """
    
    for key, _ in roi.items():
        tip_flag = False
        if tips == "":
            tip_flag = True
        """if mat.point_count == 0:
            tips = "請將腳踩到瑜珈墊中" if tip_flag else tips
        elif mat.point_count >= 2:
            tips = "請將右腳抬起" if tip_flag else tips"""
        if key == 'LEFT_KNEE' or key == 'LEFT_HIP':
            tolerance_val = 8
            min_angle = sample_angle_dict[key]-tolerance_val
            max_angle = sample_angle_dict[key]+tolerance_val
            if angle_dict[key]>=min_angle and angle_dict[key]<=max_angle:
                roi[key] = True
            elif angle_dict[key]<min_angle:
                roi[key] = False
                tips = "將左腳打直平均分配雙腳重量，勿將右腳重量全放在左腳大腿" if tip_flag else tips
            else:
                roi[key] = False
                tips = "請勿將右腳重量全放在左腳大腿，避免傾斜造成左腳負擔" if tip_flag else tips
        elif key == 'RIGHT_FOOT_INDEX':
            _,foot_y,_ = point3d[AngleNodeDef.RIGHT_FOOT_INDEX]
            _,knee_y,_ = point3d[AngleNodeDef.LEFT_KNEE]
            if foot_y <= knee_y:
                roi[key] = True
            else:
                roi[key] = False
                if tip_flag == True:
                    tips = "請將右腳抬至高於左腳膝蓋的位置，勿將右腳放在左腳膝蓋上，\n避免造成膝蓋負擔"
        elif key == 'RIGHT_KNEE':
            _,_,knee_z = point3d[AngleNodeDef.RIGHT_KNEE]
            _,_,hip_z = point3d[AngleNodeDef.RIGHT_HIP]
            if angle_dict[key]<=65 and ((hip_z-knee_z)*100)<=17:
                roi[key] = True
            elif angle_dict[key]>65:
                roi[key] = False
                tips = "請將右腳再抬高一些，不可壓到左腳膝蓋" if tip_flag else tips
            elif ((hip_z-knee_z)*100)>17:
                roi[key] = False
                tips = "將臂部往前推，打開左右骨盆，右腳膝蓋不可向前傾" if tip_flag else tips
            else:
                roi[key] = False
                tips = "右腳膝蓋不可向前傾，須與髖關節保持同一平面" if tip_flag else tips
        elif key == 'RIGHT_HIP':
            if angle_dict[key]>=100:
                roi[key] = True
            else:
                roi[key] = False
                tips = "請確認右腳膝蓋是否已經抬至左腳膝蓋以上" if tip_flag else tips
        elif key == 'LEFT_SHOULDER' or key == 'RIGHT_SHOULDER':
            if angle_dict[key]>=120:
                roi[key] = True
            else:
                roi[key] = False
                tips = "請將雙手合掌並互相施力，往上伸展至頭頂正上方" if tip_flag else tips
        elif key == 'LEFT_ELBOW' or key == 'RIGHT_ELBOW':
            tolerance_val = 10
            min_angle = sample_angle_dict[key]-tolerance_val
            if angle_dict[key]>=min_angle:
                roi[key] = True
            else:
                roi[key] = False
                tips = "請將雙手再往上伸展，使手軸貼近耳朵" if tip_flag else tips
            # if angle_dict[key]>=90:
            #     roi[key] = True
            # else:
            #     roi[key] = False
            #     tips = "請將手再抬高一些，並保持在頭頂正上方" if tip_flag else tips
        elif key == 'LEFT_INDEX' or key == 'RIGHT_INDEX':
            index_x,_,_ = point3d[AngleNodeDef.LEFT_INDEX] if key == 'LEFT_INDEX' else point3d[AngleNodeDef.RIGHT_INDEX]
            left_shoulder_x,_,_ = point3d[AngleNodeDef.LEFT_SHOULDER]
            right_shoulder_x,_,_ = point3d[AngleNodeDef.RIGHT_SHOULDER]
            if index_x>=right_shoulder_x and index_x<=left_shoulder_x:
                roi[key] = True
            elif index_x<right_shoulder_x:
                roi[key] = False
                tips = "請將雙手往左移動，保持在頭頂正上方" if tip_flag else tips
            elif index_x>left_shoulder_x:
                roi[key] = False
                tips = "請將雙手往右移動，保持在頭頂正上方" if tip_flag else tips
    if tips == "":
        tips = "動作正確"
    return roi, tips
