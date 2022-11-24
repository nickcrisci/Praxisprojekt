import socket
import flaskr.hardware.lcd as lcd

# A function to get the IP address of the Raspberry Pi
def get_ip_address():
    ip_address = ""
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    try:
        s.connect(('8.8.8.8', 80))
        ip_address = s.getsockname()[0]
        s.close()
    except:
        ip_address = "Error"
    finally:
        return ip_address

# Set Rgb to white
lcd.setRGB(255, 255, 255)
lcd.setText(get_ip_address())