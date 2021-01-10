ReadMe

Google cloud directory include all the code running on the google service.
Rasp Pi directory include the code running in the raspberry pi.
Traning CNN directory include the code about how to train the CNN by google cola
GUI directory include the code about GUI

You can use PyCharm to open all the python code and Java code, but if you wanna run, you need to set up more.


Google cloud and rasp pi code are built by Python 3.7, in addition, you need to install Tensorflow 2.0 edition, google cloud package, google-cloud-storage and pyzbar package by pip3.  Environment is very important in this project. Incompatible versions and missing installation packages can cause errors in system operation.

We use google colab to train CNN. You can use Jupiter notes or google colab to run it. It should be noted that the data required for training is stored in google drive, so you need to first place the training set in the corresponding location of the cloud disk, and then run the program.

The code involves some local data processing and model loading, so if you want to run, you need to change the address to your local address in the corresponding place.


In terms of GUI part
The basic operating environment is JAVA 8.0 and Python 3.7. 
Two other necessary visualization packages are required, named jcommon-1.0.23 and jfreechart-1.0.19.
The secret key file that connects to the cloud must be referenced correctly.

The entry of the GUI is Main.

The code file is packaged as an EXE file that can be executed with the full environment. But the place where the reference location is designed in the code needs to be slightly modified.


Finally, We strongly recommend that you use PyCharm or Jupyter notes to run the python code, and use IntelliJ IDEA to run Java code, use google colab to run ipynb file.


