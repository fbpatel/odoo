from openerp.osv import fields, osv

class seccion(osv.osv):
    
    _name = "seccion"
    _description = "Seccion"
    
    _columns = {
        'nombre' : fields.char('Nombre', size=128),
        'descripcion': fields.char('Descripcion', size=128),
        'editor':fields.one2many('editor', 'seccion_id','Editor'), 
        'maquetador':fields.one2many('maquetador', 'seccion_id','Maquetador'),
        }
    
    def name_get(self, cr, uid, ids, context=None):
        
        if context is None:
            context = {}
        res = []
        
        for record in self.browse(cr, uid, ids, context=context):
            seccion_name = record.nombre
            
            
            res.append((record.id, seccion_name))
        return res
    
seccion()