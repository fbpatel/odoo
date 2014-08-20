from openerp.osv import fields, osv

class linea_votacion(osv.osv):
    
    _name = "linea_votacion"
    _description = "Linea de Votacion"
    
    _columns = {       
        'articulo': fields.many2one('articulo','Articulo'), 
        'puntos': fields.integer('Puntos'),
        'votacion_id': fields.many2one('votacion','Votacion'), 
        }
    
    _defaults = {
        'puntos':0,        
        }
       
linea_votacion()