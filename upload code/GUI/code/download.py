# -*- coding: iso-8859-15 -*-

from google.cloud import storage
import os

class Download:
    def download_single_file(self, bucket_name, source_blob_name, destination_file_name):

        ################          这个地址你要改成你自己存放密匙的地址    ###################
        storage_client = storage.Client.from_service_account_json(
            "D:\WasteXero2.0\My First Project-7b0d2811365a.json")
        ################################################################################

        bucket = storage_client.bucket(bucket_name)
        blob = bucket.blob(source_blob_name)   # cloud的位置目录
        blob.download_to_filename(destination_file_name)
        print("File {} downloaded to {}.".format(source_blob_name, destination_file_name))

    def download_dir(self, bucket_name, destination_dir_name):
        # download name_list.txt at first
        name_list = "csv_file/name_list.txt"
        self.download_single_file(bucket_name, name_list, destination_dir_name + name_list)

        # get list of file name
        f = open(destination_dir_name + "/" + name_list, "r")  # 设置文件对象
        table = f.read()  # 将txt文件的所有内容读入到字符串str中
        name_list = table.split("$")  # 还原为list
        f.close()  # 将文件关闭

        # download all files
        for file_name in name_list:
            if file_name.endswith(".csv"):
                file_name = "csv_file/" + file_name
                self.download_single_file(bucket_name, file_name, destination_dir_name + file_name)

        # delete name list
        os.remove(destination_dir_name + "csv_file/name_list.txt")
        print("All file from this dir are downloaded")


if __name__ == "__main__":
    d = Download()
    # step 1 设置本地地址
    local_path = "D:/WasteXero2.0/"  #本地存储文件夹位置，地址一定以 "/" 作为末尾

    # create dir under this root
    root = local_path + "csv_file/"
    if not os.path.exists(root):
        os.makedirs(root)

    #download
    d.download_dir(bucket_name = "rasp-pi-bucket", destination_dir_name = local_path )