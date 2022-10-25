from flask import (
    Blueprint, request, make_response, jsonify
)

import json

from flaskr.db import get_db

bp = Blueprint('log', __name__, url_prefix='/log')

# Helper Function to turn a sqlite row into a python dict
def dict_from_row(row):
    return dict(zip(row.keys(), row)) 

def get_row(id):
    db = get_db()
    print(f"############# {id} ############")
    cur = db.execute(
        'SELECT * FROM log WHERE id = ?', [id]
    ).fetchone()

    return dict_from_row(cur)

@bp.route('/info/', methods=['POST'])
def info():
    content_type = request.headers.get('Content-Type')

    if (content_type != 'application/json'):
        return 'Content-Type not supported!'

    db = get_db()
    json = request.json

    cursor = db.execute(
        "INSERT INTO log (category, title, body) VALUES ('Info', ?, ?)",
        (json["title"], json["body"])
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