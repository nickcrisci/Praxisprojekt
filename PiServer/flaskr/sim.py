from flask import (
    Blueprint, request, make_response, jsonify, render_template, g
)

import json

from flaskr.db import get_db

from .hardware import led

bp = Blueprint('sim', __name__, url_prefix='/sim')

@bp.route("/led", methods=["POST"])
def changeLedMode():
    content_type = request.headers.get('Content-Type')
    if (content_type == 'application/json'):
        json = request.get_json()
        return jsonify(led.changeLedMode(json["mode"]))
    else:
        return { "error": "Content-Type not supported" }