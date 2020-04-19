# Improting Image class from PIL module
from PIL import Image
import numpy
'''
in this class, we resize the data from larger size to (224,224) and change it to np.array with shape [1, 224, 224, 3]
'''
class ImageProcessing:
    def resize(self, address:str):
        # Opens a image in RGB mode
        im = Image.open(address)
        # im = Image.open(r'F:\UoM\semester 4\capstone\PycharmProjects\image processing\test.jpg')
        # im.show()

        # Size of the image in pixels (size of orginal image)
        # (This is not mandatory)
        width, height = im.size
        # print(width, height)

        # 建立一个坐标系，图片在第四象限，图片的左顶点在原点（0，0）
        if width > height:
            left = (width - height) // 2
            top = 0
            right = (width + height) // 2
            bottom = height
            im_cut = im.crop((left, top, right, bottom))
        elif height > width:
            left = 0
            top = (height - width) // 2
            right = width
            bottom = (height + width) // 2
            im_cut = im.crop((left, top, right, bottom))
        else:
            im_cut = im
        newsize = (224, 224)
        im_resize = im_cut.resize(newsize)
        return numpy.array([numpy.array(im_resize)])/255