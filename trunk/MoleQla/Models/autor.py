from openerp.osv import fields, osv

class autor(osv.osv):
    
    _name = "autor"
    _description = "Autor"
    
    _columns = {
        'nombre' : fields.char('Nombre', size=128),
        'user_id': fields.many2one('res.users','Usuario'),
        }
autor()