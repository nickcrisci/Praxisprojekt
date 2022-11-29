from flaskr.db import get_db

def dict_from_row(row):
    return dict(zip(row.keys(), row)) 

def get_row(id):
    db = get_db()
    cur = db.execute(
        'SELECT * FROM log WHERE id = ?', [id]
    ).fetchone()

    return dict_from_row(cur)

def create_log(category: str, title: str, body: str):
    db = get_db()

    cursor = db.execute(
        "INSERT INTO log (category, title, body) VALUES (?, ?, ?)",
        (category, title, body)
    )

    db.commit()
    responseBody = {
        "success_message": f'Logged an Info Log with the title: {title}',
        "entity": get_row(cursor.lastrowid)
    }

    return {"body": responseBody, "id": cursor.lastrowid}