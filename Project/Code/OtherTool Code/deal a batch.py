from Linux import Linux
from CNN import DataGenerator, MobileNet


'''
Download
'''
# Setting address
remote_path = '/home/pi/camera capture'     # ssh dir
local_path = "/Users/allen/Desktop/tensorflow/database/pictures"  # local dir
host = Linux('10.0.0.164', 'pi', '123456')  # IP

# Download
host.sftp_get_dir(remote_path, local_path)
# Remove
# host.sftp_remove_dir(remote_path)


'''
CNN 批量处理
'''
# Loading well trained CNN model
model = MobileNet("/Users/allen/Desktop/Project/trained_model/MobileNet Model_my dataset_epochs = 3.h5")
# datagenerator
generator = DataGenerator("/Users/allen/Desktop/Project/Pictures/")
# 预测
print(model.predict(generator.data))
