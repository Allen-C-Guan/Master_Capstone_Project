from google.cloud import storage
import os


class Upload:
    @classmethod
    def upload_single_file(cls, bucket_name, source_file_name, destination_blob_name):
        storage_client = storage.Client.from_service_account_json(
            "/home/yingchenguan/project/csv_result/My First Project-7b0d2811365a.json")
        bucket = storage_client.bucket(bucket_name)
        blob = bucket.blob(destination_blob_name)
        blob.upload_from_filename(source_file_name)
        print("File {} uploaded to {}.".format(source_file_name, destination_blob_name))

    @classmethod
    def upload_dir(cls, bucket_name, source_dir_name):
        name_list = []
        for root, dirs, files in os.walk(source_dir_name):  # this is dir name not file
            for name in files:  # 当前文件夹为files，拿出一个文件为name
                cls.upload_single_file(bucket_name, os.path.join(root, name), "csv_file/" + name)
                name_list.append(name)
                # 如果需要删除，下面这条保留就行了
                # os.remove(os.path.join(root, name))

        name_list_path = "name_list.txt"
        # upload file
        fileObject = open(source_dir_name + "/" + name_list_path, 'w')
        fileObject.write("$".join(name_list))  # 用$隔开
        fileObject.close()
        cls.upload_single_file(bucket_name, source_dir_name + "/" + name_list_path, "csv_file/" +name_list_path)
        # os.remove(source_dir_name + "/" + name_list_path)
        print("name_list.txt has been build and uploaded")
        print("All file from this dir are uploaded")

# local_dir_path = "/home/pi/camera capture"
# upload_dir("rasp-pi-bucket", local_dir_path)
