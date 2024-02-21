import numpy as np
from mat_data import mat_point, mat_data       

def get_mat_data(rects, center):
    points = []
    for rect in rects:
        point = mat_point(rect[0], rect[1], rect[2], rect[3])
        if(not is_boundary_point(point)):
            points.append(point)

    mat = mat_data(points = points, center = center)

    return mat

def is_boundary_point(point):
        flag = False
        if(point.width >= 900):
            flag = True
        elif(point.height >= 600):
            flag = True
        return flag