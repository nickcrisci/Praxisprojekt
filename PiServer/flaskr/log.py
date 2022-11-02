from flask import (
    Blueprint, request, make_response, jsonify, render_template, g
)

import json

from flaskr.db import get_db

bp = Blueprint('log', __name__, url_prefix='/log')

# Helper Function to turn a sqlite row into a python dict
def dict_from_row(row):
    return dict(zip(row.keys(), row)) 

def get_row(id):
    db = get_db()
    cur = db.execute(
        'SELECT * FROM log WHERE id = ?', [id]
    ).fetchone()

    return dict_from_row(cur)

@bp.route("/", methods=['GET'])
def get_logs():
    db = get_db()
    cur = db.execute(
        'SELECT * FROM log'
    ).fetchall()
    g.logs = [dict_from_row(log) for log in cur]
    return render_template('log/logs.html')

@bp.route('/', methods=['POST'])
def postLog():
    content_type = request.headers.get('Content-Type')

    if (content_type != 'application/json'):
        return 'Content-Type not supported!'

    db = get_db()
    json = request.json

    cursor = db.execute(
        "INSERT INTO log (category, title, body) VALUES (?, ?, ?)",
        (json["category"], json["title"], json["body"])
    )

    db.commit()
    responseBody = {
        "success_message": f'Logged an Info Log with the title: {json["title"]}',
        "entity": get_row(cursor.lastrowid)
    }
    response = make_response(jsonify(responseBody), 201)
    response.headers['Location'] = f'http://localhost:5000/log/info/{cursor.lastrowid}'

    
    return response

@bp.route("/info/<id>", methods=["GET"])
def get_info(id):
    log_event = get_row(id)
    return log_event