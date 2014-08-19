from openerp.osv import fields, osv

class maquetacion(osv.osv):
    
    _name = "maquetacion"
    _description = "Maquetacion"
    
    _columns = {       
        'articulo_id': fields.many2one('articulo','Articulo'), 
        'seccion_id': fields.many2one('seccion','Seccion'),
        'observaciones': fields.binary('Observaciones'),
        'filenameObv': fields.char('FilenameObv'),  
        'maquetador_id': fields.integer('Maquetador'),
        'state':fields.selection([('start', 'En Maquetacion'), ('send', 'Maquetado'), ('cancel', 'Rechazado')], 'Estado de la maquetacion'),
        'comentarios': fields.text('Comentarios'),
        'versiones_anteriores' : fields.one2many('articulo', 'old_maquetacion_id','Version anterior'),
        'articulo_nombre' : fields.related('articulo_id', 'nombre', string='Nombre', type='text', readonly=True),
        'articulo_descripcion' : fields.related('articulo_id', 'descripcion', string='Descripcion', type='text', readonly=True),
        'articulo_seccion' : fields.related('articulo_id', 'seccion_id', string='Seccion', type='text', readonly=True),
        'articulo_tipoArticulo' : fields.related('articulo_id', 'tipo_articulo', string='Tipo Articulo', type='text', readonly=True),
        'articulo_tipoAutor' : fields.related('articulo_id', 'tipo_autor', string='Tipo Autor', type='text', readonly=True),
        'filenameArt': fields.char('FilenameObv'),
        'articulo_archivo' : fields.related('articulo_id', 'archivo', string='Archivo', type='binary', readonly=True)
        }
    
    _defaults = {
                  'state':'start',
                  'filenameObv':'observaciones.pdf',
                  'filenameArt': 'articulo.pdf'
                  }
    
    
    
    def aceptar(self, cr, uid, ids, context=None):
        maquetacion = self.browse(cr, uid, ids, context)
        articulo_obj = self.pool.get('articulo')
        d = fields.date.today()
        articulo_obj.write(cr, 1, maquetacion.articulo_id.id, { 'state' : 'published', 'fecha_maq':d})
        self.write(cr, uid, ids, { 'state' : 'send' })
        
        #2. Mediante el articulo
        autor_user_obj = self.pool.get('res.users')
        autor_user = autor_user_obj.browse(cr, uid, maquetacion.articulo_id.user_id, context)
        email_autor = autor_user.login
        estado = "publicable"
        
        # Asunto y texto del email
        asunto = "Articulo " + maquetacion.articulo_id.nombre
        texto = "Su articulo ha cambiado su estado a <b>" + estado + "</b>. En breve recibira mas noticias sobre su estado."
        
        # Se envia el correo
        correo_obj = self.pool.get('correo')        
        correo_obj.mail(cr, 1, email_autor, asunto, texto)  
        
        
    def rechazar(self, cr, uid, ids, context=None):
        maquetacion = self.browse(cr, uid, ids, context)
        self.write(cr, uid, ids, { 'state' : 'cancel'})
        articulo_obj = self.pool.get('articulo')
        articulo_id = articulo_obj.search(cr, uid, [('maquetacion_id', '=', maquetacion.id)])
        articulo = articulo_obj.browse(cr, uid, articulo_id, context)
        
        vals = {'seccion_id':maquetacion.seccion_id.id,
                'archivo':articulo.archivo,'filename':articulo.filename,'nombre':articulo.nombre,
                'tipo_articulo':articulo.tipo_articulo,'tipo_autor':articulo.tipo_autor,
                'palabras_clave':articulo.palabras_clave,'user_id':1,'old_maquetacion_id':maquetacion.id}
        articulo_obj.create(cr, 1, vals, context=None)
        
        articulo_obj.write(cr, 1, maquetacion.articulo_id.id, { 'state' : 'cancel_m' })
        
        #2. Mediante el articulo
        autor_user_obj = self.pool.get('res.users')
        autor_user = autor_user_obj.browse(cr, uid, maquetacion.articulo_id.user_id, context)
        email_autor = autor_user.login
        estado = "rechazado por el maquetador de seccion"
        
        # Asunto y texto del email
        asunto = "Articulo " + maquetacion.articulo_id.nombre
        texto = "Su articulo ha cambiado su estado a <b>" + estado + "</b>. En breve recibira mas noticias sobre su estado."
        
        # Se envia el correo
        correo_obj = self.pool.get('correo')        
        correo_obj.mail(cr, 1, email_autor, asunto, texto) 
        
    
    
    
    
maquetacion()