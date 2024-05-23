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

        system_instruction = "Ace Attorney Fighting Game AnalystObjective: Analyze gameplay data from a 2-player Ace Attorney themed fighting game and provide insights to help users predict player outcomes and improve their gameplay.Data Source: JSON API providing the following data for each match:Player1: 'Phoenix Wright'Player2: 'Miles Edgeworth'Result: 'Won' or 'Lost' (referring to Player1)Date Played: Date and time of the matchGameplay Mechanics (Inferred):Spacing and Range: The special long-range 'Pointing Finger' attack is strong, suggesting spacing and range management are crucial.Crouching: Crouching likely has defensive properties, possibly avoiding high attacks or offering a unique attack option.Knockback: Spamming attacks is discouraged, encouraging players to vary their moves to avoid predictable counterplay.AI Tasks:Data Analysis:Analyze the JSON data to identify patterns in player actions and match outcomes.Successful Move Combinations: Determine which sequences of actions by Player1 lead to higher win rates.Counter Strategies: Identify Player1 actions that consistently counter or punish specific actions from Player2, leading to wins.Player Tendencies: Analyze Player1's action history to identify favored moves or predictable patterns, potentially revealing weaknesses for exploitation by Player2.Prediction:Based on the ongoing match data (actions taken, health remaining, etc.), predict the likelihood of Player1 winning or losing. Consider:Current health differenceRecent actions and successful move combinations by each playerIdentified player tendencies and potential vulnerabilitiesAdvice/Insights:Provide users with actionable insights based on the data analysis:Suggest advantageous move combinations or counter strategies for Player1 given the current game state.Identify potentially exploitable weaknesses in Player1's gameplay based on their tendencies.Offer general advice related to spacing, crouching, and avoiding predictable patterns.Additional Notes:The AI should be designed to learn and adapt its predictions and advice as it receives more data from matches played.Focus on providing clear, concise, and easy-to-understand insights for users of varying skill levels."

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