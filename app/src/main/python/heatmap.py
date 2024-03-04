import cv2
import numpy as np
import json

enlarge = 50
need_center = np.array([])
need_rects = np.array([])
flag = False
function = 0

def test(data):
    data = np.array(json.loads(data))

    print(data)
    print(data.shape)

def find_center(heatmap_arr):
    centers = []
    for (x, y), value in np.ndenumerate(heatmap_arr):
        if value > 100:
            centers.append([x,y,value])
    if centers:
        output_x=0
        output_y=0
        acum_w=0
        for (x,y,w) in centers:
            output_x += w*x
            output_y += w*y
            acum_w+=w
        return np.array([(output_x/acum_w*enlarge)+25,(output_y/acum_w*enlarge)+25]).astype(int)
    else:
        return np.array([])
    
def get_heatmap(data):
    global need_center, need_rects
    data = np.array(json.loads(data))
    
    rescaled_array = cv2.resize(data.astype('uint8'), dsize=(18 * enlarge , 12 * enlarge)) 
    rescaled_array = cv2.normalize(rescaled_array, None, 0, 255, norm_type= cv2.NORM_MINMAX, dtype= cv2.CV_8U)
    heatmap = cv2.applyColorMap(rescaled_array, cv2.COLORMAP_JET)
    center = find_center(data)
    rects = find_bounding_box(heatmap)
    need_center = center
    need_rects = rects
    #print(herotwo_pose_evaluate(center ,rects))
    if len(center)!=0 :
        cv2.circle(heatmap, (center[1], center[0]), 10, (255, 255, 255), 1)
    if  len(rects)> 1:
        for rect in rects:
            x , y , w , h = rect
            cv2.rectangle(heatmap, (x, y), (x + w, y + h), (36,255,12), 2)
    heatmap = cv2.rotate(heatmap, cv2.ROTATE_180)
    is_success, im_buf_arr = cv2.imencode(".png", heatmap)
    bytes_data = im_buf_arr.tobytes()
    return bytes_data

def find_bounding_box(heatmap):
    global flag, function
    flag = False
    function = 0
    # https://stackoverflow.com/questions/58419893/generating-bounding-boxes-from-heatmap-data
    # Grayscale then Otsu's threshold
    gray = cv2.cvtColor(heatmap, cv2.COLOR_BGR2GRAY)
    thresh = cv2.threshold(gray, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)[1]
    # Find contours
    kernel = np.ones((3,3), np.uint8)
    dilation = cv2.dilate(thresh, kernel, iterations = 1)
    cnts = cv2.findContours(dilation, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    cnts = cnts[0] if len(cnts) == 2 else cnts[1]
    rects = []
    for c in cnts:
        x,y,w,h = cv2.boundingRect(c)
        rects.append([x,y,w,h])

        if (x > 750 and y > 450):
            flag = True

        if (y <= 100):
            if (x >= 150 and x < 350):
                function = 1
            elif (x >= 350 and x < 550):
                function = 2
            elif (x >= 550 and x <= 750):
                function = 3
        elif (y >= 450):
            if(x >= 350 and x <= 550):
                function = 4

    big = [[0,0,0,0], [0,0,0,0]]
    for b in rects:
        b0 = big[0][2]*big[0][3]
        b1 = big[1][2]*big[1][3]
        b3 = b[2]*b[3]

        if (b3 >= b0 or b3 >= b1):
            if (b0 >= b1):
                big[1] = b
            else :
                big[0] = b

    # 提取座標
    point1 = np.array(big[0][:2])
    point2 = np.array(big[1][:2])
    #print(point1)
    #print(point2)
    # 计算两点之间的差异
    delta = point2 - point1

    # 计算夹角（弧度）
    angle_radians = np.arctan2(delta[1], delta[0])

    # 将弧度转换为度数
    angle_degrees = np.degrees(angle_radians)

    #print("与X轴的夹角（度数）：", angle_degrees)

    return np.array(rects)

'''def herotwo_pose_evaluate(center ,rects):
    if len(rects) == 2 and abs( rects[0][2] - rects[1][2])>50:       
        if rects[0][2] > rects[1][2]:
            front_foot = rects[0]
            rear_foot = rects[1]
        else:
            front_foot = rects[1]
            rear_foot = rects[0]
        front2center = abs(front_foot[0]+(front_foot[3]/2)-center[1]) 
        rear2center = abs(rear_foot[0]+(rear_foot[3]/2)-center[1])
        if front2center<rear2center and abs(front2center-rear2center)<100:
            return True
    return False'''

def get_rects():
    global need_rects
    return need_rects

def get_center():
    global need_center
    return need_center

def checkReturn():
    global flag
    return flag

def checkFunction():
    global function
    return function