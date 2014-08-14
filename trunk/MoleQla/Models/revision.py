from openerp.osv import fields, osv

class revision(osv.osv):
    
    _name = "revision"
    _description = "Revision"
    
    _columns = {       
        'articulo_id': fields.many2one('articulo','Articulo'), 
        'filename': fields.char('Filename'), 
        'seccion_id': fields.many2one('seccion','Seccion'),
        'observaciones': fields.binary('Observaciones'),
        'filenameObv': fields.char('FilenameObv'),  
        'revisor_id': fields.integer('Editor'),
        'state':fields.selection([('start', 'En Revision'), ('send', 'Aceptado'), ('cancel', 'Rechazado')], 'Estado de la revision'),
        'comentarios': fields.text('Comentarios'),
        'versiones_anteriores' : fields.one2many('articulo', 'old_revision_id','Version anterior'),
        }
    
    _defaults = {
                  'state':'start',
                  'filenameObv':'observaciones.pdf'
                  }
    
    
    
    def aceptar(self, cr, uid, ids, context=None):
        revision = self.browse(cr, uid, ids, context)
        self.write(cr, uid, ids, { 'state' : 'send' })
        articulo_obj = self.pool.get('articulo')
        
        maquetador_obj = self.pool.get('maquetador')
        maquetador_id = maquetador_obj.search(cr, uid, [('seccion_id', '=', revision.seccion_id.id)])
        maquetador = maquetador_obj.browse(cr, uid, maquetador_id, context)
        vals = {'articulo_id':revision.articulo_id.id,'seccion_id':revision.seccion_id.id,'maquetador_id':maquetador[0].user_id.id}
        maquetacion_obj = self.pool.get('maquetacion')
        maquetacion_obj.create(cr, 1, vals,context=None)
        maquetacion_id = maquetacion_obj.search(cr, uid, [('articulo_id', '=', revision.articulo_id.id)])
        articulo_obj.write(cr, 1, revision.articulo_id.id, { 'state' : 'editing', 'maquetacion_id':maquetacion_id[0] })
        
    def rechazar(self, cr, uid, ids, context=None):
        revision = self.browse(cr, uid, ids, context)
        self.write(cr, uid, ids, { 'state' : 'cancel' })
        articulo_obj = self.pool.get('articulo') 
        articulo_id = articulo_obj.search(cr, uid, [('revision_id', '=', revision.id)])
        articulo = articulo_obj.browse(cr, uid, articulo_id, context)
        
        vals = {'seccion_id':revision.seccion_id.id,
                'archivo':articulo.archivo,'filename':articulo.filename,'nombre':articulo.nombre,
                'tipo_articulo':articulo.tipo_articulo,'tipo_autor':articulo.tipo_autor,
                'palabras_clave':articulo.palabras_clave,'user_id':1,'old_revision_id':revision.id}
        articulo_obj.create(cr, 1, vals, context=None)
        articulo_obj.write(cr, 1, revision.articulo_id.id, { 'state' : 'cancel' })
        
    
    
    
    
revision()