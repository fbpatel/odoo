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
cursor.execute("SELECT rp.id, rp.image FROM maquetador e INNER JOIN res_users ru ON e.user_id = ru.id LEFT JOIN res_partner rp ON ru.partner_id = rp.id")
maquetadores = cursor.fetchall()

#maquetadores[i][0] -> id
#maquetadores[i][1] -> imagen

i=0
while (i < len(maquetadores)):
	if maquetadores[i][1]:
		open(ruta+'/'+str(maquetadores[i][0])+'.jpg', 'wb').write(base64.decodestring(str(maquetadores[i][1])))
	i = i + 1

cursor.close()
conn.close()
print "Fotos maquetadores creadas"
