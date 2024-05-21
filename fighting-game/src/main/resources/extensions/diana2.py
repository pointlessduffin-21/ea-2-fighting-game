import openai
import mysql.connector
import json
import google.generativeai as genai
from flask import Flask, request, jsonify
from flask_cors import CORS
import requests

app = Flask(__name__)
CORS(app)

API_URL = 'http://localhost:6969/api/all'

# Gemini AI
def gemini(prompt, history_txt="chat_history.json"):
    try:
        genai.configure(api_key="AIzaSyDo8UiP5jbUVi_W2-zmKcMuD3FL3ArmQM4")

        generation_config = {
            "temperature": 1,
            "top_p": 0.95,
            "top_k": 64,
            "max_output_tokens": 8192,
        }

        safety_settings = [
            {
                "category": "HARM_CATEGORY_HARASSMENT",
                "threshold": "BLOCK_NONE"
            },
            {
                "category": "HARM_CATEGORY_HATE_SPEECH",
                "threshold": "BLOCK_NONE"
            },
            {
                "category": "HARM_CATEGORY_SEXUALLY_EXPLICIT",
                "threshold": "BLOCK_NONE"
            },
            {
                "category": "HARM_CATEGORY_DANGEROUS_CONTENT",
                "threshold": "BLOCK_NONE"
            },
        ]

        system_instruction = "You are Cubeworks AI designed to assist and interpret data from a database."

        model = genai.GenerativeModel(model_name="gemini-1.5-flash-latest",
                                      generation_config=generation_config,
                                      system_instruction=system_instruction,
                                      safety_settings=safety_settings)

        try:
            with open(history_txt, 'r') as f:
                history = json.load(f)
        except json.decoder.JSONDecodeError:
            print("JSON file is empty or not properly formatted, initializing an empty history.")
            history = []

        convo = model.start_chat(history=history)

        print("Executing Gemini with the prompt. Please wait! \n")

        convo.send_message(prompt)
        print(convo.last.text)

        with open(history_txt, "w") as f:
            user_message = {"role": "user", "parts": [prompt]}
            model_message = {"role": "model", "parts": [convo.last.text]}
            history.append(user_message)
            history.append(model_message)
            json.dump(history, f)

        return convo.last.text
    except Exception as e:
        print(f"Error executing Gemini: {e}")
        print("Retrying...")
        gemini(prompt, history_txt)

# API endpoints (same as before)
@app.route('/api/query', methods=['POST'])
def handle_query():
    data = request.get_json()
    query = data['query']

    # Fetch data from the API
    response = requests.get(API_URL)
    data = response.json()

    prompt = f"{query} Here's some potentially relevant information: {data}"

    response = gemini(prompt)

    return jsonify({'response': response})

if __name__ == '__main__':
    app.run(debug=True)