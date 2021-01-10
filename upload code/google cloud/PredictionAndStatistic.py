import sys

sys.path.append("/Users/allen/Code_git/Master_Capstone_Project/Project/Code/google cloud")
from qrcode import QRcode
from imageprocessing import ImageProcessing
from CNN import DataGenerator, MobileNet
import os

'''
Analysis
Analyze the downloaded data and return statistical data in the form of a dictionary, including
sales, total waste, and corresponding carbon dioxide emissions
'''

class analysis:
    @classmethod
    def MoblieNetAnalysis(cls, model_path:str, img_dir:str) -> dict:
        '''
        :param model_path: str
        :param img_dir: str
        :return: dict           dictionary with form [times, weight, co2]
        '''

        '''
        loading model
        '''
        model = MobileNet(model_path)
        # loop pick single picture up
        img_path = img_dir
        # 存储已经发出去的dishes QRcode:str  :  label:str
        exist_dishes = {}
        # waste food dictionary key = label:str  value = [cnt:int,weight:int]
        waste_dishes = {}
        for root, dirs, files_list in os.walk(img_path):
            files_list.sort()
            for file_name in files_list:  # file 是单个文件名：

                if file_name.endswith(".jpg") or file_name.endswith(".JPG"): # filter file only JPG get into
                    """
                    pick up a single file
                    """

                    file_path = os.path.join(root, file_name)  # 这个函数return的是一个string
                    # 获取QRcode，return的是个str
                    try:
                        # qrcode = 10
                        qrcode = QRcode.decode(file_path)   # qrcode 是str
                    except:
                        continue
                    cur_weight = int(file_name.split("%%")[-1].split(".")[0])
                    '''
                    qrcode is the first time or second
                    '''
                    # if this dish not in dict, get label and add {qrcode: label} in dict
                    if qrcode not in exist_dishes:
                        # predict
                        imagepro = ImageProcessing()
                        img = imagepro.resize(file_path)
                        label = list(model.predict(img).keys())[0]  # return the label dict {name:str : 1}, 取出name(str)
                        # add label in exist_dishes
                        exist_dishes[qrcode] = [label, cur_weight]

                    # if this dish in dict, get label, pop this qrcode, store data
                    else:
                        label, pre_weight = exist_dishes[qrcode]
                        cnt, hist_weight = waste_dishes.get(label, [0, 0])
                        waste_dishes[label] = [cnt + 1, hist_weight + min(pre_weight, cur_weight)]
                        # clear exist_dishes
                        exist_dishes.pop(qrcode)
        statistic_dict = {}

        for label, [cnt, weight] in waste_dishes.items():

            statistic_dict[label] = [cnt, weight,round(weight * DataGenerator.co2_dict.get(label, 5), 2)]
        return statistic_dict







