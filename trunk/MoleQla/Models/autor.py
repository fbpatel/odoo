from openerp.osv import fields, osv

class autor(osv.osv):
    
    _name = "autor"
    _description = "Autor"
    
    _columns = {
        'user_id': fields.many2one('res.users','Usuario'),
        'iden_partner' : fields.related('user_id', 'partner_id', string='Identificador Partner', relation="res.users",type='many2one', readonly=True),
        'nombre' : fields.related('iden_partner', 'display_name', string='Nombre', relation="res.partner",type='char')
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