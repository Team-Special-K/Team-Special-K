import pymysql
import smtplib
import time
import re

DB_NAME = "FIRSTDB"
TABLE_NAME = "Orders"
EMAIL_ADDR = "msudenver.specialk@gmail.com"

def connect_db():
    """returns a connection to the database"""

    connect = False
    try: 
        connect = pymysql.connect(host = 'localhost', user = 'root', db = DB_NAME)
    except:  
        print("no connection")
    return connect


def get_rows(connect, key_header = False, key_wanted = False):
    """returns a tuple of database rows
        first row is column names"""

    assert(hasattr(connect, "cursor"))

    query = "SELECT * FROM %s" % (TABLE_NAME)
    if key_header and key_wanted: 
        query += " WHERE %s = '%s'" % (key_header, key_wanted)

    cursor = connect.cursor()
    cursor.execute(query)
    
    if not cursor.rowcount: return False

    assert(hasattr(cursor, "description"))
    col_names = tuple(i[0] for i in cursor.description)

    return tuple((col_names,) + cursor.fetchall())


def change_value(connect, row_id, key_header, new_value):
    """changes the value of an item to new_value"""

    assert(hasattr(connect, "cursor"))

    query = "UPDATE %s SET %s = %s WHERE id = %s;"%(TABLE_NAME, key_header, new_value, row_id)

    cursor = connect.cursor()
    rows = cursor.execute(query)
    connect.commit()

    return rows if rows else False
    

def connect_email(): 
    """returns a connection to the smtp server"""

    server = smtplib.SMTP_SSL(host = "smtp.gmail.com")

    for attempt in range(5):
        try:
            server.login(EMAIL_ADDR, "teamspecialk")
            return server
        except ValueError as e:
            
            print(e, " Bad Password")
    return False


def send_email(db, server, rows):
    """Sends emails from the server using tuple rows"""

    col_names = dict()
    for loc, key in enumerate(rows[0]):
        col_names[key] = loc

    for row in rows[1:]:
        
        confirmation = row[col_names["confirmation"]]
        if int(confirmation): continue
        
        email = row[col_names["email"]]
        prod_id = row[col_names["product_id"]]
        row_id = row[col_names["id"]]
        if not re.match("\w+@\w+\.\w+", email, re.ASCII):
            continue
        try:
            server.sendmail(EMAIL_ADDR, email,
                            "thank you for your order of %s" % (prod_id))
            changed = change_value(db, row_id, "confirmation", 1)
            
        except ValueError as e:
            print("Email error ", e)
            continue

        time.sleep(1)

    return

db = connect_db()
assert(db)

email_server = connect_email()
assert(email_server)

order_rows = get_rows(db, "confirmation", "0")

if order_rows: send_email(db, email_server, order_rows)
email_server.quit()
