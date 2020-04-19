#! /usr/bin/python2

import time
import sys

EMULATE_HX711 = False
referenceUnit = 100
weight_threshold = 100
clear_treshhold = 20
save_folder = '/home/pi/camera capture/'



import picamera
if not EMULATE_HX711:
    import RPi.GPIO as GPIO
    from hx711 import HX711
else:
    from emulated_hx711 import HX711


def cleanAndExit():
    if not EMULATE_HX711:
        GPIO.cleanup()
    sys.exit()

def readWeight():
        val = hx.get_weight(5)
        print(val) #just for testing
        hx.power_down()
        hx.power_up()
        return max(0, val)

def takePicture(val):
    camera = picamera.PiCamera()

    camera.resolution = (2592, 1944)
    camera.framerate = 15
    # camera.rotation=180
    camera.start_preview()
    sleep(2)
    times = time.strftime("%Y_%m_%d-%H_%M_%S", time.localtime())
    camera.capture(save_folder + str(times) + "%%" + str(val) + 'g.jpg')
    # camera.stop_preview()
    camera.close()

def clearningWait():
    while True:
        if readWeight() < clear_treshhold:
            return


##   main ##
# initial
hx = HX711(5, 6)
hx.set_reading_format("MSB", "MSB")
hx.set_reference_unit(referenceUnit)
hx.reset()
hx.tare()


# infinite loop

while True:
    try:
        trigger = readWeight()
        if trigger > weight_threshold:
            time.sleep(0.7)  #waiting until senser stable
            val = readWeight() # get weight
            takePicture(val) #take pictures
            #if loading space is clean, then continune,to prevent taking picture during cleaning
            clearningWait()

        time.sleep(0.2)

    except (KeyboardInterrupt, SystemExit):
        cleanAndExit()
