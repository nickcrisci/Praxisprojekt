import time
from grovepi import *

led = 4

pinMode(led, "OUTPUT")

def changeLedMode(mode):
    if mode == "Off":
        ledMode = False
    elif mode == "On":
        ledMode = True
    else:
        return { "success": False }

    try:
        digitalWrite(led, ledMode)
    except IOError:
        return { "success": False }
    
    return { "success": True }