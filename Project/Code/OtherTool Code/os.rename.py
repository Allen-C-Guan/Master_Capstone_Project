import os
folder_path = "/Volumes/Allen SANSUMG Drive/project/dataset/fruits-360/Training/Apple Red 2/"
file_list = os.listdir(folder_path)
# 切换到当前文件夹路径下
os.chdir(folder_path)
for old_name in file_list:
    new_name = "ggg" + old_name
    os.rename(old_name, new_name)
print("finishied")