from FeetData import FeetData
import AngleNodeDef
import cv2
import numpy as np

from YogaMatRangeGetter import YogaMatRangeGetter


# 負責將人體骨架的腳座標點，轉換成瑜珈墊上面的點
# 轉至技術介紹: https://blog.csdn.net/guduruyu/article/details/72518340
class YogaMatProcessor:
    def __init__(self, use_default_matrix=True):
        # 鏡頭的座標位置
        self.camera_flat_points = np.array([[1, 1], [0, 1], [0, 0], [1, 0]], dtype=np.float32)
        # 預設瑜珈墊在鏡頭的座標
        default_mat_point = np.array([[0.9, 0.97],
                                      [0.1, 0.97],
                                      [0.15, 0.76],
                                      [0.85, 0.76]], dtype=np.float32)

        # 使用預設的轉至矩陣
        self.use_default_matrix = use_default_matrix
        # 轉換矩陣
        self.transform_matrix = cv2.getPerspectiveTransform(default_mat_point,
                                                            self.camera_flat_points)
        self.feet_data = FeetData()

    # 產生腳的資料
    def generate_feet_data(self, r_point2d, r_point3d):
        # 將骨架資料根據 python 格式進行轉換
        point2d, point3d = self.__handle_skeleton_point(r_point2d, r_point3d)

        # 檢查骨架是否為空
        if self.__contain_point(point2d, point3d):
            # 取得腳的 2d 座標
            feet_points = self.__get_feet_points(point2d)
            # 將腳的座標進行轉換
            transform_points = self.transform_point(feet_points)

            # 創建腳的資料
            left_feet, right_feet = transform_points[0], transform_points[1]
            self.feet_data.set_point(left_feet, right_feet)
        else:
            self.feet_data.set_point(None, None)

        return self.feet_data.to_dict()

    # 產稱瑜珈墊的轉至矩陣
    def generate_transform_matrix_with_image(self, image):
        range_getter = YogaMatRangeGetter()
        max_contour, approx, mat_points, contours, mask = range_getter.generate_mat_range(image, True)

        self.generate_transform_matrix(mat_points)

    # 產稱瑜珈墊的轉至矩陣
    def generate_transform_matrix(self, unit_points):
        is_4_point = len(unit_points[0]) == 4

        transform_matrix = cv2.getPerspectiveTransform(unit_points, self.camera_flat_points) if is_4_point else []

        if is_4_point:
            self.transform_matrix = transform_matrix

    # 將鏡頭中的座標點，轉換到瑜珈墊上面
    def transform_point(self, input_points):
        transformed_points = [
            cv2.perspectiveTransform(np.array([np.float32([p])]), self.transform_matrix)[0][0] for p
            in input_points]

        return transformed_points

    # 將骨架資料根據 python 格式進行轉換
    def __handle_skeleton_point(self, r_point2d, r_point3d):
        point3d = [[r_point3d.get(i).get(j) for j in range(3)] for i in range(r_point3d.size())]
        point2d = [[r_point2d.get(i).get(j) for j in range(2)] for i in range(r_point2d.size())]

        return point2d, point3d

    # 檢查骨架是否為空
    def __contain_point(self, point2d, point3d):
        return not isinstance(point2d, int) and not isinstance(point3d, int)

    # 從 MediaPipe 的點中，取得在鏡頭中腳的點
    def __get_feet_points(self, point2d):
        return [[point2d[AngleNodeDef.LEFT_HEEL][0], point2d[AngleNodeDef.LEFT_HEEL][1]],
                [point2d[AngleNodeDef.RIGHT_HEEL][0], point2d[AngleNodeDef.RIGHT_HEEL][1]]]

    # 取得腳的資料
    def get_left_foot_x(self):
        # 取得 left_foot 的 x 座標
        return self.feet_data.left_foot[0] if self.feet_data.left_foot is not None else - 999999

    def get_left_foot_y(self):
        # 取得 left_foot 的 y 座標
        return self.feet_data.left_foot[1] if self.feet_data.left_foot is not None else - 999999

    def get_right_foot_x(self):
        # 取得 right_foot 的 x 座標
        return self.feet_data.right_foot[0] if self.feet_data.right_foot is not None else - 999999

    def get_right_foot_y(self):
        # 取得 right_foot 的 y 座標
        return self.feet_data.right_foot[1] if self.feet_data.right_foot is not None else - 999999