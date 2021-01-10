from PredictionAndStatistic import analysis
import os
from download import Download
from Saving_csv import SavingCsv

rasp_img_save_path = '/home/yingchenguan/project/rasp picture'
bucket_name = "rasp-pi-bucket"
#download
download = Download()
download.download_dir(bucket_name, rasp_img_save_path)
model_path = "/home/yingchenguan/project/trained_model/MobileNet Model_my dataset_epochs = 3.h5"
img_dir = rasp_img_save_path
#prediction
anaslysis_result = analysis.MoblieNetAnalysis(model_path, img_dir)
print(anaslysis_result)

#saving data
save_path = "/home/yingchenguan/project/csv_result/"
SavingCsv.saveAsCsv(anaslysis_result, save_path)