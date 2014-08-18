from openerp.osv import fields, osv

class destaque_articulos(osv.osv):
    
    _name = "destaque_articulos"
    _description = "Destaque"
    
    _columns = {       
        'articulo_divulgativo': fields.many2one('articulo','Articulo'), 
        'articulo_investigacion': fields.many2one('articulo','Articulo'),  
        'revisor_id': fields.integer('Editor'),
        'numero_id':fields.many2one('numero','Numero'), 
        'fecha_fin': fields.date('Plazo Maximo'),
        'state':fields.selection([('start', 'Pendiente'), ('send', 'Confirmado')], 'Estado del proceso'),
        }
    
    def aceptar(self, cr, uid, ids, context=None):
        self.write(cr, uid, ids, { 'state' : 'send'})
    
    
destaque_articulos()