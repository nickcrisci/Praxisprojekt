import os

from flask import Flask

def create_app(test_config=None):
    app = Flask(__name__, instance_relative_config=True)
    app.config.from_mapping(
        SECRET_KEY='dev',
        DATABASE=os.path.join(app.instance_path, 'flaskr.sqlite'),
    )

    if test_config is None:
        app.config.from_pyfile('config.py', silent=True)
    else:
        app.config.from_mapping(test_config)

    try:
        os.makedirs(app.instance_path)
    except OSError:
        pass

    @app.route('/hello')
    def hello():
        return 'Hello, World!'

    # Import Database
    from . import db
    db.init_app(app)

    # Import Log as Blueprint
    from . import log
    app.register_blueprint(log.bp)

    # Import sim as Blueprint
    # Only import when running on a Raspberry Pi (since GrovePi can't be installed via pip anymore)
    try:
        from . import sim
        app.register_blueprint(sim.bp)
    except ModuleNotFoundError:
        pass

    return app