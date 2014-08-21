# -*- encoding: utf-8 -*-
from openerp.osv import fields, osv

class linea_votacion(osv.osv):
    
    _name = "linea_votacion"
    _description = "Linea de Votacion"
    
    _columns = {       
        'articulo': fields.many2one('articulo','Artículo'), 
        'puntos': fields.integer('Puntos'),
        'votacion_inv_id': fields.many2one('votacion','Votación'),
        'votacion_div_id': fields.many2one('votacion','Votación'), 
        }
    
    _defaults = {
        'puntos':0,        
        }
       
linea_votacion()