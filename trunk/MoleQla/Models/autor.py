# -*- encoding: utf-8 -*-
from openerp.osv import fields, osv

class autor(osv.osv):
    
    _name = "autor"
    _description = "Autor"
    
    _columns = {
        'user_id': fields.many2one('res.users','Usuario'),
        'nombre' : fields.char('Nombre'),
        'apellidos' : fields.char('Apellidos')
        }
    
    def name_get(self, cr, uid, ids, context=None):
        
        if context is None:
            context = {}
        res = []
        
        for record in self.browse(cr, uid, ids, context=context):
            autor_name = record.nombre
            
            
            res.append((record.id, autor_name))
        return res
        
autor()