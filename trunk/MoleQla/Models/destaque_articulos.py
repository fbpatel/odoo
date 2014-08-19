from openerp.osv import fields, osv

class destaque_articulos(osv.osv):
    
    _name = "destaque_articulos"
    _description = "Destaque"
    
    _columns = {       
        'articulo_divulgativo': fields.many2one('articulo','Articulo Divulgativo'), 
        'articulo_investigacion': fields.many2one('articulo','Articulo de Investigacion'),  
        'revisor_id': fields.integer('Editor'),
        'numero_id':fields.many2one('numero','Numero'), 
        'state':fields.selection([('start', 'Pendiente'), ('send', 'Confirmado')], 'Estado del proceso'),
        'seccion_id': fields.many2one('seccion','Seccion'), 
        }
    
    _defaults = {
        'state' : 'start'
                 }
    
    def aceptar(self, cr, uid, ids, context=None):
        art_divulgativo = self.browse(cr, 1, ids, context).articulo_divulgativo
        art_investigacion = self.browse(cr, 1, ids, context).articulo_investigacion
        obj_articulo= self.pool.get('articulo')
        obj_articulo.write(cr, 1, art_divulgativo.id, { 'destacado' : 'TRUE'  })
        obj_articulo.write(cr, 1, art_investigacion.id, { 'destacado' : 'TRUE' })
        self.write(cr, 1, ids, { 'state' : 'send'})
    
    
destaque_articulos()