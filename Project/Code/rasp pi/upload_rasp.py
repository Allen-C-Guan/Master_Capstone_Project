from google.cloud import storage
import os
import shutil
def upload_single_file(bucket_name, source_file_name, destination_blob_name):
    """Uploads a file to the bucket."""
    # bucket_name = "your-bucket-name"
    # source_file_name = "local/path/to/file"
    # destination_blob_name = "to_cloud_file_name"

    #如果是在云端 直接用client就行了
    
    storage_client = storage.Client.from_service_account_json("/home/pi/Project/upload/My First Project-7b0d2811365a.json")
    bucket = storage_client.bucket(bucket_name)
    blob = bucket.blob(destination_blob_name)
    blob.upload_from_filename(source_file_name)
    print("File {} uploaded to {}.".format(source_file_name, destination_blob_name))


def upload_dir(bucket_name, source_dir_name, hist_dir_path):
    name_list = []
    for root, dirs, files in os.walk(source_dir_name):# this is dir name not file
        for name in files: # 当前文件夹为files，拿出一个文件为name
            upload_single_file(bucket_name, os.path.join(root, name), name)
            name_list.append(name)
            shutil.move(os.path.join(root, name), hist_dir_path)


    # build file list
    name_list_path = "name_list.txt"
    #upload file
    fileObject = open(source_dir_name + "/" + name_list_path,  'w')
    fileObject.write("$".join(name_list))  # 用$隔开
    fileObject.close()
    upload_single_file(bucket_name, source_dir_name + "/" + name_list_path, name_list_path)
    shutil.move(source_dir_name + "/" + name_list_path, hist_dir_path)
    print("name_list.txt has been build and uploaded")
    print("All file from this dir are downloaded")





if __name__ == "__main__":
    local_dir_path = "/home/pi/camera capture"
    upload_dir("rasp-pi-bucket", local_dir_path, "/home/pi/history")





#upload_blob("rasp-pi-bucket", '/Users/allen/Desktop/Project/Code/afasd%%344.png', "MYY2.png", )