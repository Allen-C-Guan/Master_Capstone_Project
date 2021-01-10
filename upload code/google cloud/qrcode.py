# 导入所需工具包
from pyzbar import pyzbar
from PIL import Image


class QRcode:
    @classmethod   # 变成一个类方法
    def decode(cls, address:str) -> int:   # cls表示的是类，例如使用QRcode.decode(str)  此时cls == QRcode
        # 加载输入图像
        image = Image.open(address)

        # 找到图像中的条形码并进行解码
        barcodes = pyzbar.decode(image)

        # 循环检测到的条形码
        for barcode in barcodes:
            # 条形码数据为字节对象，所以如果我们想在输出图像上画出来，就需要先将它转换成字符串
            barcodeData = barcode.data.decode("utf-8")
            # 绘出图像上条形码的数据和条形码类型
            text = "{} ".format(barcodeData)
            content = text

        return content