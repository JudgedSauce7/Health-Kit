#Model Training

import tensorflow as tf
import pandas as pd
from tensorflow import keras
from collections import Counter
import numpy as np
import csv

print(tf.__version__)
if tf.config.experimental.list_physical_devices("GPU"):
    print("Present")


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
model.fit(train_dataset, epochs=10)
print(model.summary())

model.save('my_model.h5')

#Converter
converter = tf.lite.TFLiteConverter.from_keras_model(model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
tflite_model = converter.convert()
open("converted_model.tflite", "wb").write(tflite_model)
