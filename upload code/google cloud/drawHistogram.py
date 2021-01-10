import cv2 as cv
from matplotlib import pyplot as plt

img=cv.imread('F:\capstone project\data\hh_chicken_wings2.jpg')
plt.hist(img.ravel(), 256)
plt.show()