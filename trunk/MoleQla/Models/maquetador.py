from openerp.osv import fields, osv

class maquetador(osv.osv):
    
    _name = "maquetador"
    _description = "Maquetador"
    
    _columns = {
        'nombre' : fields.char('Nombre', size=128),
        'apellidos': fields.char('Apellidos', size=128),        
        'user_id': fields.many2one('res.users','Usuario'),
        'seccion_id': fields.many2one('seccion','Seccion'),
        }
maquetador()