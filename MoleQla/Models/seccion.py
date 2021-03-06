# -*- encoding: utf-8 -*-
from openerp.osv import fields, osv

class seccion(osv.osv):
    
    _name = "seccion"
    _description = "Seccion"
    
    _columns = {
        'nombre' : fields.char('Nombre', size=128),
        'descripcion': fields.char('Descripción', size=128),
        'max_articulos': fields.integer('Número máximo de artículos por número'),
        'editor':fields.one2many('editor', 'seccion_id','Editor'), 
        'maquetador':fields.one2many('maquetador', 'seccion_id','Maquetador'),
        }
                            
    _defaults = {
        'max_articulos' : 3, 
        }
    
    def name_get(self, cr, uid, ids, context=None):
        
        if context is None:
            context = {}
        res = []
        
        for record in self.browse(cr, 1, ids, context=context):
            seccion_name = record.nombre
            
            
            res.append((record.id, seccion_name))
        return res
    
seccion()