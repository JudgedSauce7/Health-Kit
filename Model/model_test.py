#Model Testing

import tensorflow as tf
import pandas as pd
from tensorflow import keras
from collections import Counter
import numpy as np
import csv

new_model = tf.keras.models.load_model('my_model.h5')
new_model.summary()

df2 = pd.read_csv("Testing.csv")

print(df2.dtypes)

df2['prognosis'] = pd.Categorical(df2['prognosis'])
df2['prognosis'] = df2.prognosis.cat.codes

print(df2['prognosis'])

prognosis2 = df2.pop('prognosis')
dataset2 = tf.data.Dataset.from_tensor_slices((df2.values, prognosis2.values))

train_dataset2 = dataset2.shuffle(len(df2)).batch(1)

test_loss, test_acc = new_model.evaluate(train_dataset2)

print('\nTest Accuracy :', test_acc)
print('\nTest Error : ', test_loss)
