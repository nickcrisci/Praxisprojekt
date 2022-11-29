from flask import (
    Blueprint, request, make_response, jsonify, render_template, g
)

import json

from flaskr.db import get_db

from . import logging

bp = Blueprint('log', __name__, url_prefix='/log')

@bp.route("/", methods=['GET'])
def get_logs():
    db = get_db()
    cur = db.execute(
        'SELECT * FROM log'
    ).fetchall()
    g.logs = [logging.dict_from_row(log) for log in cur]
    return render_template('log/logs.html')

@bp.route('/', methods=['POST'])
def postLog():
    content_type = request.headers.get('Content-Type')

    if (content_type != 'application/json'):
        return 'Content-Type not supported!'
    
    json = request.json

    logged = logging.create_log(json["category"], json["title"], json["body"])

    response = make_response(jsonify(logged["body"]), 201)
    response.headers['Location'] = f'http://localhost:5000/log/info/{logged["id"]}'

    return response

@bp.route("/info/<id>", methods=["GET"])
def get_info(id):
    log_event = logging.get_row(id)
    return log_event