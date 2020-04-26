# Health-Kit

The aim is to develop a one-stop solution to all the medical needs of a person. Be it predicting what disease/condition they may have based on the symptoms.
A remider feature for ongoing medications, and also a scanner(barcode/QR) for easy logging of the medications and their information retrieval.

For the disease prediction module, the dataset has been retrieved from <b>Kaggle</b>. It includes a series of diseases along with their symptoms in the form of a sparse matrix. The data has been split and used to train the ANN model built using <b>Tensoflow</b> and the <b>Keras</b> API. The trained model is hosted on a personal server that the Android application connects to. The symptoms are entered on the user side and a request is sent on the server, which in turn predicts the diease and returns back the output to the application.

A bar code scanner has also been implemented to help the user scan for medicines and retrieve relevant information about the same. It has been implemented using <b>Firebase ML Kit</b> Android module.
