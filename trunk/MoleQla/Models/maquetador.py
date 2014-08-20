from openerp.osv import fields, osv

class maquetador(osv.osv):
    
    _name = "maquetador"
    _description = "Maquetador"
    
    _columns = {
        'nombre' : fields.char('Nombre', size=128),
        'apellidos': fields.char('Apellidos', size=128),
        'descripcion': fields.text('Descripcion'),         
        'user_id': fields.many2one('res.users','Usuario'),
        'seccion_id': fields.many2one('seccion','Seccion'),
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