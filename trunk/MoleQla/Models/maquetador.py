# -*- encoding: utf-8 -*-
from openerp.osv import fields, osv

class maquetador(osv.osv):
    
    _name = "maquetador"
    _description = "Maquetador"
    
    _columns = {
        'nombre' : fields.char('Nombre', size=128),
        'apellidos': fields.char('Apellidos', size=128),
        'descripcion': fields.text('Descripción'),         
        'user_id': fields.many2one('res.users','Usuario'),
        'seccion_id': fields.many2one('seccion','Sección'),
        }

    def name_get(self, cr, uid, ids, context=None):
        
        if context is None:
            context = {}
        res = []
        
        for record in self.browse(cr, uid, ids, context=context):
            maquetador_name = record.nombre
            
            
            res.append((record.id, maquetador_name))
        return res
maquetador()