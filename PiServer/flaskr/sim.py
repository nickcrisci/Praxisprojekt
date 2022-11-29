from flask import (
    Blueprint, request, make_response, jsonify, render_template, g
)

import json

from flaskr.db import get_db

from .hardware import led, lcd

from . import logging

# initially set lcd to white background
lcd.setRGB(255, 255, 255)

bp = Blueprint('sim', __name__, url_prefix='/sim')

@bp.route("/led", methods=["POST"])
def changeLedMode():
    content_type = request.headers.get('Content-Type')
    if (content_type == 'application/json'):
        json = request.get_json()
        logging.create_log("Info", "LED Post", f"Received a POST request to change the LED mode to {json['mode']}")
        return jsonify(led.changeLedMode(json["mode"]))
    else:
        logging.create_log("Error", "LED Post", f"Received a POST request to change the LED mode, but the Content-Type was not application/json")
        return { "error": "Content-Type not supported" }

@bp.route("/lcd", methods=["POST"])
def changeLcdText():
    content_type = request.headers.get('Content-Type')
    if (content_type == 'application/json'):
        json = request.get_json()
        logging.create_log("Info", "LCD Post", f"Received a POST request to change the LCD text to {json['text']}")
        return jsonify(lcd.setText(json["text"]))
    else:
        logging.create_log("Error", "LCD Post", f"Received a POST request to change the LCD text, but the Content-Type was not application/json")
        return { "error": "Content-Type not supported" }