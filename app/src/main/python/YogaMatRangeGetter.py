import cv2
import numpy as np


# 偵測場景中的瑜珈墊座標
class YogaMatRangeGetter:
    def __init__(self):
        # 瑜珈墊顏色區間，為 hsv 座標
        self.lower_green = np.array([30, 10, 10])
        self.upper_green = np.array([100, 180, 180])
        # 預定轉換的攝影機座標
        self.camera_flat_points = np.array([[1, 1], [0, 1], [0, 0], [1, 0]], dtype=np.float32)

    # 設定顏色遮罩
    def set_mask(self, lower, upper):
        self.lower_green = lower
        self.upper_green = upper

    # 偵測場景中的瑜珈墊，回傳四邊形座標
    def generate_mat_range(self, image, return_unit=False):
        # 框出瑜珈墊
        hsv = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)
        mask = cv2.inRange(hsv, self.lower_green, self.upper_green)

        contours, _ = cv2.findContours(mask, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
        max_contour = max(contours, key=cv2.contourArea)
        hull = cv2.convexHull(max_contour)
        approx = cv2.approxPolyDP(hull, 0.05 * cv2.arcLength(hull, True), True)

        mat_points = np.array([approx.reshape(-1, 2)], dtype=np.float32)

        # 將瑜珈墊座標調整順序，確保為 [1, 1], [0, 1], [0, 0], [1, 0] 的順序
        mat_points = self.__adjust_points_order(mat_points)

        # 將座標點傳換成 0 ~ 1 之間座標
        if return_unit:
            height = len(image)
            width = len(image[0])
            mat_points = inverse_transform_coordinates(mat_points, width, height)

        # 回傳所需資訊
        return max_contour, approx, mat_points, contours, mask

    # 將瑜珈墊座標調整順序，確保為 [1, 1], [0, 1], [0, 0], [1, 0] 的順序
    def __adjust_points_order(self, mat_points):
        # 根據 x 座標大小排序
        sorted_unit_point = mat_points[:, np.argsort(mat_points[0, :, 0])]
        new_indices = [3, 0, 1, 2]
        sorted_unit_point = sorted_unit_point[:, new_indices]

        return sorted_unit_point


def inverse_transform_coordinates(coordinates, width, height):
    """
    將輸入的座標除以 height 和 width，返回反向轉換後的座標。

    Args:
        coordinates (numpy.ndarray): 二維座標陣列，每個元素都是一個座標點的陣列，如[[x1, y1], [x2, y2], ...]
        height (float): 高度
        width (float): 寬度

    Returns:
        numpy.ndarray: 反向轉換後的座標陣列，格式與輸入相同
    """
    """
    將輸入的座標除以 height 和 width，返回反向轉換後的座標，並四捨五入到小數第二位。

    Args:
        coordinates (numpy.ndarray): 二維座標陣列，每個元素都是一個座標點的列表，如[[x1, y1], [x2, y2], ...]
        height (float): 高度
        width (float): 寬度

    Returns:
        numpy.ndarray: 反向轉換後的座標陣列，格式與輸入相同
    """
    transformed_coordinates = coordinates / np.array([width, height], dtype=np.float32)
    return np.round(transformed_coordinates, decimals=2)