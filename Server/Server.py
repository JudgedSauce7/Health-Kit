from flask import Flask
import model_predict

#from OpenSSL import SSL
#context = SSL.Context(SSL.TLSv1_2_METHOD)
#context.use_privatekey_file('myprivatekey.key')
#context.use_certificate_file('mycert.crt')

app = Flask(__name__)

@app.route('/id/<name>')
    
def default(name):
    word = ''
    input1=[]
    for char in name:
        word = word + char
        if(char == '+'):
            word = word[:-1]
            input1.append(word)
            word = ''

    output=model_predict.Predict(input1)
    return output

if __name__ == "__main__":
    app.run(host="192.168.43.205", port = 5000, debug =True)