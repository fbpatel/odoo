# -*- encoding: utf-8 -*-
from openerp.osv import fields, osv

class linea_votacion(osv.osv):
    
    _name = "linea_votacion"
    _description = "Linea de Votacion"
    
    _columns = {       
        'articulo': fields.many2one('articulo','Artículo'), 
        'filename': fields.char('Filename'),
        'articulo_nombre' : fields.related('articulo', 'nombre', string='Nombre', type='char', readonly=True),
        'articulo_seccion' : fields.related('articulo', 'seccion_id', string='Sección', type='many2one', relation='seccion',readonly=True),
        'articulo_archivo' : fields.related('articulo', 'archivo', string='Archivo', type='binary', readonly=True),
        'puntos': fields.integer('Puntos'),
        'votacion_inv_id': fields.many2one('votacion','Votación'),
        'votacion_div_id': fields.many2one('votacion','Votación'), 
        }
    
    _defaults = {
        'puntos':0,        
        }
       
linea_votacion()