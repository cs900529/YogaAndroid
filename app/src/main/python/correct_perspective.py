import numpy as np
import cv2
""" 
WARNING!!!!

NOT USAGE NOW
NOT USAGE NOW
NOT USAGE NOW
NOT USAGE NOW
NOT USAGE NOW
NOT USAGE NOW
NOT USAGE NOW
NOT USAGE NOW
NOT USAGE NOW
NOT USAGE NOW

"""
def np_from_buffer(buffer):
    nparr = np.frombuffer(buffer, np.uint8)
    img_np = cv2.imdecode(nparr, cv2.IMREAD_COLOR)
    return img_np

def correct_horizontal_perspective(image_array, angle):
    
    
    h, w = image_array.shape[:2]
    
    # 定義原始圖像的四個頂點
    src_points = np.float32([
        [0, 0],
        [w, 0],
        [0, h],
        [w, h]
    ])
    
    # 計算左右校正的目標點
    shift_y = w * np.tan(np.deg2rad(angle))  # 計算垂直平移量

    if(shift_y > 0): # 校正角度為正
        dst_points = np.float32([
            [0, 0],                             # 左上角
            [w + shift_y * 0.5, -shift_y],      # 右上角
            [0, h],                             # 左下角
            [w + shift_y * 0.5, h + shift_y]    # 右下角
        ])
    elif(shift_y < 0): # 校正角度為負
        dst_points = np.float32([
        [shift_y * 0.5, shift_y],               # 左上角
        [w, 0],                                 # 右上角
        [shift_y * 0.5, h - shift_y],           # 左下角
        [w, h]                                  # 右下角
    ])

    ''' 負角度 default
    dst_points = np.float32([
        [0, shift_y],       # 左上角
        [w, 0],             # 右上角
        [0, h - shift_y],   # 左下角
        [w, h]              # 右下角
    ])
    '''
    
    # 計算透視變換矩陣
    transformation_matrix = cv2.getPerspectiveTransform(src_points, dst_points)
    
    # 進行透視校正
    corrected_image = cv2.warpPerspective(image_array, transformation_matrix, (w, h))
    # 将图像转换为字节数组
    _, buffer = cv2.imencode('.png', corrected_image)
    byte_array = buffer.tobytes()
    return byte_array
    