#!/usr/bin/python

import psycopg2
import base64
import sys

ruta = sys.argv[1]
rutaEntrevista = sys.argv[2]
print ruta

conn_string = "host='localhost' dbname='moleqla' user='openuser' password='openerp'"
conn = psycopg2.connect(conn_string)

cursor = conn.cursor()
cursor.execute("SELECT A.archivo FROM articulo A WHERE (SELECT state FROM numero N WHERE N.id = A.numero_id) = 'a_publicar' ORDER BY A.seccion_id")
archivos = cursor.fetchall()

cursorN = conn.cursor()
cursorN.execute("SELECT numero_id FROM articulo A WHERE (SELECT state FROM numero N WHERE N.id = A.numero_id) = 'a_publicar' ORDER BY A.seccion_id")
numeros = cursorN.fetchall()

#Consulta para la entrevista
cursorEntrevista = conn.cursor()
cursorEntrevista.execute("SELECT N.entrevista FROM numero N WHERE N.state = 'a_publicar'")
entrevista = cursorEntrevista.fetchone()

#Si no hay ningun numero nuevo se devulve false
if len(archivos) == 0:
    print "False"
else:
    i=0
    while (i < len(archivos)):
            numero = numeros[i][0]
            open(ruta+'/'+str(numero)+'_'+str(i)+'.pdf', 'wb').write(base64.decodestring(str(archivos[i][0])))
            i = i + 1

   #Se crea la entrevista
    open(rutaEntrevista+'/entrevista.pdf', 'wb').write(base64.decodestring(str(entrevista[0])))
    cursorEntrevista.close()


    #Devolvemos el numero
    print numeros[0][0]

cursor.close()
cursorN.close()
conn.close()
