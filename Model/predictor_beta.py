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

'''
df = pd.read_csv("Training.csv")

print(df.dtypes)

df['prognosis'] = pd.Categorical(df['prognosis'])
df['prognosis'] = df.prognosis.cat.codes

print(df['prognosis'])

prognosis = df.pop('prognosis')
dataset = tf.data.Dataset.from_tensor_slices((df.values, prognosis.values))

#for symptom, prognosis in dataset.take(5):
#  print ('Symptom: {}, Prognosis: {}'.format(symptom, prognosis))

train_dataset = dataset.shuffle(len(df)).batch(1)

#print(len(Counter(prognosis).keys()))

def get_compiled_model():
  model = tf.keras.Sequential([
    tf.keras.layers.Dense(120, activation='relu'),
    tf.keras.layers.Dense(80, activation='relu'),
    tf.keras.layers.Dense(60, activation='relu'),
    tf.keras.layers.Dense(41, activation='softmax')
  ])
  model.compile(optimizer='adam',
                loss='sparse_categorical_crossentropy',
                metrics=['accuracy'])
  return model
  
model = get_compiled_model()
model.fit(train_dataset, epochs=5)
print(model.summary())

model.save('my_model.h5')
'''
new_model = tf.keras.models.load_model('my_model.h5')
new_model.summary()
'''
#Converter
converter = tf.lite.TFLiteConverter.from_keras_model(model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
tflite_model = converter.convert()
open("converted_model.tflite", "wb").write(tflite_model)

df2 = pd.read_csv("Testing.csv")

print(df2.dtypes)

df2['prognosis'] = pd.Categorical(df2['prognosis'])
df2['prognosis'] = df2.prognosis.cat.codes

print(df2['prognosis'])

prognosis2 = df2.pop('prognosis')
dataset2 = tf.data.Dataset.from_tensor_slices((df2.values, prognosis2.values))

train_dataset2 = dataset2.shuffle(len(df2)).batch(1)

test_loss, test_acc = new_model.evaluate(train_dataset2)

print('\nTest accuracy:', test_acc)
'''
'''
#Input 1
df3 = pd.read_csv("Testingcopy.csv")

df3 = df3[df3['prognosis']=="disease"]
print(df3)

df3.pop('prognosis')
print(df3.values)
dataset3 = tf.data.Dataset.from_tensor_slices((df3.values))

'''
#Input 2
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

while(y!="Predict"):
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

np.savetxt('newfile.csv', [n_symp], delimiter=',', fmt='%s')

with open('newfile.csv','a') as fd:
    writer = csv.writer(fd)
    writer.writerow(symp)

df3 = pd.read_csv("newfile.csv")
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
