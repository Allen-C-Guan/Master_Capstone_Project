##保存
ipTable=['123.111.111.1','111.111.111.1']
fileObject = open('sampleList.txt', 'w')

fileObject.write("$".join(ipTable))  # 用$隔开
fileObject.close()
# 读取
f = open("sampleList.txt","r")   #设置文件对象
table = f.read()     #将txt文件的所有内容读入到字符串str中
list = table.split("$")  # 还原为list
f.close()   #将文件关闭