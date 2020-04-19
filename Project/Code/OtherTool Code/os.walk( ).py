import os
path = "F:\my dataset"
'''
walk的逻辑是这样的，该函数会遍历所有的子目录，并以文件夹为单位返回一个tuple。
root表示当前目录， 
dirs返回一个list，表示该文件夹下的所有文件夹 
files返回一个list，表示该文件夹下的所有非文件夹的文件

因此root+files中的任何一个元素，都组成了当前文件下的一个非文件夹的文件的绝对目录
'''
for root, dirs, files in os.walk(path):
    # print(root)
    # print(dirs)
    # print((files))
    for name in files: # 当前文件夹为files，拿出一个文件为name
        if name.startswith("._"):
            os.remove(os.path.join(root, name)) # 放在一起，就是当前被拿出来的文件的绝对目录

print("finished")