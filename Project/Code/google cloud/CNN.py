import tensorflow as tf
import tensorflow_hub as hub
from tensorflow.keras.preprocessing.image import ImageDataGenerator
import numpy as np

'''
this file include 2 part
1. build data generator (iterater), including decode name map
2. reload well trained MobileNet with prediction function,
    predict function return dictionary with form {label:str : cnt: int }
    
'''
class DataGenerator:
    # 这是类变量，我们认为mapping是相同的可以互相使用
    class_names_arr = np.array(
        ['Apple', 'Eggplant', 'Lemon', 'Mango', 'Orange', 'Peach', 'Pear', 'Pepper Green', 'Strawberry', 'Tomato',
         'Apple_Pie', 'Chicken_Wings', 'Chocolate_Cake', 'Dumplings', 'Fish_And_Chips', 'French_Fries', 'Hamburger',
         'Hot_Dog', 'Ice_Cream', 'Pancakes', 'Pizza', 'Steak', 'Sushi', 'Waffles'])
    name_decode_map = {0: 'Apple',1: 'Eggplant',2: 'Lemon',3: 'Mango',4: 'Orange',5: 'Peach',6: 'Pear',7: 'Pepper Green',8: 'Strawberry',9: 'Tomato',10: 'Apple_Pie',11: 'Chicken_Wings',12: 'Chocolate_Cake',13: 'Dumplings',14: 'Fish_And_Chips',15: 'French_Fries',16: 'Hamburger',17: 'Hot_Dog',18: 'Ice_Cream',19: 'Pancakes',20: 'Pizza',21: 'Steak',22: 'Sushi',23: 'Waffles'}
    # co2_dict ,key:label , value: 1kg product with x kg co2
    co2_dict ={'Steak': 27, 'Apple':1.1, "Lemon": 1.1, "Mango":1.1, "Orange":1.1, "Peach":1.1, "Pear":1.1, "Pepper Green":2,"Eggplant":2,
               'Strawberry':1.1,'Tomato':2.9, 'Chicken_Wings':6.9, 'Chocolate_Cake':4.9, 'Pizza':2.07, 'Hamburger':5,  'French_Fries':5.7,
               'Hot_Dog':6.38}

    # instance var
    def __init__(self, data_path):
        '''
        :param data_path: str
        '''
        self.data = ImageDataGenerator(rescale=1 / 255).flow_from_directory(
            data_path, target_size=(224, 224), batch_size=1, shuffle="false")


class MobileNet:
    def __init__(self, path_5h):
        '''
        :param path:str
        '''
        self.path = path_5h
        self.model = tf.keras.models.load_model(path_5h, custom_objects={'KerasLayer':hub.KerasLayer})


    def predict(self, data):
        '''

        :param data: data generator(iterator) or np.array
        :return: cnt_dic: dict with form  {label:str : cnt: int }
        '''
        predicted_result = self.model.predict(data)  # 预测，
        predicted_index_labels = np.argmax(predicted_result, axis=-1)  # 取最大值，得到index_label
        cnt_dic = {}
        # 统计数据
        for index_label in predicted_index_labels:
            cnt_dic[DataGenerator.name_decode_map[index_label]] = cnt_dic.get(DataGenerator.name_decode_map[index_label], 0) + 1
        return cnt_dic




