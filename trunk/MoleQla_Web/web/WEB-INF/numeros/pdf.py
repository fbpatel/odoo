#!/usr/bin/python

import psycopg2
import base64
import sys

ruta = sys.argv[1]
print ruta

conn_string = "host='localhost' dbname='moleqla' user='openuser' password='openerp'"
conn = psycopg2.connect(conn_string)

cursor = conn.cursor()
cursor.execute("SELECT archivo FROM articulo A WHERE (SELECT state FROM numero N WHERE N.id = A.numero_id) = 'a_publicar'")
archivos = cursor.fetchall()

cursorN = conn.cursor()
cursorN.execute("SELECT numero_id FROM articulo A WHERE (SELECT state FROM numero N WHERE N.id = A.numero_id) = 'a_publicar'")
numeros = cursorN.fetchall()

#Si no hay ningun numero nuevo se devulve false
if len(archivos) == 0:
    print "False"
else:
    i=0
    while (i < len(archivos)):
            numero = numeros[i][0]
            open(ruta+'/'+str(numero)+'_'+str(i)+'.pdf', 'wb').write(base64.decodestring(str(archivos[i][0])))
            i = i + 1

    #Devolvemos el numero
    print numeros[0][0]

cursor.close()
cursorN.close()
conn.close()
