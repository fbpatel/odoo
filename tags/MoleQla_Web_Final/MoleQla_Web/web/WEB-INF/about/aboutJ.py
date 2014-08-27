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
cursor.execute("SELECT rp.id, rp.image FROM res_partner rp WHERE rp.id IN( SELECT ru.partner_id FROM res_users ru  WHERE ru.id IN ( SELECT rg.uid FROM res_groups_users_rel rg  WHERE rg.gid IN ( SELECT g.id FROM res_groups g WHERE g.name = 'Editor Jefe' )AND rg.uid <> 1))")
editoresJ = cursor.fetchall()

#editoresJ[i][0] -> id
#editoresJ[i][1] -> imagen

i=0
while (i < len(editoresJ)):
	if editoresJ[i][1]:
		open(ruta+'/'+str(editoresJ[i][0])+'.jpg', 'wb').write(base64.decodestring(str(editoresJ[i][1])))
	i = i + 1

cursor.close()
conn.close()
print "Fotos editores jefe creadas"
