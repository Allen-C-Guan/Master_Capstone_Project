import cv2

img = cv2.imread('F:\capstone project\data\chicken_wings2.jpg', 1)

imgYUV = cv2.cvtColor(img, cv2.COLOR_BGR2YCrCb)
cv2.imshow("src", img)

channelsYUV = cv2.split(imgYUV)
channelsYUV[0] = cv2.equalizeHist(channelsYUV[0])

channels = cv2.merge(channelsYUV)
result = cv2.cvtColor(channels, cv2.COLOR_YCrCb2BGR)


cv2.imwrite('F:\capstone project\data\hh_chicken_wings2.jpg',result)
cv2.imshow("dst", result)
cv2.waitKey(0)
