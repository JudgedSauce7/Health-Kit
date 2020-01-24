#Model Prediction

import tensorflow as tf
import pandas as pd
from tensorflow import keras
from collections import Counter
import numpy as np
import csv

#print(tf.__version__)
#if tf.config.experimental.list_physical_devices("GPU"):
#    print("Present")
labels=['Fungal infection','Allergy','GERD','Chronic cholestasis','Drug Reaction',
'Peptic ulcer diseae','AIDS','Diabetes','Gastroenteritis','Bronchial Asthma','Hypertension',
'Migraine','Cervical spondylosis',
'Paralysis (brain hemorrhage)','Jaundice','Malaria','Chicken pox','Dengue','Typhoid','hepatitis A',
'Hepatitis B','Hepatitis C','Hepatitis D','Hepatitis E','Alcoholic hepatitis','Tuberculosis',
'Common Cold','Pneumonia','Dimorphic hemmorhoids(piles)',
'Heartattack','Varicoseveins','Hypothyroidism','Hyperthyroidism','Hypoglycemia','Osteoarthristis',
'Arthritis','(vertigo) Paroymsal  Positional Vertigo','Acne','Urinary tract infection','Psoriasis',
'Impetigo']
labels.sort()

new_model = tf.keras.models.load_model('my_model.h5')
new_model.summary()

with open("Testing.csv", "r") as new:
    reader = csv.reader(new)
    symptoms = next(reader)

symptoms = symptoms[:len(symptoms)-1]
print(symptoms)

symptoms_enter = []
symp=[]

for i in range (len(symptoms)):
    symp.append(0)

y="Enter Symptoms : "
print(y)

while(y!="predict"):
    y=input()
    symptoms_enter.append(y)

for i in range(len(symptoms_enter)):
    for k in range(len(symptoms)):
        if(symptoms_enter[i]==symptoms[k]):
            symp[k]=1

#print(symp)

s2 = pd.DataFrame(symp)

newarr = np.asarray(symp)
n_symp = np.asarray(symptoms)

np.savetxt('Predicting.csv', [n_symp], delimiter=',', fmt='%s')

with open('Predicting.csv','a') as fd:
    writer = csv.writer(fd)
    writer.writerow(symp)

df3 = pd.read_csv("Predicting.csv")
dataset3 = tf.data.Dataset.from_tensor_slices((df3.values))

#Prediction
print("You Have :")
train_dataset3 = dataset3.batch(1)
#train_dataset3 = dataset3
prediction = new_model.predict(train_dataset3)
#print(prediction[0])
#print(np.argmax(prediction[0]))
out=np.argmax(prediction[0])
print(labels[out])
