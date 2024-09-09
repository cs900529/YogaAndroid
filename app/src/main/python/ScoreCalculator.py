
from AngleCalculator import calculate_angle, calculate_angle_in_andriodStudio
from yogaFileGetter import get_sample_score_angle_dict, get_std_angle_dict, get_weight_angle_dict


class ScoreCalculator:
    def __init__(self, pose_name):
        self.pose_name = pose_name

        self.average_angle_dict = get_sample_score_angle_dict(pose_name)
        self.std_angle_dict = get_std_angle_dict(pose_name)
        self.weight = get_weight_angle_dict(pose_name)

        self.score_dict = {}

    def calculate_score(self, point3d, is_android_studio):
        if is_android_studio:
            input_angle = calculate_angle_in_andriodStudio(self.pose_name, point3d)
        else:
            input_angle = calculate_angle(self.pose_name, point3d)

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

        # 計算總和
        sum_score = 0
        max_score = 0
        for joint, score in self.score_dict.items():
            weight = self.weight.get(joint, None)

            sum_score += weight * score
            max_score += weight

        # 計算平均值
        average_value = sum_score / max_score

        return int(average_value)