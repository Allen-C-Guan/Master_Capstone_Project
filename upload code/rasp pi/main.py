import os
import time
import sys


#change part
import threading
def enableWeightSensor():
    os.system("python3 /home/pi/Project/hx711py/enable_weight_sensor.py")

def main(upload_time):
    # enable weight_sensor

    ##change part
    thread = threading.Thread(target=enableWeightSensor)
    thread.start()
    ##change end

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