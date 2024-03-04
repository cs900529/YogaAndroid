import numpy as np


# !!!!!!! 請注意以下的 TODO
class FeetData:
    def __init__(self, left_foot=None, right_foot=None):
        self.left_foot = left_foot
        self.right_foot = right_foot

    # 設定資料
    def set_point(self, left_foot, right_foot):
        # TODO : 由於目前 輸入的 MediaPipe 座標左右相反， 先階段決定先維持現狀，因此在 FeetData 中將左右腳設定相反
        self.left_foot = right_foot
        self.right_foot = left_foot

    # 取得在瑜珈墊腳的個數
    def get_feet_count_on_mat(self):
        count = sum(1 for foot in [self.left_foot, self.right_foot] if all(0 <= value <= 1 for value in foot))
        return count

    # 取得靠近重心的腳
    def get_closer_foot_to_center(self, center):
        if np.size(center) == 0 or self.left_foot is None or self.right_foot is None:
            return "None"

        # 回傳是左腳還是右腳比較靠近重心座標
        left_foot_distance = sum(abs(foot - center_coord) for foot, center_coord in zip(self.left_foot, center))
        right_foot_distance = sum(abs(foot - center_coord) for foot, center_coord in zip(self.right_foot, center))


        if right_foot_distance < left_foot_distance:
            return "RIGHT_HEEL"
        elif left_foot_distance < right_foot_distance:
            return "LEFT_HEEL"
        else:
            return "Equal"

    def to_dict(self):
        # 將 FeetData 物件轉換為字典
        dict = {"left_foot": self.left_foot, "right_foot": self.right_foot}
        return dict

    @classmethod
    def from_dict(cls, data_dict):
        left_foot = data_dict.get("left_foot", None)
        right_foot = data_dict.get("right_foot", None)
        return cls(left_foot, right_foot)

    def __str__(self):
        return f"left_foot = {self.left_foot}, right_foot = {self.right_foot}"
