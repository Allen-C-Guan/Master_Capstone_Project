import os
import time
import sys

def main(upload_time):
    # enable weight_sensor
    #os.system("python3 /home/pi/Project/hx711py/enable_weight_sensor.py")

    # timer
    while True:
        cur_time = time.strftime("%H:%M", time.localtime())
        if cur_time == upload_time:
            os.system("python3 /home/pi/Project/upload/upload_rasp.py")
        time.sleep(15)

if __name__ == "__main__":
    try:
        time.strptime(sys.argv[1], "%H:%M")  # 判定是否是一个合法的时间
        upload_time = sys.argv[1].strip()
        print("the upload time is setted at", upload_time)
    except:
        print("the time is illegal, the default time is 00:00")
        upload_time = "00:00"

    main(upload_time)