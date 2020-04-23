import sys
sys.path.append("/Users/allen/Code_git/Master_Capstone_Project/Project/Code/google cloud")

from PredictionAndStatistic import analysis

#
# '''
# ###########    Download   #############
#
# in this part, the data could download from rasp pi in to PC automatically and clear the directory of rasp pi
# '''
# # Setting address
# remote_path = '/home/pi/camera capture'     # ssh dir
# local_path = "/Users/allen/Desktop/Project/rasp img"  # local dir
# host = Linux('10.0.0.164', 'pi', '123456')  # IP
# # Download
# host.sftp_get_dir(remote_path, local_path)
# print("Download Finish")
# # Remove
# # host.sftp_remove_dir(remote_path)
# #
'''
##########    Analysis    ###########

Analyze the downloaded data and return statistical data in the form of a dictionary,
with form [name, sales, weight, co2 emission]
'''
# setting model saving path and img directory
model_path = "/Users/allen/Code_git/Master_Capstone_Project/Project/trained_model/MobileNet Model_my dataset_epochs = 3.h5"
img_dir = "/Users/allen/Desktop/test_image"
# analysis and return statistic data by dictionary, dictionary with form [times, weight, co2]
anaslysis_result = analysis.MoblieNetAnalysis(model_path, img_dir)



print(anaslysis_result)