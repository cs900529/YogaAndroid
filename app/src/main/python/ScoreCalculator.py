import toolkit
from pathlib import Path
from AngleCalculator import calculate_angle, calculate_angle_in_andriodStudio

class ScoreCalculator:
    def __init__(self, pose_name):
        folder_path = Path(f"JsonFile")
        self.pose_name = pose_name

        average_path = folder_path / pose_name / "sample_score.json"
        std_path = folder_path / pose_name / "std_angle.json"
        weight_path = folder_path / pose_name / "weight.json"

        self.average_angle_dict = toolkit.readSampleJsonFile(average_path)
        self.std_angle_dict = toolkit.readSampleJsonFile(std_path)
        self.weight = toolkit.readSampleJsonFile(weight_path)

        self.score_dict = {}


    def calculate_score(self, point3d):
        input_angle = calculate_angle_in_andriodStudio(self.pose_name, point3d)
        # input_angle = calculate_angle(self.pose_name, point3d)

        self.score_dict = {}
        for joint, input_value in input_angle.items():
            average_value = self.average_angle_dict.get(joint, None)
            std_value = self.std_angle_dict.get(joint, None)

            if average_value is not None and std_value is not None:
                diff = abs(input_value - average_value)

                if diff <= 2 * std_value:
                    self.score_dict[joint] = 100
                elif diff <= 3 * std_value:
                    self.score_dict[joint] = 50
                else:
                    self.score_dict[joint] = 0

        # 使用字典生成式計算差異
        difference_dict = {joint: input_angle[joint] - self.average_angle_dict[joint] for joint in input_angle if
                           joint in self.average_angle_dict}
        difference_dict = {joint: round(value, 1) for joint, value in difference_dict.items()}

        # 打印結果
        print("偵測角度", input_angle)
        print("平均角度", self.average_angle_dict)
        print("關節差距", difference_dict)
        # 使用字典生成式將所有元素乘以 2
        full_score_range_dict = {joint: value * 2 for joint, value in self.std_angle_dict.items()}
        print("滿分區間", full_score_range_dict)
        print("關節得分", self.score_dict)

        # 計算總和
        sum_score = 0
        max_score = 0
        for joint, score in self.score_dict.items():
            weight = self.weight.get(joint, None)

            # print("w", weight, "s ", score)
            sum_score += weight * score
            max_score += weight

        # 計算平均值
        average_value = sum_score / max_score
        # print(average_value)

        return int(average_value)