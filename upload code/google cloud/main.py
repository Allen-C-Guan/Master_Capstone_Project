import os
import time
import sys

def main(setting_time):
    # timer
    while True:
        cur_time = time.strftime("%H:%M", time.localtime())

        print("cur_time: ", cur_time)
        print("setting time: ", setting_time)

        if cur_time == setting_time:
            os.system("/home/yingchenguan/project/code/start.py")
            print("have prcessed")

        time.sleep(15)
        print("waiting........")

if __name__ == "__main__":
    print("please enter a time for processing data (24 hour clock) ie. 13:50")
    setting_time = input()

    try:
        time.strptime(setting_time, "%H:%M")  # 判定是否是一个合法的时间
        upload_time = setting_time.strip()
        print("the processing time has setted at", upload_time)
    except:
        print("the time is illegal, the default time is 00:00")
        upload_time = "00:00"

    main(upload_time)