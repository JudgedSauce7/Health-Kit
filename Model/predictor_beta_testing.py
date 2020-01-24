import pandas as pd
import numpy as np
import csv
'''
#Quantization
import tensorflow as tf

converter = tf.lite.TFLiteConverter.from_saved_model(saved_model_dir)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
tflite_quant_model = converter.convert()
open("converted_model.tflite", "wb").write(tflite_quantized_model)

'''

with open("Testing.csv", "r") as new:
    reader = csv.reader(new)
    symptoms = next(reader)

print(symptoms)

symptoms = symptoms[:len(symptoms)-1]
print(symptoms)

symptoms_enter = []
symp=[]

for i in range (len(symptoms)):
    symp.append(0)

y="Enter Symptoms : \n"
print(y)

while(y!="Done"):
    y=input()
    symptoms_enter.append(y)

for i in range(len(symptoms_enter)):
    for k in range(len(symptoms)):
        if(symptoms_enter[i]==symptoms[k]):
            symp[k]=1

print(symp)
