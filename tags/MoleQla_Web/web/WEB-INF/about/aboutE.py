#!/usr/bin/python

import psycopg2
import base64
import sys

ruta = sys.argv[1]
print ruta
#ruta = "/home/rafa/Escritorio/fotos"

conn_string = "host='localhost' dbname='moleqla' user='openuser' password='openerp'"
conn = psycopg2.connect(conn_string)

cursor = conn.cursor()
cursor.execute("SELECT rp.id, rp.image FROM editor e INNER JOIN res_users ru ON e.user_id = ru.id LEFT JOIN res_partner rp ON ru.partner_id = rp.id")
editores = cursor.fetchall()

#editores[i][0] -> id
#editores[i][1] -> imagen

i=0
while (i < len(editores)):
	if editores[i][1]:
		open(ruta+'/'+str(editores[i][0])+'.jpg', 'wb').write(base64.decodestring(str(editores[i][1])))
	i = i + 1

cursor.close()
conn.close()
print "Fotos editores creadas"
