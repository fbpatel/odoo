from openerp.osv import fields, osv

class correo(osv.osv):
    
    _name = "correo"
    _description = "Correo"
    
    _columns = {
        'emailnotificacion' : fields.char('Email', size=128),
        'passwordnotificacion': fields.char('Password', size=128),
        }
correo()