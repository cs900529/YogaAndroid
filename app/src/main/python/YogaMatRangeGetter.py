import cv2
import numpy as np

# 偵測場景中的瑜珈墊，製作轉換矩陣
class YogaMatRangeGetter:
    def __init__(self):
        # 瑜珈墊顏色區間
        self.lower_green = np.array([30, 20, 20])
        self.upper_green = np.array([70, 180, 180])

    # 偵測場景中的瑜珈墊，製作轉換矩陣
    def generate_transform_matrix(self, image):
        hsv = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)
        mask = cv2.inRange(hsv, self.lower_green, self.upper_green)

        contours, _ = cv2.findContours(mask, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
        max_contour = max(contours, key=cv2.contourArea)
        hull = cv2.convexHull(max_contour)
        approx = cv2.approxPolyDP(hull, 0.05 * cv2.arcLength(hull, True), True)

        original_points = np.array([approx.reshape(-1, 2)], dtype=np.float32)

        if len(original_points[0]) == 4:
            self.transform_matrix = cv2.getPerspectiveTransform(original_points, self.camera_flat_points)

        # 框出來綠色區塊, 根據綠色區塊製作凸集
        return max_contour, approx
